package com.jizheping.api.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 总利息计算工具类
 */
public class CaculatorUtil {
    //一百
    public static final BigDecimal ONE_HUNDRED = new BigDecimal("100.0000");
    //每年有几个月（12）固定值
    public static final BigDecimal NUMBER_MONTHS_OF_YEAR = new BigDecimal("12.0000");
    //本金管理利率
    public static final BigDecimal ACCOUNT_MANAGER_CHARGE_RATE = new BigDecimal("0.0500");
    //利息管理利率
    public static final BigDecimal INTEREST_MANAGER_CHARGE_RATE = new BigDecimal("0.1000");

    /**
     * 计算总利息方法
     * @param totalAmount     本金
     * @param yearRate        年化利率
     * @param month2Return    还款期数
     * @param returnType      还款类型
     * @return                 总利息
     */
    public static BigDecimal caculateTotalInterest(
            BigDecimal totalAmount,
            BigDecimal yearRate,
            int month2Return,
            int returnType
    ){
        //总利息
        BigDecimal totalInterest = BigDecimal.ZERO;

        //已还本金
        BigDecimal returnAmount = BigDecimal.ZERO;

        //月利率计算,保留小数点后八位
        BigDecimal monthRate = yearRate.divide(BigDecimal.valueOf(100),BidConst.SCALE_CACULATE,BigDecimal.ROUND_HALF_UP).divide(BigDecimal.valueOf(12),BidConst.SCALE_CACULATE,BigDecimal.ROUND_HALF_UP);

        if(returnType == BidConst.RETURN_TYPE_MONTH_INTERST_PRINCIPAL){//等额本息
            //月利息  =  本金  *  月利率
            BigDecimal temp1 = totalAmount.multiply(monthRate);
            //总金额率   =   (1+月利率)^借款期数
            BigDecimal temp2 = BigDecimal.ONE.add(monthRate).pow(month2Return);
            //总利率   =   (1+月利率)^借款期数   -   1
            BigDecimal temp3 = temp2.subtract(BigDecimal.ONE);

            //每月还款数目
            BigDecimal monthAmount = temp1.multiply(temp2).divide(temp3,BidConst.SCALE_CACULATE,BigDecimal.ROUND_HALF_UP);

            //总利息   =   每月还款数目   *   借款期数   -   本金
            totalInterest = monthAmount.multiply(BigDecimal.valueOf(month2Return)).subtract(totalAmount);
        }else if(returnType == BidConst.RETURN_TYPE_MONTH_PRINCIPAL){//等额本金
            //总利息=（还款期数+1）×本金×月利率/2
            totalInterest = BigDecimal.valueOf(month2Return).add(BigDecimal.ONE).multiply(totalAmount).multiply(monthRate).divide(BigDecimal.valueOf(2),BidConst.SCALE_CACULATE,BigDecimal.ROUND_HALF_UP);
        }else if(returnType == BidConst.RETURN_TYPE_MONTH_INTERST){//按月到期
            //月利息  =  本金  *  月利率
            BigDecimal temp1 = totalAmount.multiply(monthRate);

            //总利息  =  月利息  *  借款期数
            totalInterest = temp1.multiply(BigDecimal.valueOf(month2Return));
        }

        return totalInterest;
    }

    /**
     * 获取月利率
     *     年利率/12
     */
    public static BigDecimal getMonthlyRate(BigDecimal yearRate) {
        if (yearRate == null)
            return BigDecimal.ZERO;
        return yearRate.divide(ONE_HUNDRED).divide(NUMBER_MONTHS_OF_YEAR, BidConst.SCALE_CACULATE, RoundingMode.HALF_UP);
    }

