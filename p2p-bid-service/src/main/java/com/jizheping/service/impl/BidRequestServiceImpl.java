package com.jizheping.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jizheping.accountFeign.AccountFeign;
import com.jizheping.api.entity.*;
import com.jizheping.api.exception.GlobalException;
import com.jizheping.api.util.BidConst;
import com.jizheping.api.vo.BidRequestAuditQueryObject;
import com.jizheping.api.vo.CodeMsg;
import com.jizheping.api.vo.PageResult;
import com.jizheping.mapper.BidMapper;
import com.jizheping.mapper.BidRequestAuditHistoryMapper;
import com.jizheping.mapper.BidRequestMapper;
import com.jizheping.service.BidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;

@Service
public class BidRequestServiceImpl implements BidRequestService {
    //标的Mapper接口
    @Autowired
    private BidRequestMapper bidRequestMapper;

    @Autowired
    private BidMapper bidMapper;

    @Autowired
    private AccountFeign accountFeign;

    //标的审核记录Mapper接口
    @Autowired
    private BidRequestAuditHistoryMapper bidRequestAuditHistoryMapper;

    //添加标的信息
    @Override
    public void saveBidRequest(BidRequest bidRequest) {
        bidRequestMapper.addBitRequest(bidRequest);
    }

    /**
     * 根据条件查询所有标的信息
     * @param queryObject    封装了查询标的的条件
     * @return
     */
    @Override
    public PageResult selectBidRequestByState(BidRequestAuditQueryObject queryObject) {
        //使用pageHelper设置分页信息
        PageHelper.startPage(queryObject.getCurrentPage(),queryObject.getPageSize());

        //获取条件查询的结果
        List<BidRequest> bidRequestList = bidRequestMapper.selectBidRequestByCondition(queryObject);

        //使用SimpleDateFormat对象对查询出的时间进行格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //循环遍历查询结果集
        for (BidRequest bidRequest : bidRequestList){
            //将格式化后的时间赋值给自定义String类型的时间，用于前台展示
            bidRequest.setApplyTime(simpleDateFormat.format(bidRequest.getApplyDate()));
        }

        //将查询结果集放到PageInfo中
        PageInfo<BidRequest> pageInfo = new PageInfo<>(bidRequestList);

        //封装返回结果对象
        PageResult pageResult = new PageResult((int)pageInfo.getTotal(),queryObject.getPageSize(),
                queryObject.getCurrentPage(),pageInfo.getList());

        return pageResult;
    }

    /**
     * 标的审核发标前审核
     * @param bidRequestId   被审核的标的Id
     * @param auditState     审核结果
     * @param remark         审核意见
     * @param auditorId      审核人Id
     */
    @Override
    public void doAuditForPublish(BidRequestAuditHistory bidRequestAuditHistory) throws ParseException {
        BidRequest bidRequest = bidRequestMapper.selectBidRequestById(bidRequestAuditHistory.getBidRequestId());

        //获取标的状态用于判断标的审核是否通过
        int bidRequestState = bidRequest.getBidRequestState();

        //修改借款表信息表中的状态信息
        if(bidRequestAuditHistory.getState() == BidConst.STATE_PASS){
            //审核通过，将状态信息修改为招标中
            bidRequestState = BidConst.BIDREQUEST_STATE_BIDDING;
        }else if(bidRequestAuditHistory.getState() == BidConst.STATE_REJECT){
            //审核不通过，将状态信息修改为发表前审核失败
            bidRequestState = BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE;
        }
        bidRequest.setBidRequestState(bidRequestState);

        Calendar fromCal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("(yyyy-MM-dd HH:mm:ss)");
        //将当前时间赋值给审核时间
        Date date = new Date();
        bidRequest.setPublishDate(date);
        //将时间增加指定天数，并赋值给数据库中的disableDate
        String dateString = dateFormat.format(date);
        date = dateFormat.parse(dateString);
        fromCal.setTime(date);
        fromCal.add(Calendar.DATE,bidRequest.getDisableDays());
        date = fromCal.getTime();
        //将增加后的时间复制给标的结束时间
        bidRequest.setDisableDate(date);

        int row = bidRequestMapper.updateBidRequest(bidRequest);
        //判断标的信息是否成功，失败则抛出异常
        if(row<=0){
            throw new GlobalException(CodeMsg.BORROW_OPTIMISTIC_LOCK_FAIL.fillArgs(Long.toString(bidRequestAuditHistory.getBidRequestId())));
        }

        //创建标的审核对象，用于向数据    库中进行添加操作
        BidRequestAuditHistory bidRequestAuditHistory1 = new BidRequestAuditHistory();
        bidRequestAuditHistory1.setAuditType(BidConst.PUBLISH_AUDIT);//标的审核类型(发表前审核、满标一审、满标二审)
        bidRequestAuditHistory1.setBidRequestId(bidRequestAuditHistory.getBidRequestId());//被审核的标的id
        bidRequestAuditHistory1.setApplier(bidRequest.getCreateUser());//审核申请人
        bidRequestAuditHistory1.setApplyTime(bidRequest.getApplyDate());//申请时间
        bidRequestAuditHistory1.setAuditor(bidRequestAuditHistory.getAuditor());//审核人
        bidRequestAuditHistory1.setRemark(bidRequestAuditHistory.getRemark());//审核意见
        bidRequestAuditHistory1.setState(bidRequestAuditHistory.getState());//审核结果状态值

        bidRequestAuditHistoryMapper.saveBidRequestAuditHistory(bidRequestAuditHistory1);
    }

