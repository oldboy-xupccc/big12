package com.chuangdata.userprofile.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author luxiaofeng
 */
public class TimeConvertor {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat SDF_DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Convert time from String to Long
     *
     * @param strTime must be "yyyy-MM-dd HH:mm:ss"
     * @return long time, ms
     * @throws ParseException
     */
    public static long convertTime(String strTime) throws ParseException {
        Date date = SDF.parse(strTime);
        return date.getTime();
    }


    public static long convert2DayTime(String strTime) throws ParseException {
        Date date = SDF.parse(strTime);
        return date.getTime();
    }

    public static long convertTime(String strTime, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = simpleDateFormat.parse(strTime);
        return date.getTime();
    }
}