    /**
     * 计算每期利息方法
     * @param returnType             借款类型
     * @param bidRequestAmount      借款金额
     * @param yearRate              年化利率
     * @param monthIndex            第几期
     * @param month2return          总期数
     * @return                       每期利息
     */
    public static BigDecimal calMonthlyInterest(int returnType,BigDecimal bidRequestAmount,BigDecimal yearRate,int monthIndex,int month2return){
        //每期利息
        BigDecimal monthlyInterest = BigDecimal.ZERO;
        //获取月利率
        BigDecimal monthlyRate = getMonthlyRate(yearRate);
        //判断还款方式
        if(returnType == BidConst.RETURN_TYPE_MONTH_INTERST_PRINCIPAL){//等额本息
            //只借一个月
            if(month2return == 1){
                monthlyInterest = bidRequestAmount.multiply(monthlyRate).setScale(BidConst.SCALE_CACULATE, RoundingMode.HALF_UP);
            }else{
                //月利息
                BigDecimal temp1 = bidRequestAmount.multiply(monthlyRate);
                //总金额率
                BigDecimal temp2 = (BigDecimal.ONE.add(monthlyRate)).pow(month2return);
                //总利率
                BigDecimal temp3 = (BigDecimal.ONE.add(monthlyRate)).pow(month2return).subtract(BigDecimal.ONE);
                //(1+b)^(index-1)
                BigDecimal temp4 = (BigDecimal.ONE.add(monthlyRate)).pow(monthIndex-1);
                //计算每月还款总数
                BigDecimal monthToReturnMoney = temp1.multiply(temp2).divide(temp3,BidConst.SCALE_CACULATE,RoundingMode.HALF_UP);
                //计算总还款
                BigDecimal totalReturnmoney = monthToReturnMoney.multiply(BigDecimal.valueOf(month2return));
                //计算总利息
                BigDecimal totalInterest = totalReturnmoney.subtract(bidRequestAmount);
                //判断当前月是否为最后一期
                if(monthIndex <month2return){
                    monthlyInterest = (temp1.subtract(monthToReturnMoney)).multiply(temp4).add(monthToReturnMoney).setScale(BidConst.SCALE_CACULATE,RoundingMode.HALF_UP);
                }else{
                    BigDecimal temp6 = BigDecimal.ZERO;
                    //汇总最后一期之前的所有利息之和
                    for(int i=1;i<month2return;i++){
                        BigDecimal temp5 = (BigDecimal.ONE.add(monthlyRate)).pow(i-1);
                        monthlyInterest = (temp1.subtract(monthToReturnMoney)).multiply(temp5).add(monthToReturnMoney).setScale(BidConst.SCALE_CACULATE,RoundingMode.HALF_UP);
                        temp6 = temp6.add(monthlyInterest);
                    }
                    monthlyInterest = totalInterest.subtract(temp6);
                }
            }
        }else if (returnType == BidConst.RETURN_TYPE_MONTH_INTERST) {// 按月到期
            monthlyInterest = DecimalFormatUtil.amountFormat(bidRequestAmount.multiply(monthlyRate));
        }else if(returnType == BidConst.RETURN_TYPE_MONTH_PRINCIPAL){//等额本金
            //每月要还的本金=借款额度/月份n
            //第一个月  =  借款额度*月利率
            //第2个月  =  (借款额度-每月要还的本金)*月利率
            //第3个月  =  (借款额度-每月要还的本金*2)*月利率
            //....
            //第n个月 =   (借款额度-每月要还的本金*(n-1))*月利率

            BigDecimal monthlyPrincipal = bidRequestAmount.divide(BigDecimal.valueOf(month2return),BidConst.SCALE_CACULATE, RoundingMode.HALF_UP);//每月本金
            //第monthIndex个月的利息
            monthlyInterest = bidRequestAmount.subtract(
                    monthlyPrincipal.multiply(BigDecimal.valueOf(monthIndex).subtract(BigDecimal.ONE))
            ).multiply(monthlyRate).setScale(BidConst.SCALE_CACULATE, RoundingMode.HALF_UP);
        }
        return monthlyInterest;
    }