    //满标一审
    public void doAuditForOne(BidRequestAuditHistory bidRequestAuditHistory) throws ParseException {
        BidRequest bidRequest = bidRequestMapper.selectBidRequestById(bidRequestAuditHistory.getBidRequestId());

        System.out.println("bidRequest === " + bidRequest);

        //获取标的状态用于判断标的审核是否通过
        int bidRequestState = bidRequest.getBidRequestState();

        //修改借款表信息表中的状态信息
        if(bidRequestAuditHistory.getState() == BidConst.STATE_PASS){
            //审核通过，将状态信息修改为满标二审
            bidRequestState = BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2;
        }else if(bidRequestAuditHistory.getState() == BidConst.STATE_REJECT){
            //审核不通过，将状态信息修改为满标审核失败
            bidRequestState = BidConst.BIDREQUEST_STATE_REJECTED;
        }
        bidRequest.setBidRequestState(bidRequestState);

        int row = bidRequestMapper.updateBidRequest(bidRequest);
        //判断标的信息是否成功，失败则抛出异常
        if(row<=0){
            throw new GlobalException(CodeMsg.BORROW_OPTIMISTIC_LOCK_FAIL.fillArgs(Long.toString(bidRequestAuditHistory.getBidRequestId())));
        }

        //创建标的审核对象，用于向数据库中进行
        // 添加操作
        BidRequestAuditHistory bidRequestAuditHistory1 = new BidRequestAuditHistory();
        bidRequestAuditHistory1.setAuditType(BidConst.FULL_AUDIT1);//标的审核类型(发表前审核、满标一审、满标二审)
        bidRequestAuditHistory1.setBidRequestId(bidRequestAuditHistory.getBidRequestId());//被审核的标的id
        bidRequestAuditHistory1.setApplier(bidRequest.getCreateUser());//审核申请人
        bidRequestAuditHistory1.setApplyTime(bidRequest.getApplyDate());//申请时间
        bidRequestAuditHistory1.setAuditor(bidRequestAuditHistory.getAuditor());//审核人
        bidRequestAuditHistory1.setRemark(bidRequestAuditHistory.getRemark());//审核意见
        bidRequestAuditHistory1.setState(bidRequestAuditHistory.getState());//审核结果状态值

        bidRequestAuditHistoryMapper.saveBidRequestAuditHistory(bidRequestAuditHistory1);
    }

