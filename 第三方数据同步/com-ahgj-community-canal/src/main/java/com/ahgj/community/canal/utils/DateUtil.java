package com.ahgj.community.canal.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 通用处理日期的公共类
 * @author Hohn
 */
public class DateUtil {


    // 系统默认日期格式
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    // 系统默认日起时间格式
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // 时分秒
    public static final String DATE_HMS_FORMAT = "HH:mm:ss";
    //UTC日期格式
    public static final String DATE_UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
    private static final SimpleDateFormat utcData = new SimpleDateFormat(DATE_UTC_FORMAT);
    // 8位日期格式
    public static final String DATE_FORMAT_8 = "yyyyMMdd";

    // 14为日期时间格式
    public static final String DATE_TIME_FORMAT_14 = "yyyyMMddHHmmss";

    public final static String YEAR = "year";

    public final static String MONTH = " month ";

    public final static String DAY = " day ";

    public final static String WEEK = " week ";

    public final static String HOUR = " hour ";

    public final static String MINUTE = " minute ";

    public final static String SECOND = " second ";


    /**
     * 判断参数是否等于null或者空
     *
     * @param para
     * @return boolean
     */
    private static boolean checkPara(Object para) {
        if (null == para || "".equals(para)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得系统的当前时间，毫秒.
     *
     * @return long
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }


    /**
     * 获得系统的当前时间
     *
     * @return e.g.Thu Oct 12 10:25:14 CST 2006
     */
    public static Date getCurrentDate() {
        // return new Date(System.currentTimeMillis());
        return new Date(getCurrentTimeMillis());
    }


    /**
     * 获取系统当前时间的 时分秒
     *
     * @return
     */
    public static synchronized String getCurrentTime4Hms() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_HMS_FORMAT);
        return dateFormat.format(new Date());
    }

    /**
     * 按标准格式格式化时间到 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static synchronized String formatDate(Date date) {
        return dateFormat.format(date);
    }

    /**
     * 按标准格式格式化时间到 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static synchronized String formatDateToUtc(Date date) {
        return utcData.format(date);
    }

    /**
     * 获取想要的时间格式
     *
     * @param dateStr    时间
     * @param rawFormat  原格式
     * @param wantFormat 想转换的格式
     * @return
     * @author YFB
     */
    public static String getWantDate(String dateStr, String rawFormat, String wantFormat) {
        if (!"".equals(dateStr) && dateStr != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(wantFormat);
            try {
                SimpleDateFormat sdfStr = new SimpleDateFormat(rawFormat);
                Date date = sdfStr.parse(dateStr);
                dateStr = sdf.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dateStr;
    }

    /**
     * 按标准格式格式化时间到 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static synchronized String formatDateTime(Date date) {
        return dateTimeFormat.format(date);
    }


    /**
     * 获得系统当前日期，以默认格式显示
     *
     * @return e.g.2006-10-12
     */
    public static synchronized String getCurrentFormatDate() {
        Date currentDate = getCurrentDate();
        return dateFormat.format(currentDate);
    }

    /**
     * 获得昨天的日期，格式：yyyy-MM-dd
     *
     * @return
     */
    public static synchronized String getYestodayFromDateStr() {
        Date yesterday = minusDays(new Date(), 1);
        return dateFormat.format(yesterday);
    }

    /**
     * 获得系统当前日期时间，以默认格式显示
     *
     * @return e.g.2006-10-12 10:55:06
     */
    public static synchronized String getCurrentFormatDateTime() {
        Date currentDate = getCurrentDate();
        return dateTimeFormat.format(currentDate);
    }

    public static Date getNextDate(Date now, int days) {
        return addDays(now, days);
    }

    public static Date getNextDate(String nowDate, int days) {
        Date now = parseStrToDate(nowDate);
        return getNextDate(now, days);
    }

    public static synchronized String getNextDateString(String nowDate, int days) {
        Date nextDate = getNextDate(nowDate, days);
        return formatDate(nextDate);
    }

    /**
     * 获得系统当前日期时间，按照指定格式返回
     *
     * @param pattern e.g.DATE_FORMAT_8 = "yyyyMMdd";
     *                DATE_TIME_FORMAT_14 = "yyyyMMddHHmmss";
     *                或者介于二者之间的格式,e.g."yyyyMMddHH"
     * @return e.g.200610121115
     */
    public static synchronized String getCurrentCustomFormatDateTime(String pattern) {
        if (checkPara(pattern)) {
            return "";
        }
        Date currentDate = getCurrentDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        return dateFormat.format(currentDate);
    }

    /**
     * 输入日期，按照指定格式返回
     *
     * @param date
     * @param pattern e.g.DATE_FORMAT_8 = "yyyyMMdd";
     *                DATE_TIME_FORMAT_14 = "yyyyMMddHHmmss";
     *                或者类似于二者的格式,e.g."yyyyMMddHH"，"yyyyMM"
     * @return String
     */
    public static synchronized String formatDate(Date date, String pattern) {
        if (checkPara(pattern) || checkPara(date)) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        return dateFormat.format(date);
    }


    /**
     * 输入日期，按照指定格式返回
     *
     * @param date
     * @param pattern e.g.DATE_FORMAT_8 = "yyyyMMdd";
     *                DATE_TIME_FORMAT_14 = "yyyyMMddHHmmss";
     *                或者类似于二者的格式,e.g."yyyyMMddHH"，"yyyyMM"
     * @param locale  国家地区，
     *                e.g.new Locale("zh","CN","") 中国
     *                Locale.getDefault();
     * @return string
     */
    public static synchronized String formatDate(Date date, String pattern, Locale locale) {
        if (checkPara(pattern) || checkPara(date)) {
            return "";
        }
        if (checkPara(locale)) {
            locale = Locale.getDefault();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, locale);
        String customFormatDate = dateFormat.format(date);

        return customFormatDate;
    }


    /**
     * 将时间字符串按照默认格式DATE_FORMAT = "yyyy-MM-dd"，转换为Date
     *
     * @param dateStr
     * @return Date
     */
    public static synchronized Date parseStrToDate(String dateStr) {
        if (checkPara(dateStr)) {
            return null;
        }
        Date resDate = dateFormat.parse(dateStr, new ParsePosition(0));
        return resDate;
    }


    /**
     * 将时间字符串按照默认格式DATE_TIME_FORMAT ="yyyy-MM-dd HH:mm:ss",转换为Date
     *
     * @param dateStr
     * @return Date
     */
    public static synchronized Date parseStrToDateTime(String dateStr) {
        if (checkPara(dateStr)) {
            return null;
        }
        Date resDate = dateTimeFormat.parse(dateStr, new ParsePosition(0));
        return resDate;
    }


    /**
     * 将时间字符串按照默认格式DATE_FORMAT = "yyyy-MM-dd"，转换为Calender
     *
     * @param dateStr
     * @return Calendar
     */
    public static synchronized Calendar parseStrToCalendar(String dateStr) {
        if (checkPara(dateStr)) {
            return null;
        }
        Date resDate = dateTimeFormat.parse(dateStr, new ParsePosition(0));

        Locale locale = Locale.getDefault();
        Calendar cal = new GregorianCalendar(locale);
        cal.setTime(resDate);

        return cal;
    }


    /**
     * 将日期字符串转换成日期时间字符串
     *
     * @param dateStr
     * @return string
     */
    public static synchronized String parseDateStrToDateTimeStr(String dateStr) {
        if (checkPara(dateStr)) {
            return "";
        }
        Date resDate = dateFormat.parse(dateStr, new ParsePosition(0));
        return formatDate(resDate, DATE_TIME_FORMAT);
    }


    /**
     * 将时间或者时间日期字符串按照指定格式转换为Date
     *
     * @param dateStr
     * @param pattern
     * @return Date
     */
    public static Date parseStrToCustomPatternDate(String dateStr, String pattern) {
        if (checkPara(pattern) || checkPara(dateStr)) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                pattern);
        Date resDate = dateFormat.parse(dateStr, new ParsePosition(0));

        return resDate;
    }


    /**
     * 将时间字符串从一种格式转换成另一种格式.
     *
     * @param dateStr     e.g. String dateStr = "2006-10-12 16:23:06";
     * @param patternFrom e.g. DatePattern.DATE_TIME_FORMAT
     * @param patternTo   e.g. DatePattern.DATE_TIME_FORMAT_14
     * @return string
     */
    public static String convertDatePattern(String dateStr,
                                            String patternFrom, String patternTo) {
        if (checkPara(patternFrom) || checkPara(patternTo) || checkPara(dateStr)) {
            return "";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(patternFrom);
        Date resDate = dateFormat.parse(dateStr, new ParsePosition(0));
        return formatDate(resDate, patternTo);
    }

    /**
     * 日期天数增加
     *
     * @param date
     * @param days
     * @return Date
     */
    public static Date addDays(Date date, int days) {
        if (checkPara(date)) {
            return null;
        }
        if (0 == days) {
            return date;
        }
        Locale loc = Locale.getDefault();
        Calendar cal = new GregorianCalendar(loc);
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);

        return cal.getTime();
    }

    /**
     * 日期天数减少
     *
     * @param date
     * @param days
     * @return Date
     */
    public static Date minusDays(Date date, int days) {
        return addDays(date, (0 - days));
    }

    /**
     * 按时间格式相加
     *
     * @param dateStr 原来的时间
     * @param pattern 时间格式
     * @param type    "year"年、"month"月、"day"日、"week"周、
     *                "hour"时、"minute"分、"second"秒
     *                通过常量来设置,e.g.DateFormatUtils.YEAR
     * @param count   相加数量
     * @return Date
     */
    public static String addDate(String dateStr, String pattern,
                                 String type, int count) {
        if (checkPara(dateStr) || checkPara(pattern) || checkPara(type)) {
            return "";
        }
        if (0 == count) {
            return dateStr;
        }
        Date date = parseStrToCustomPatternDate(dateStr, pattern);
        Locale loc = Locale.getDefault();
        Calendar cal = new GregorianCalendar(loc);
        cal.setTime(date);

        if (YEAR.equals(type)) {
            cal.add(Calendar.YEAR, count);
        } else if (MONTH.equals(type)) {
            cal.add(Calendar.MONTH, count);
        } else if (DAY.equals(type)) {
            cal.add(Calendar.DAY_OF_MONTH, count);
        } else if (WEEK.equals(type)) {
            cal.add(Calendar.WEEK_OF_MONTH, count);
        } else if (HOUR.equals(type)) {
            cal.add(Calendar.HOUR, count);
        } else if (MINUTE.equals(type)) {
            cal.add(Calendar.MINUTE, count);
        } else if (SECOND.equals(type)) {
            cal.add(Calendar.SECOND, count);
        } else {
            return "";
        }

        return formatDate(cal.getTime(), pattern);
    }


    /**
     * 日期大小比较
     *
     * @param dateStr1
     * @param dateStr2
     * @param pattern
     * @return int
     */
    public static int compareDate(String dateStr1, String dateStr2, String pattern) {
        if (checkPara(dateStr1) || checkPara(dateStr2) || checkPara(pattern)) {
            return 888;
        }
        Date date1 = parseStrToCustomPatternDate(dateStr1, pattern);
        Date date2 = parseStrToCustomPatternDate(dateStr2, pattern);

        return date1.compareTo(date2);
    }

    /**
     * 获得这个月的第一天
     *
     * @param dateStr
     * @return string
     */
    public static String getFirstDayInMonth(String dateStr) {
        if (checkPara(dateStr)) {
            return "";
        }
        Calendar cal = parseStrToCalendar(dateStr);
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, firstDay);

        return formatDate(cal.getTime(), DATE_FORMAT);
    }

    /**
     * 获得这个月的最后一天
     *
     * @param dateStr
     * @return string
     */
    public static String getLastDayInMonth(String dateStr) {
        if (checkPara(dateStr)) {
            return "";
        }
        Calendar cal = parseStrToCalendar(dateStr);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, lastDay);

        return formatDate(cal.getTime(), DATE_FORMAT);
    }

    /**
     * 获得这周的第一天
     *
     * @param dateStr
     * @return string
     */
    public static String getFirstDayInWeek(String dateStr) {
        if (checkPara(dateStr)) {
            return "";
        }
        Calendar cal = parseStrToCalendar(dateStr);
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_WEEK);
        cal.set(Calendar.DAY_OF_WEEK, firstDay);

        return formatDate(cal.getTime(), DATE_FORMAT);
    }


    /**
     * 获得这周的最后一天
     *
     * @param dateStr
     * @return string
     */
    public static String getLastDayInWeek(String dateStr) {
        if (checkPara(dateStr)) {
            return "";
        }
        Calendar cal = parseStrToCalendar(dateStr);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_WEEK);
        cal.set(Calendar.DAY_OF_WEEK, lastDay);

        return formatDate(cal.getTime(), DATE_FORMAT);
    }

    /**
     * 解析一个日期之间的所有月份
     *
     * @param beginDateStr
     * @param endDateStr
     * @return
     */
    public static ArrayList getMonthList(String beginDateStr, String endDateStr) {
        // 指定要解析的时间格式
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
        // 返回的月份列表
        String sRet = "";

        // 定义一些变量
        Date beginDate = null;
        Date endDate = null;

        GregorianCalendar beginGC = null;
        GregorianCalendar endGC = null;
        ArrayList list = new ArrayList();

        try {
            // 将字符串parse成日期
            beginDate = f.parse(beginDateStr);
            endDate = f.parse(endDateStr);

            // 设置日历
            beginGC = new GregorianCalendar();
            beginGC.setTime(beginDate);

            endGC = new GregorianCalendar();
            endGC.setTime(endDate);

            // 直到两个时间相同
            while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {
                sRet = beginGC.get(Calendar.YEAR) + "-"
                        + (beginGC.get(Calendar.MONTH) + 1);
                list.add(sRet);
                // 以月为单位，增加时间
                beginGC.add(Calendar.MONTH, 1);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析一个日期段之间的所有日期
     *
     * @param beginDateStr 开始日期
     * @param endDateStr   结束日期
     * @return
     */
    public static ArrayList getDayList(String beginDateStr, String endDateStr) {
        // 指定要解析的时间格式
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

        // 定义一些变量
        Date beginDate = null;
        Date endDate = null;

        Calendar beginGC = null;
        Calendar endGC = null;
        ArrayList list = new ArrayList();

        try {
            // 将字符串parse成日期
            beginDate = f.parse(beginDateStr);
            endDate = f.parse(endDateStr);

            // 设置日历
            beginGC = Calendar.getInstance();
            beginGC.setTime(beginDate);

            endGC = Calendar.getInstance();
            endGC.setTime(endDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // 直到两个时间相同
            while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {

                list.add(sdf.format(beginGC.getTime()));
                // 以日为单位，增加时间
                beginGC.add(Calendar.DAY_OF_MONTH, 1);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList getYearList() {
        ArrayList list = new ArrayList();
        Calendar c = null;
        c = Calendar.getInstance();
        c.setTime(new Date());
        int currYear = Calendar.getInstance().get(Calendar.YEAR);

        int startYear = currYear - 5;
        int endYear = currYear + 10;
        for (int i = startYear; i < endYear; i++) {
            list.add(new Integer(i));
        }
        return list;
    }

    public static int getCurrYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 得到某一年周的总数
     *
     * @param year
     * @return
     */
    public static LinkedHashMap getWeekList(int year) {
        LinkedHashMap map = new LinkedHashMap();
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        int count = getWeekOfYear(c.getTime());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dayOfWeekStart = "";
        String dayOfWeekEnd = "";
        for (int i = 1; i <= count; i++) {
            dayOfWeekStart = sdf.format(getFirstDayOfWeek(year, i));
            dayOfWeekEnd = sdf.format(getLastDayOfWeek(year, i));
            map.put(new Integer(i), "第" + i + "周(从" + dayOfWeekStart + "至" + dayOfWeekEnd + ")");
        }
        return map;

    }

    /**
     * 得到一年的总周数
     *
     * @param year
     * @return
     */
    public static int getWeekCountInYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        int count = getWeekOfYear(c.getTime());
        return count;
    }

    /**
     * 取得当前日期是多少周
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);

        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 得到某年某周的第一天
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getFirstDayOfWeek(cal.getTime());
    }

    /**
     * 得到某年某周的最后一天
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getLastDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getLastDayOfWeek(cal.getTime());
    }

    /**
     * 得到某年某月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getFirestDayOfMonth(int year, int month) {
        month = month - 1;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);

        int day = c.getActualMinimum(c.DAY_OF_MONTH);

        c.set(Calendar.DAY_OF_MONTH, day);
        return c.getTime();

    }

    /**
     * 提到某年某月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(int year, int month) {
        month = month - 1;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        int day = c.getActualMaximum(c.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c.getTime();
    }

    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTime();
    }


    public static String getWeekOfDate(Date date) {
        String[] weekDays = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 判断一个日期是否大于dateStr1 小于dateStr2 dateStr3 是需要比较的值 默认使用 yyyy-MM-dd HH:mm:ss的格式
     *
     * @param dateStr1
     * @param dateStr2
     * @param dateStr3
     * @return
     */
    public static boolean compareStringDate(String dateStr1, String dateStr2, String dateStr3) {
        if (dateStr3.compareTo(dateStr1) > 0 && dateStr3.compareTo(dateStr2) <= 0) {
            return true;
        }
        return false;

    }

    /**
     * 当前时间减去传入的时间
     *
     * @param time
     * @return
     */
    public static long currentTimeLoseTime(String time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(DateUtil.getCurrentFormatDateTime());
            Date d2 = df.parse(time);
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            long minute = diff / (1000 * 60);
            return minute;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 当前时间减去传入的时间
     *
     * @param endTime
     * @param startTime
     * @return 分钟
     */
    public static double getLoseTimeMinute(String endTime, String startTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(endTime);
            Date d2 = df.parse(startTime);
            double diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            double minute = diff / (1000 * 60);
            BigDecimal b = new BigDecimal(minute);
            double minuteFormat = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            return minuteFormat;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 当前时间减去传入的时间
     *
     * @param endTime
     * @param startTime
     * @return 分钟
     */
    public static long getLoseTimeSecond(String endTime, String startTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(endTime);
            Date d2 = df.parse(startTime);
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            long minute = diff / (1000);

            return minute;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


/*   public static void main(String[] args) {
//       String str=getWeekOfDate(new Date());
//        System.out.println(str);
//        String dates = "2007-05-24 12:22:22";
//        System.out.println("convertDatePattern(dates,DATE_TIME_FORMAT,yyyy:MM;dd) = " + convertDatePattern(dates, DATE_TIME_FORMAT, "yyyy年MM月dd日"));
//        java.text.DateFormat dft1=new SimpleDateFormat(DATE_TIME_FORMAT);
//        java.text.DateFormat dft=new SimpleDateFormat("yyyy年MM月dd日");
//        Date date=null;
//        try {
//            date=dft1.parse(dates);
//        } catch (ParseException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//
//        System.out.println("dft.parse(new Date()) = " + dft.format(date));
//        System.out.println(DateUtil.getCurrentFormatDateTime());
//        boolean size=compareDate3("2017-11-08 10:00:00","2017-11-08 19:23:00","2017-11-08 9:00:00");
//        System.out.println("====================="+size);


       //String currentTime4Hms = DateUtil.getCurrentTime4Hms();
       // System.out.println("====================="+currentTime4Hms);
   }*/
}