    /**
     * 计算每期还款
     *
     * @param returnType
     *            还款类型
     * @param bidRequestAmount
     *            借款金额
     * @param yearRate
     *            年利率
     * @param monthIndex
     *            第几期
     * @param monthes2Return
     *            还款期限
     * @return
     */
    public static BigDecimal calMonthToReturnMoney(int returnType, BigDecimal bidRequestAmount, BigDecimal yearRate, int monthIndex, int monthes2Return) {
        BigDecimal monthToReturnMoney = BigDecimal.ZERO;
        BigDecimal monthlyRate = getMonthlyRate(yearRate);
        if (returnType == BidConst.RETURN_TYPE_MONTH_INTERST_PRINCIPAL) {// 按月分期（等额本息）
            if (monthes2Return == 1) {
                monthToReturnMoney = bidRequestAmount.add(bidRequestAmount.multiply(monthlyRate)).setScale(BidConst.SCALE_CACULATE, RoundingMode.HALF_UP);
            } else {
                BigDecimal temp1 = bidRequestAmount.multiply(monthlyRate);
                BigDecimal temp2 = (BigDecimal.ONE.add(monthlyRate)).pow(monthes2Return);
                BigDecimal temp3 = (BigDecimal.ONE.add(monthlyRate)).pow(monthes2Return).subtract(BigDecimal.ONE);
                // 算出每月还款
                monthToReturnMoney = temp1.multiply(temp2).divide(temp3, BidConst.SCALE_CACULATE, RoundingMode.HALF_UP);
            }
        } else if (returnType == BidConst.RETURN_TYPE_MONTH_INTERST) {// 按月到期
            BigDecimal monthlyInterest = bidRequestAmount.multiply(monthlyRate).setScale(BidConst.SCALE_CACULATE, RoundingMode.HALF_UP);
            if (monthIndex == monthes2Return) {
                monthToReturnMoney = bidRequestAmount.add(monthlyInterest).setScale(BidConst.SCALE_CACULATE, RoundingMode.HALF_UP);
            } else if (monthIndex < monthes2Return) {
                monthToReturnMoney = monthlyInterest;
            }
        }else if(returnType == BidConst.RETURN_TYPE_MONTH_PRINCIPAL){//等额本金
            //每月要还的本金=借款额度/月份n

            //第一个月  =  借款额度*月利率
            //第2个月  =  (借款额度-每月要还的本金)*月利率
            //第3个月  =  (借款额度-每月要还的本金*2)*月利率
            //....
            //第n个月 =   (借款额度-每月要还的本金*(n-1))*月利率

            BigDecimal monthlyPrincipal = bidRequestAmount.divide(BigDecimal.valueOf(monthes2Return),BidConst.SCALE_CACULATE, RoundingMode.HALF_UP);//每月本金
            //第monthIndex个月的利息
            BigDecimal monthlyInterest = bidRequestAmount.subtract(
                    monthlyPrincipal.multiply(BigDecimal.valueOf(monthIndex).subtract(BigDecimal.ONE))
            ).multiply(monthlyRate).setScale(BidConst.SCALE_CACULATE, RoundingMode.HALF_UP);
            monthToReturnMoney = monthlyPrincipal.add(monthlyInterest);

        }
        return DecimalFormatUtil.formatBigDecimal(monthToReturnMoney, BidConst.SCALE_STORE);
    }

    /**
     * 计算一次投标实际获得的利息=投标金额/借款金额 *总利息
     *
     * @param bidRequestAmount
     *            借款金额
     * @param monthes2Return
     *            还款期数
     * @param yearRate
     *            年利率
     * @param returnType
     *            还款类型
     * @param acturalBidAmount
     *            投标金额
     * @return
     */
    public static BigDecimal calBidInterest(BigDecimal bidRequestAmount, int monthes2Return, BigDecimal yearRate, int returnType, BigDecimal acturalBidAmount) {
        // 借款产生的总利息
        BigDecimal totalInterest = caculateTotalInterest(bidRequestAmount, yearRate, monthes2Return,returnType);
        // 所占比例
        BigDecimal proportion = acturalBidAmount.divide(bidRequestAmount, BidConst.SCALE_CACULATE, RoundingMode.HALF_UP);


        BigDecimal bidInterest = totalInterest.multiply(proportion);
        return DecimalFormatUtil.formatBigDecimal(bidInterest, BidConst.SCALE_STORE);
    }

    /**
     * 计算充值手续费
     *
     * @param amount
     * @return
     */
	/*public static BigDecimal calRechargeFee(BigDecimal amount) {
		return DecimalFormatUtil.formatBigDecimal(amount.multiply(BidConst.RECHARGE_FEE_RATE), BidConst.STORE_SCALE);
	}*/

    /**
     * 计算利息管理费
     *
     * @param interest
     *            利息
     * @return
     */

    public static BigDecimal calInterestManagerCharge(BigDecimal interest) {
        return DecimalFormatUtil.formatBigDecimal(
                interest.multiply(INTEREST_MANAGER_CHARGE_RATE),
                BidConst.SCALE_STORE);
    }

    /**
     * 计算借款管理费
     *
     * @param bidRequestAmount
     *            借款金额
     * @return
     */
    public static BigDecimal calAccountManagementCharge(
            BigDecimal bidRequestAmount) {
        BigDecimal accountManagementCharge = DecimalFormatUtil
                .formatBigDecimal(
                        bidRequestAmount.multiply(ACCOUNT_MANAGER_CHARGE_RATE),
                        BidConst.SCALE_CACULATE);
        return accountManagementCharge;
    }
}