    //满标二审
    public void doAuditForTwo(BidRequestAuditHistory bidRequestAuditHistory) throws ParseException {
        BidRequest bidRequest = bidRequestMapper.selectBidRequestById(bidRequestAuditHistory.getBidRequestId());

        //获取标的状态用于判断标的审核是否通过
        int bidRequestState = bidRequest.getBidRequestState();

        //修改借款表信息表中的状态信息
        if(bidRequestAuditHistory.getState() == BidConst.STATE_PASS){
            //审核通过，将状态信息修改为已放款
            bidRequestState = BidConst.BIDREQUEST_STATE_BACK;
        }else if(bidRequestAuditHistory.getState() == BidConst.STATE_REJECT){
            //审核不通过，将状态信息修改为满标二审审核失败
            bidRequestState = BidConst.BIDREQUEST_STATE_REJECTED;
        }
        bidRequest.setBidRequestState(bidRequestState);

        int row = bidRequestMapper.updateBidRequest(bidRequest);
        //判断标的信息是否成功，失败则抛出异常
        if(row<=0){
            throw new GlobalException(CodeMsg.BORROW_OPTIMISTIC_LOCK_FAIL.fillArgs(Long.toString(bidRequestAuditHistory.getBidRequestId())));
        }

        //创建标的审核对象，用于向数据库中进行
        // 添加操作
        BidRequestAuditHistory bidRequestAuditHistory1 = new BidRequestAuditHistory();
        bidRequestAuditHistory1.setAuditType(BidConst.FULL_AUDIT1);//标的审核类型(发表前审核、满标一审、满标二审)
        bidRequestAuditHistory1.setBidRequestId(bidRequestAuditHistory.getBidRequestId());//被审核的标的id
        bidRequestAuditHistory1.setApplier(bidRequest.getCreateUser());//审核申请人
        bidRequestAuditHistory1.setApplyTime(bidRequest.getApplyDate());//申请时间
        bidRequestAuditHistory1.setAuditor(bidRequestAuditHistory.getAuditor());//审核人
        bidRequestAuditHistory1.setRemark(bidRequestAuditHistory.getRemark());//审核意见
        bidRequestAuditHistory1.setState(bidRequestAuditHistory.getState());//审核结果状态值

        bidRequestAuditHistoryMapper.saveBidRequestAuditHistory(bidRequestAuditHistory1);
    }

    /**
     * 根据Id查询标的信息
     * @param id
     * @return
     */
    @Override
    public BidRequest getBidRequestById(Long id) {
        return bidRequestMapper.selectBidRequestById(id);
    }

    /**
     * 投标方法
     * @param bid      被投标的标的id
     * @param amount   投标金额
     * @param userId   投标人
     */
    @Override
    public void tender(Long bid, BigDecimal amount,Long userId) {
        BidRequest bidRequest = bidRequestMapper.selectBidRequestById(bid);

        bidRequest.setCurrentSum(bidRequest.getCurrentSum().add(amount));
        bidRequest.setBidCount(bidRequest.getBidCount() + 1);

        bidRequestMapper.updateBidRequest(bidRequest);

        Bid bidClass = new Bid();
        bidClass.setActualRate(bidRequest.getCurrentRate());
        bidClass.setAvailableAmount(amount);
        bidClass.setBidRequestId(bid);
        bidClass.setBidTime(new Date());
        bidClass.setBidRequestTitle(bidRequest.getTitle());
        bidClass.setBidUser(LoginInfo.empty(userId));

        bidMapper.insertBid(bidClass);

        Account account = new Account();

        account.setId(userId);
        account.setFreezedAmount(amount);

        int i = accountFeign.updateAccount(account);

        BidRequest bidRequested = bidRequestMapper.selectBidRequestById(bid);

        if(bidRequested.getCurrentSum().equals(bidRequested.getBidRequestAmount())){
            bidRequested.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);

            bidRequestMapper.updateBidRequest(bidRequested);
        }
    }

    //修改标的信息
    @Override
    public void updateBidRequest(BidRequest bidRequest){
        bidRequestMapper.updateBidRequest(bidRequest);
    }

    @Override
    public List<BidRequest> getBidRequestListByCreateUserId(Long id) {
        List<BidRequest> list = bidRequestMapper.getBidRequestListByCreateUserId(id);

        for(BidRequest bidRequest : list){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            bidRequest.setApplyTime(simpleDateFormat.format(bidRequest.getApplyDate()));
        }

        return list;
    }
}
