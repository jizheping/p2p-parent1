package com.jizheping.api.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 获取当前天的最后一分钟一秒中(23:59:59)
     * @param date    需要进行格式化
     * @return    返回格式化后的时间对象
     */
    public static Date endOfDate(Date date){
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(Calendar.DATE,1);
        calendar.add(Calendar.SECOND,-1);

        return calendar.getTime();
    }


}
