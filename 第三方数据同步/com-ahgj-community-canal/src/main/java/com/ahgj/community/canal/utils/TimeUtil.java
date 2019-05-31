package com.ahgj.community.canal.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 获取 当前年、半年、季度、月、日、小时 开始结束时间
 * 返回的数据是data类型
 * DateUtil.formatDateToUtc 转换utc格式
 */
public class TimeUtil {
    /**
     * 获取 当前年、半年、季度、月、日、小时 开始结束时间
     */
    private final static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat longHourSdf = new SimpleDateFormat("yyyy-MM-dd HH");
    private final static SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat _shortSdf = new SimpleDateFormat("yyyyMMdd");


    /**
     * 获得指定日期的前一天
     * @param specifiedDay yy-MM-dd
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayBefore(String specifiedDay){
//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        Date date=null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day-1);

        String dayBefore=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayBefore;
    }

    /**
     * 获得本周的第一天，周一
     *
     * @return
     */
    public static Date getCurrentWeekDayStartTime() {
        Calendar c = Calendar.getInstance();
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK) - 2;
            c.add(Calendar.DATE, -weekday);
            c.setTime(_shortSdf.parse(_shortSdf.format(c.getTime())));
            //c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获得目标日期的，周日
     *
     * @return
     */
    public Date getCurrentWeekDayEndTime(String targetDate) {
        Date from = DateUtil.parseStrToCustomPatternDate(targetDate, DateUtil.DATE_FORMAT);
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        try {
            int weekday = cal.get(Calendar.DAY_OF_WEEK);
            cal.add(Calendar.DATE, 8 - weekday);
            cal.setTime(_shortSdf.parse(_shortSdf.format(cal.getTime())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal.getTime();
    }

    /**
     * 获得本周的最后一天，周日
     *
     * @return
     */
    public static Date getCurrentWeekDayEndTime() {
        Calendar c = Calendar.getInstance();
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK);
            c.add(Calendar.DATE, 8 - weekday);
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获得本天的开始时间
     *
     * @return
     */
    public static Date getCurrentDayStartTime() {
        Date now = new Date();
        try {
            now = shortSdf.parse(shortSdf.format(now));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获得本天的结束时间
     *
     * @return
     */
    public static Date getCurrentDayEndTime() {
        Date now = new Date();
        try {
            now = longSdf.parse(shortSdf.format(now) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获得本小时的开始时间
     *
     * @return
     */
    public static Date getCurrentHourStartTime() {
        Date now = new Date();
        try {
            now = longHourSdf.parse(longHourSdf.format(now));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获得本小时的结束时间
     *
     * @return
     */
    public static Date getCurrentHourEndTime() {
        Date now = new Date();
        try {
            now = longSdf.parse(longHourSdf.format(now) + ":59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获得本月的开始时间
     *
     * @return
     */
    public static Date getCurrentMonthStartTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
            //now = longSdf.parse(shortSdf.format(cal.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获得目标日期的开始时间
     *
     * @param targetDate yyyymm 年月
     * @return
     */
    public static Date getTargetMonthStartTime(String targetDate) {
        Date from = DateUtil.parseStrToCustomPatternDate(targetDate + "01", DateUtil.DATE_FORMAT_8);
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        Date now = null;
        try {
            cal.set(Calendar.DATE, 1);
            now = shortSdf.parse(shortSdf.format(cal.getTime()));
            //now = longSdf.parse(shortSdf.format(cal.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }


    /**
     * 本月的结束时间
     *
     * @return
     */
    public static Date getCurrentMonthEndTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        now = getMonthStartDate(c, now);
        return now;
    }

    /**
     * 目标日期月份的结束时间
     *
     * @param targetDate yyyymm 年月
     * @return
     */
    public static Date getTargetMonthEndTime(String targetDate) {
        Date from = DateUtil.parseStrToCustomPatternDate(targetDate + "01", DateUtil.DATE_FORMAT_8);
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        Date now = null;
        now = getMonthStartDate(cal, now);
        return now;
    }

    private static Date getMonthStartDate(Calendar cal, Date now) {
        try {
            cal.set(Calendar.DATE, 1);
            cal.add(Calendar.MONTH, 1);
            cal.add(Calendar.DATE, -1);
            now = longSdf.parse(shortSdf.format(cal.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }


    /**
     * 当前年的开始时间
     *
     * @return
     */
    public static Date getCurrentYearStartTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        now = getYearStartDate(c, now);
        return now;
    }

    private static Date getYearStartDate(Calendar c, Date now) {
        now = getYearCalendarDate(c, now);
        return now;
    }

    private static Date getYearCalendarDate(Calendar c, Date now) {
        try {
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DATE, 1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
            //now = shortSdf.parse(shortSdf.format(c.getTime())+ " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }


    /**
     * 目标年的开始时间
     *
     * @param targetDate yyyy
     * @return
     */
    public static Date getTargetYearStartTime(String targetDate) {
        Date from = DateUtil.parseStrToCustomPatternDate(targetDate + "1201", DateUtil.DATE_FORMAT_8);
        Calendar c = Calendar.getInstance();
        c.setTime(from);
        Date now = null;
        now = getTargetYearDate(c, now);
        return now;
    }

    private static Date getTargetYearDate(Calendar c, Date now) {
        now = getTargetCalenderDate(c, now);
        return now;
    }

    private static Date getTargetCalenderDate(Calendar c, Date now) {
        try {
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DATE, 1);
            now = _shortSdf.parse(_shortSdf.format(c.getTime()));
            //now = shortSdf.parse(shortSdf.format(c.getTime())+ " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }


    /**
     * 当前年的结束时间
     *
     * @return
     */
    public static Date getCurrentYearEndTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        now = getYearEndDate(c, now);
        return now;
    }

    private static Date getYearEndDate(Calendar c, Date now) {
        try {
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 31);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }


    /**
     * 目标年的结束时间
     *
     * @param targetDate 年份 yyyy
     * @return
     */
    public static Date getTargetYearEndTime(String targetDate) {
        Date from = DateUtil.parseStrToCustomPatternDate(targetDate + "1201", DateUtil.DATE_FORMAT_8);
        Calendar c = Calendar.getInstance();
        c.setTime(from);
        Date now = null;
        now = getYearEndDate(c, now);
        return now;
    }

    /**
     * 当前季度的开始时间
     *
     * @return
     */
    public static Date getCurrentQuarterStartTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        now = getQuarterStartDate(c, currentMonth, now);
        return now;
    }

    /**
     * 目标日期的季度的开始时间
     *
     * @param targetDate yyyymm 1季度 1-3之间的月份; 2季度4-6之间的月份; 3季度 7-9之间的月份; 4季度 10-12之间的月份;
     * @return
     */
    public static Date getTargetQuarterStartTime(String targetDate) {
        Date from = DateUtil.parseStrToCustomPatternDate(targetDate + "01", DateUtil.DATE_FORMAT_8);
        Calendar c = Calendar.getInstance();
        c.setTime(from);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        now = getQuarterStartDate(c, currentMonth, now);
        return now;
    }

    private static Date getQuarterStartDate(Calendar c, int currentMonth, Date now) {
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = _shortSdf.parse(_shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 目标日期季度的结束时间
     *
     * @param targetDate yyyymm 1季度 1-3之间的月份; 2季度4-6之间的月份; 3季度 7-9之间的月份; 4季度 10-12之间的月份;
     * @return
     */
    public static Date getTargetQuarterEndTime(String targetDate) {
        Date from = DateUtil.parseStrToCustomPatternDate(targetDate + "01", DateUtil.DATE_FORMAT_8);
        Calendar c = Calendar.getInstance();
        c.setTime(from);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        now = getQuarterEndDate(c, currentMonth, now);
        return now;
    }

    /**
     * 当前季度的结束时间
     *
     * @return
     */
    public static Date getCurrentQuarterEndTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        now = getQuarterEndDate(c, currentMonth, now);
        return now;
    }

    private static Date getQuarterEndDate(Calendar c, int currentMonth, Date now) {
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获取前/后半年的开始时间
     *
     * @return
     */
    public static Date getHalfYearStartTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        now = getStarDate(c, currentMonth, now);
        return now;

    }

    /**
     * 获取前/后半年的开始时间
     *
     * @param targetDate yyyymm  01-06之间上半年 07-12之间下半年
     * @return
     */
    public static Date getHalfYearStartTime(String targetDate) {
        Date from = DateUtil.parseStrToCustomPatternDate(targetDate + "01", DateUtil.DATE_FORMAT_8);
        Calendar c = Calendar.getInstance();
        c.setTime(from);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        now = getStarDate(c, currentMonth, now);
        return now;

    }

    private static Date getStarDate(Calendar c, int currentMonth, Date now) {
        try {
            if (currentMonth >= 1 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 0);
            } else if (currentMonth >= 7 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 6);
            }
            c.set(Calendar.DATE, 1);
            now = _shortSdf.parse(_shortSdf.format(c.getTime()));
            //now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }


    /**
     * 获取目标日期的 前/后半年的结束时间
     *
     * @param targetDate yyyymm  01-06之间上半年 07-12之间下半年
     * @return
     */
    public static Date getHalfYearEndTime(String targetDate) {
        Date from = DateUtil.parseStrToCustomPatternDate(targetDate + "01", DateUtil.DATE_FORMAT_8);
        Calendar c = Calendar.getInstance();
        c.setTime(from);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        now = getEndDate(c, currentMonth, now);
        return now;
    }

    private static Date getEndDate(Calendar c, int currentMonth, Date now) {
        try {
            if (currentMonth >= 1 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }


    /**
     * 获取前/后半年的结束时间
     *
     * @return
     */
    public static Date getHalfYearEndTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        now = getEndDate(c, currentMonth, now);
        return now;
    }

   /* public static void main(String[] args) {
        System.out.println("当前小时开始：" + getCurrentHourStartTime().toString());
        System.out.println("当前小时结束：" + getCurrentHourEndTime().toString());
        System.out.println("当前天开始：" + getCurrentDayStartTime().toString());
        System.out.println("当前天时结束：" + getCurrentDayEndTime().toString());

        System.out.println("当前天开始：" + getCurrentDayStartTime().toString());
        System.out.println("当前天时结束：" + getCurrentDayEndTime().toString());

        System.out.println("当前周开始：" + getCurrentWeekDayStartTime().toString());
        System.out.println("当前周结束：" + getCurrentWeekDayEndTime().toString());
        //System.out.println("当前月开始：" + getCurrentMonthStartTime().toString());
        //System.out.println("当前月结束：" + getCurrentMonthEndTime().toString());
        System.out.println("2019年02月开始时间：" + DateUtil.formatDateToUtc(getTargetMonthStartTime("201902")));
        System.out.println("2019年02月结束时间：" + DateUtil.formatDateToUtc(getTargetMonthEndTime("201902")));
        //System.out.println("当前季度开始：" + getCurrentQuarterStartTime().toString());
        //System.out.println("当前季度结束：" + getCurrentQuarterEndTime().toString());
        System.out.println("2019年1季度开始：" + DateUtil.formatDateToUtc(getTargetQuarterStartTime("201903")));
        System.out.println("2019年1季度结束：" + DateUtil.formatDateToUtc(getTargetQuarterEndTime("201903")));


        // System.out.println("当前半年/后半年开始：" + getHalfYearStartTime().toString());
        //System.out.println("当前半年/后半年结束：" + getHalfYearEndTime().toString());
        System.out.println("2019前半年/后半年开始：" + DateUtil.formatDateToUtc(getHalfYearStartTime("201906")));
        System.out.println("2019前半年/后半年结束：" + DateUtil.formatDateToUtc(getHalfYearEndTime("201906")));

        System.out.println("2019年开始：" + DateUtil.formatDateToUtc(getTargetYearStartTime("2019")));
        System.out.println("2019年结束：" + DateUtil.formatDateToUtc(getTargetYearEndTime("2019")));


    }*/


}
