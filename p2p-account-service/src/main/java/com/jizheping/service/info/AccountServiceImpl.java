package com.jizheping.service.info;

import com.jizheping.api.entity.*;
import com.jizheping.api.util.BidConst;
import com.jizheping.api.util.CaculatorUtil;
import com.jizheping.api.util.JsonUtils;
import com.jizheping.dao.AccountDao;
import com.jizheping.dao.AccountFlowDao;
import com.jizheping.mapper.PaymentScheduleDetailMapper;
import com.jizheping.mapper.PaymentScheduleMapper;
import com.jizheping.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AccountFlowDao accountFlowDao;

    @Autowired
    private PaymentScheduleDetailMapper paymentScheduleDetailMapper;

    @Autowired
    private PaymentScheduleMapper paymentScheduleMapper;

    /**
     * @param account   控制层使用try-catch捕获异常信息，所以无返回
     */
    @Override
    public void createAccount(Account account) {
        accountDao.save(account);
    }

    /**
     * @param id   主键Id
     * @return   返回对应的账户信息
     */
    @Override
    public Account selectAccountById(Long id) {
        Optional<Account> optional = accountDao.findById(id);
        //三目表达式
        //判断查询结果是否存在，存在正常返回查询结果，错误返回空
        return optional.isPresent()?optional.get():null;
    }



    @Override
    public void updateAccount(Account account) {
        Account account1 = accountDao.findById(account.getId()).get();

        account1.setFreezedAmount(account1.getFreezedAmount().add(account.getFreezedAmount()));
        account1.setUsableAmount(account1.getUsableAmount().subtract(account.getFreezedAmount()));

        accountDao.save(account1);

        AccountFlow accountFlow = new AccountFlow();

        accountFlow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_BID_FREEZED);
        accountFlow.setAmount(account.getFreezedAmount());
        accountFlow.setNote("参与投标");
        accountFlow.setUseableAmount(account1.getUsableAmount());
        accountFlow.setFreezedAmount(account1.getFreezedAmount());
        accountFlow.setActionTime(new Date());
        accountFlow.setAccountId(account.getId());

        accountFlowDao.save(accountFlow);
    }

    //修改发标人帐户信息、添加动账记录信息，用于放款
    @Override
    public void updateAccountForLoan(Account account){
        //获取对应的账户信息，对账户中的可用余额、待还本息、剩余授信额度进行修改
        Account account1 = accountDao.findById(account.getId()).get();

        //可用额度增加
        account1.setUsableAmount(account1.getUsableAmount().add(account.getUsableAmount()));
        //待还本息增加
        account1.setUnReturnAmount(account1.getUnReturnAmount().add(account.getUnReturnAmount()));
        //剩余授信额度减少
        account1.setRemainBorrowLimit(account1.getRemainBorrowLimit().subtract(account.getRemainBorrowLimit()));
        //将修改后的信息更新到数据库中
        accountDao.save(account1);
        //创建accountFlow对象用于向accountFlow中插入新的动账记录信息
        AccountFlow accountFlow = new AccountFlow();
        //添加动账类型(成功借款)
        accountFlow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL);
        //添加动账金额(可用余额增加的数量)
        accountFlow.setAmount(account.getUsableAmount());
        //添加动账说明
        accountFlow.setNote("借款成功，借款金额到账");
        //添加动账后的可用金额
        accountFlow.setUseableAmount(account1.getUsableAmount());
        //添加动账后的冻结金额
        accountFlow.setFreezedAmount(account1.getFreezedAmount());
        //添加动账时间
        accountFlow.setActionTime(new Date());
        //添加所属账户id
        accountFlow.setAccountId(account1.getId());
        //将accountFlow对象更新到数据库中
        accountFlowDao.save(accountFlow);
    }

    //修改对应账户信息、添加对应的还款计划、回款计划
    @Override
    public void updateAccounts(Map<String,String> map) {
        //获取对应的标的信息
        BidRequest bidRequest = JsonUtils.toBean(map.get("bidRequest"),BidRequest.class);
        //获取还款期数
        int monthes2Return = bidRequest.getMonthes2Return();
        List bidJsonList = JsonUtils.toBean(map.get("bidJsonList"),List.class);
        //获取对应的投标记录
        for(Object obj: bidJsonList){
            Bid bid = JsonUtils.toBean(obj.toString(),Bid.class);
            //获取对应投标人的账户信息
            Account account = accountDao.findById(bid.getBidUser().getId()).get();
            //将账户中的冻结金额减少
            account.setFreezedAmount(account.getFreezedAmount().subtract(bid.getAvailableAmount()));
            //计算待收利息
            BigDecimal unReturnInterest = bid.getAvailableAmount().divide(bidRequest.getBidRequestAmount(),BidConst.SCALE_CACULATE,BigDecimal.ROUND_HALF_UP).multiply(bidRequest.getTotalRewardAmount()).divide(BigDecimal.valueOf(1),BidConst.SCALE_STORE,BigDecimal.ROUND_HALF_UP);
            //添加待收利息
            account.setUnReceiveInterest(account.getUnReceiveInterest().add(unReturnInterest));
            //添加待收本金
            account.setUnReceivePrincipal(account.getUnReceivePrincipal().add(bid.getAvailableAmount()));
            //将修改后的账户信息更新到数据库中
            accountDao.save(account);

            //创建动账记录对象，存储动账信息（将相应的冻结金额扣除）
            AccountFlow accountFlow = new AccountFlow();
            //添加动账类型
            accountFlow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL);
            //添加动账金额
            accountFlow.setAmount(bid.getAvailableAmount());
            //添加动账说明（借款成功，账户中的动账额度减少）
            accountFlow.setNote("借款成功，账户中的动账额度减少");
            //添加动账后的可用金额
            accountFlow.setUseableAmount(account.getUsableAmount());
            //添加动账后的冻结余额
            accountFlow.setFreezedAmount(account.getFreezedAmount());
            //添加动账时间
            accountFlow.setActionTime(new Date());
            //添加所属账户的id
            accountFlow.setAccountId(account.getId());
            //将当前动账信息添加到数据库
            accountFlowDao.save(accountFlow);
        }

        for(int i=0;i<monthes2Return;i++){
            PaymentSchedule paymentSchedule = new PaymentSchedule();
            //获取每一期的还款期限
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH,i+1);
            Date endLine = calendar.getTime();
            //将还款时间添加到还款计划中
            paymentSchedule.setDeadLine(endLine);
            //设置还款金额
            paymentSchedule.setTotalAmount(CaculatorUtil.calMonthToReturnMoney(bidRequest.getReturnType(),bidRequest.getBidRequestAmount(),bidRequest.getCurrentRate(),i+1,bidRequest.getMonthes2Return()));
            //设置还款本金
            paymentSchedule.setPrincipal(CaculatorUtil.calMonthToReturnMoney(bidRequest.getReturnType(),bidRequest.getBidRequestAmount(),bidRequest.getCurrentRate(),i+1,bidRequest.getMonthes2Return()).subtract(CaculatorUtil.calMonthlyInterest(bidRequest.getReturnType(),bidRequest.getBidRequestAmount(),bidRequest.getCurrentRate(),i+1,bidRequest.getMonthes2Return())));
            //设置还款利息
            paymentSchedule.setInterest(CaculatorUtil.calMonthlyInterest(bidRequest.getReturnType(),bidRequest.getBidRequestAmount(),bidRequest.getCurrentRate(),i+1,bidRequest.getMonthes2Return()));
            //设置这是第几期还款
            paymentSchedule.setMonthIndex(i+1);
            //设置还款状态(正常待还、已还款、等待还款)
            paymentSchedule.setState(BidConst.PAYMENT_STATE_NORMAL);
            //设置借款标类型
            paymentSchedule.setBidRequestType(bidRequest.getBidRequestType());
            //设置还款方式
            paymentSchedule.setReturnType(bidRequest.getReturnType());
            //设置借款标id
            paymentSchedule.setBidRequestId(bidRequest.getId());
            //设置借款人id
            paymentSchedule.setBorrowUser(bidRequest.getCreateUser());
            //设置借款标标题
            paymentSchedule.setBidRequestTitle(bidRequest.getTitle());
            //将还款计划信息添加到数据库中
            paymentScheduleMapper.insertPaymentSchedule(paymentSchedule);
            //获取还款计划的id
            Long id = paymentSchedule.getId();

            for(Object obj : bidJsonList){
                Bid bid = JsonUtils.toBean(obj.toString(),Bid.class);
                //创建一个回款计划实体类
                PaymentScheduleDetail paymentScheduleDetail = new PaymentScheduleDetail();
                //添加投标金额
                paymentScheduleDetail.setBidAmount(bid.getAvailableAmount());
                //添加投标id(投标记录表主键)
                paymentScheduleDetail.setBidId(bid.getId());
                //添加该期待收总金额
                paymentScheduleDetail.setTotalAmount(CaculatorUtil.calMonthToReturnMoney(bidRequest.getReturnType(), bid.getAvailableAmount(), bidRequest.getCurrentRate(), i+1, bidRequest.getMonthes2Return()));
                //添加该期待收本金
                paymentScheduleDetail.setPrincipal(CaculatorUtil.calMonthToReturnMoney(bidRequest.getReturnType(), bid.getAvailableAmount(), bidRequest.getCurrentRate(), i+1, bidRequest.getMonthes2Return()).subtract(CaculatorUtil.calMonthlyInterest(bidRequest.getReturnType(),bid.getAvailableAmount(),bidRequest.getCurrentRate(),i+1,bidRequest.getMonthes2Return())));
                //添加该期待收利息
                paymentScheduleDetail.setInterest(CaculatorUtil.calMonthlyInterest(bidRequest.getReturnType(),bid.getAvailableAmount(),bidRequest.getCurrentRate(),i+1,bidRequest.getMonthes2Return()));
                //添加还款期数（这是第几期）
                paymentScheduleDetail.setMonthIndex(i+1);
                //添加借款表id
                paymentScheduleDetail.setBidRequestId(bidRequest.getId());
                //添加还款方式
                paymentScheduleDetail.setReturnType(bidRequest.getReturnType());
                //添加所属还款计划id
                paymentScheduleDetail.setPaymentScheduleId(id);
                //添加借款人id
                paymentScheduleDetail.setFromLogininfo(bidRequest.getCreateUser());
                //添加出借人id
                paymentScheduleDetail.setToLogininfoId(bid.getBidUser().getId());
                //添加本期还款截至时间
                paymentScheduleDetail.setDeadline(endLine);
                //将回款计划添加到数据库中
                paymentScheduleDetailMapper.insert(paymentScheduleDetail);
            }
        }
    }

    //满标审核失败账户余额返回
    @Override
    public void moneyBack(Map<String, String> map) {
        //获取对应的标的信息
        BidRequest bidRequest = JsonUtils.toBean(map.get("bidRequest"),BidRequest.class);
        //获取投标记录列表
        List bidJsonList = JsonUtils.toBean(map.get("bidJsonList"),List.class);
        //循环遍历
        for(Object obj: bidJsonList){
            Bid bid = JsonUtils.toBean(obj.toString(),Bid.class);
            //获取对应投标人的账户信息
            Account account = accountDao.findById(bid.getBidUser().getId()).get();
            //增加可用资金
            account.setUsableAmount(bid.getAvailableAmount());
            //将账户中的冻结金额减少
            account.setFreezedAmount(account.getFreezedAmount().subtract(bid.getAvailableAmount()));
            //将修改后的账户信息更新到数据库中
            accountDao.save(account);

            //创建动账记录对象，存储动账信息（将相应的冻结金额扣除）
            AccountFlow accountFlow = new AccountFlow();
            //添加动账类型
            accountFlow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL);
            //添加动账金额
            accountFlow.setAmount(bid.getAvailableAmount());
            //添加动账说明（借款成功，账户中的动账额度减少）
            accountFlow.setNote("投资失败，资金返回");
            //添加动账后的可用金额
            accountFlow.setUseableAmount(account.getUsableAmount());
            //添加动账后的冻结余额
            accountFlow.setFreezedAmount(account.getFreezedAmount());
            //添加动账时间
            accountFlow.setActionTime(new Date());
            //添加所属账户的id
            accountFlow.setAccountId(account.getId());
            //将当前动账信息添加到数据库
            accountFlowDao.save(accountFlow);
        }
    }

    //加载还款列表
    @Override
    public List<PaymentSchedule> loadRefundList(Long id) {
        return paymentScheduleMapper.selectPaymentSchduleByUserId(id);
    }

    //还款
    @Override
    public void refundMoney(Long id) {
        //获取对应的还款计划信息
        PaymentSchedule paymentSchedule = paymentScheduleMapper.selectByPrimaryKey(id);
        //修改还款计划状态   (待还 --->   已还)
        paymentSchedule.setState(BidConst.PAYMENT_STATE_DONE);
        //获取当前时间,用于设置还款时间、回款时间
        Date date = new Date();
        //修改还款时间为当前时间
        paymentSchedule.setPayDate(date);
        //将修改后的还款计划信息更新到数据库
        paymentScheduleMapper.updateByPrimaryKey(paymentSchedule);

        //获取对应借款人的账户信息
        Account account = accountDao.findById(paymentSchedule.getBorrowUser().getId()).get();
        //修改借款人的帐户信息   (可用金额减少、待还金额减少、可用授信额度增加)
        account.setUsableAmount(account.getUsableAmount().subtract(paymentSchedule.getTotalAmount()));
        account.setUnReturnAmount(account.getUnReturnAmount().subtract(paymentSchedule.getTotalAmount()));
        account.setRemainBorrowLimit(account.getRemainBorrowLimit().add(paymentSchedule.getPrincipal()));
        //将修改后的账户信息更新到数据库
        accountDao.save(account);

        //创建动账记录对象，用于向数据库中添加动账信息
        AccountFlow accountFlow = new AccountFlow();
        //设置动账类型
        accountFlow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_RETURN_MONEY);
        //设置动账金额
        accountFlow.setAmount(paymentSchedule.getTotalAmount());
        //设置动账说明
        accountFlow.setNote("还款,可用金额减少");
        //设置动账后的可用金额
        accountFlow.setUseableAmount(account.getUsableAmount().subtract(paymentSchedule.getTotalAmount()));
        //设置动账后的冻结金额
        accountFlow.setFreezedAmount(account.getFreezedAmount());
        //设置动账时间
        accountFlow.setActionTime(date);
        //设置所属账户id
        accountFlow.setAccountId(account.getId());
        //将数据添加到数据库
        accountFlowDao.save(accountFlow);

        //根据还款计划的id获取对应回款计划的列表
        List<PaymentScheduleDetail> list = paymentScheduleDetailMapper.selectByPaymentSchedule(id);
        //循环遍历回款计划列表
        for (PaymentScheduleDetail paymentScheduleDetail : list){
            //设置回款时间
            paymentScheduleDetail.setPayDate(date);
            //将更新后的数据更新(update)到数据库
            paymentScheduleDetailMapper.updateByPrimaryKey(paymentScheduleDetail);

            //获取投资人账户信息
            Account account1 = accountDao.findById(paymentScheduleDetail.getToLogininfoId()).get();
            //修改投资人账户信息(可用金额增加、待收本金减少、待收利息减少)
            account1.setUsableAmount(paymentScheduleDetail.getTotalAmount().add(account1.getUsableAmount()));
            account1.setUnReceivePrincipal(account1.getUnReceivePrincipal().subtract(paymentScheduleDetail.getPrincipal()));
            account1.setUnReceiveInterest(account1.getUnReceiveInterest().subtract(paymentScheduleDetail.getInterest()));
            //将更新后的账户信息更新到数据库
            accountDao.save(account1);

            //创建新的动账记录信息，用于向动账记录表中添加
            AccountFlow accountFlow1 = new AccountFlow();
            //设置动账类型
            accountFlow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_CALLBACK_MONEY);
            //设置动账金额
            accountFlow.setAmount(paymentSchedule.getTotalAmount());
            //设置动账说明
            accountFlow.setNote("回款");
            //设置动账后的可用金额
            accountFlow.setUseableAmount(account1.getUsableAmount().add(paymentSchedule.getTotalAmount()));
            //设置动账后的冻结金额
            accountFlow.setFreezedAmount(account1.getFreezedAmount());
            //设置动账时间
            accountFlow.setActionTime(date);
            //设置所属账户id
            accountFlow.setAccountId(account1.getId());
            //将数据添加到数据库
            accountFlowDao.save(accountFlow);
        }
    }
}
