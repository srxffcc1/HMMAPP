package com.healthy.library.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Author: Li
 * Date: 2018/8/21 0021
 * Description:
 */
public class DateUtils {
    /**
     * 日期格式：MM-dd HH:mm
     **/
    public static final String DF_MM_DD_HH_MM = "MM-dd HH:mm";
    private static final String DATE_FORMAT_PATTERN_YMD = "yyyy-MM-dd";
    private static final String DATE_FORMAT_PATTERN_YMD_HM = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_PATTERN_YMD_HMS = "yyyy-MM-dd HH:mm:ss";

    public static String getTimeToString(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date());
    }

    /**
     * @param date       2020-12-30
     * @param orgPattern yyyy-MM-dd
     * @param pattern    yyyy/MM/dd
     * @return
     */
    public static String getTimeFormat(String date, String orgPattern, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        try {
            return format.format(new SimpleDateFormat(orgPattern).parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String formatTime2String(String pattern, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(date);
    }

    public static String formatPattern(String originPattern, String wantedPattern, String time) {
        Date date = formatString2Time(originPattern, time);
        return formatTime2String(wantedPattern, date);
    }

    public static Date formatString2Time(String pattern, String time) {
        try {
            return new SimpleDateFormat(pattern, Locale.CHINA).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 将原有格式 转换格式为 yyyy-MM-dd
     *
     * @param destime
     * @param format
     * @return
     */
    public static String getDateDay(String destime, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(destime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return result;
    }

    /**
     * 将原有格式（format） 自定义转换时间格式（resultFormat）
     *
     * @param destime
     * @param format
     * @param resultFormat
     * @return
     */
    public static String getDateDay(String destime, String format, String resultFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(destime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = new SimpleDateFormat(resultFormat).format(date);
        return result;
    }

    //获得阶段性时间的显示要求
    public static String getClassDateHasHour(String destime) {
        ////System.out.println("新的时间");
        if (TextUtils.isEmpty(destime)) {
            return "";
        }
        String result = "";
        Date nowDate = new Date();
//        ////System.out.println("当前时间"+nowDate);
        Date endDate = null;
        long nd = 1000 * 24 * 60 * 60;//每天毫秒数
        long nh = 1000 * 60 * 60;//每小时毫秒数
        long nm = 1000 * 60;//每分钟毫秒数
        long ns = 1000;//每秒毫秒数
        try {
            endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(destime);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        long diff = nowDate.getTime() - endDate.getTime();
        ////System.out.println(diff);
        if (diff < nm) {//小于一分钟
            result = "今天";
        } else if (nm <= diff && diff < nh) {//大于1分钟到1小时
            result = "今天";
        } else if (nh <= diff && diff < nd) {//大于1小时小于24小时
            if (IsYesterday(destime)) {
                String dd = destime.split(" ")[1];
                result = "昨天";
            } else {
                result = "今天";
            }
        } else if (nd <= diff && diff < 2 * nd) {//大于24小于48小时
            if (IsYesterday(destime)) {
                String dd = destime.split(" ")[1];
                result = "昨天";
            } else {
                String dd = destime.split(" ")[0];
                String dd2 = destime.split(" ")[1];
                result = dd.split("-")[1] + "-" + dd.split("-")[2] + " " + dd2.split(":")[0] + ":" + dd2.split(":")[1];
            }

        } else if (2 * nd <= diff && diff < 365 * nd) {//大于48小时小于1年
            String dd = destime.split(" ")[0];
            String dd2 = destime.split(" ")[1];
            result = dd.split("-")[1] + "-" + dd.split("-")[2] + " " + dd2.split(":")[0] + ":" + dd2.split(":")[1];
        } else {//大于1年
            result = destime.split(" ")[0];
        }
        if ("0 秒前".equals(result)) {
            result = "刚刚";
        }
        return result;
    }

    //获得阶段性时间的显示要求
    public static String getClassDateNoHour(String destime) {
        ////System.out.println("新的时间");
        if (TextUtils.isEmpty(destime)) {
            return "";
        }
        String result = "";
        Date nowDate = new Date();
//        ////System.out.println("当前时间"+nowDate);
        Date endDate = null;
        long nd = 1000 * 24 * 60 * 60;//每天毫秒数
        long nh = 1000 * 60 * 60;//每小时毫秒数
        long nm = 1000 * 60;//每分钟毫秒数
        long ns = 1000;//每秒毫秒数
        try {
            endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(destime);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        long diff = nowDate.getTime() - endDate.getTime();
        ////System.out.println(diff);
        if (diff < nm) {//小于一分钟
            result = "今天";
        } else if (nm <= diff && diff < nh) {//大于1分钟到1小时
            result = "今天";
        } else if (nh <= diff && diff < nd) {//大于1小时小于24小时
            if (IsYesterday(destime)) {
                String dd = destime.split(" ")[1];
                result = "昨天";
            } else {
                result = "今天";
            }
        } else if (nd <= diff && diff < 2 * nd) {//大于24小于48小时
            if (IsYesterday(destime)) {
                String dd = destime.split(" ")[1];
                result = "昨天";
            } else {
                String dd = destime.split(" ")[0];
                String dd2 = destime.split(" ")[1];
                result = dd.split("-")[1] + "/" + dd.split("-")[2];
            }

        } else if (2 * nd <= diff && diff < 365 * nd) {//大于48小时小于1年
            String dd = destime.split(" ")[0];
            String dd2 = destime.split(" ")[1];
            result = dd.split("-")[1] + "/" + dd.split("-")[2];
        } else {//大于1年
            result = destime.split(" ")[0];
        }
        if ("0 秒前".equals(result)) {
            result = "刚刚";
        }
        return result;
    }

    public static String getClassDateTools(String destime) {
        ////System.out.println("新的时间");
        if (TextUtils.isEmpty(destime)) {
            return "";
        }
        String result = "";
        Date nowDate = new Date();
//        ////System.out.println("当前时间"+nowDate);
        Date endDate = null;
        long nd = 1000 * 24 * 60 * 60;//每天毫秒数
        long nh = 1000 * 60 * 60;//每小时毫秒数
        long nm = 1000 * 60;//每分钟毫秒数
        long ns = 1000;//每秒毫秒数
        try {
            endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(destime);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        long diff = nowDate.getTime() - endDate.getTime();
        ////System.out.println(diff);
        if (diff < 0) {
            String dd = destime.split(" ")[0];
            String dd2 = destime.split(" ")[1];
            result = dd2.split(":")[0] + ":" + dd2.split(":")[1];
        } else if (diff < nm && diff >= 0) {//小于一分钟
            String dd = destime.split(" ")[0];
            String dd2 = destime.split(" ")[1];
            result = dd2.split(":")[0] + ":" + dd2.split(":")[1];
        } else if (nm <= diff && diff < nh) {//大于1分钟到1小时
            String dd = destime.split(" ")[0];
            String dd2 = destime.split(" ")[1];
            result = dd2.split(":")[0] + ":" + dd2.split(":")[1];
        } else if (nh <= diff && diff < nd) {//大于1小时小于24小时
            if (IsYesterday(destime)) {
                String dd = destime.split(" ")[0];
                String dd2 = destime.split(" ")[1];
                result = dd2.split(":")[0] + ":" + dd2.split(":")[1];
            } else {
                String dd = destime.split(" ")[0];
                String dd2 = destime.split(" ")[1];
                result = dd2.split(":")[0] + ":" + dd2.split(":")[1];
            }
        } else if (nd <= diff && diff < 2 * nd) {//大于24小于48小时
            if (IsYesterday(destime)) {
                String dd = destime.split(" ")[1];
                result = dd.split(":")[0] + ":" + dd.split(":")[1];
            } else {
                String dd = destime.split(" ")[0];
                String dd2 = destime.split(" ")[1];
                result = dd2.split(":")[0] + ":" + dd2.split(":")[1];
            }
        } else if (2 * nd <= diff && diff < 365 * nd) {//大于48小时小于1年
            String dd = destime.split(" ")[0];
            String dd2 = destime.split(" ")[1];
            result = dd2.split(":")[0] + ":" + dd2.split(":")[1];
        } else {//大于1年
            result = destime.split(" ")[0];
        }
        if ("0 秒前".equals(result)) {
            result = "刚刚";
        }
        return result;
    }

    public static String getAgeWithMonth(Date datedate, Date nowTimeDate) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(datedate);
        String nowTime = new SimpleDateFormat("yyyy-MM-dd").format(nowTimeDate);
        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(nowTime)) {
            return "";
        }
        String[] data = date.split("-");
        String[] nowData = nowTime.split("-");
        if (data.length < 3 || nowData.length < 3) {
            return "";
        }

        Calendar birthday = new GregorianCalendar(Integer.valueOf(data[0]),
                Integer.valueOf(data[1]), Integer.valueOf(data[2]));

        Calendar now = new GregorianCalendar(Integer.valueOf(nowData[0]),
                Integer.valueOf(nowData[1]), Integer.valueOf(nowData[2]));
        int day = now.get(Calendar.DAY_OF_MONTH)
                - birthday.get(Calendar.DAY_OF_MONTH);
        int month = now.get(Calendar.MONTH) - birthday.get(Calendar.MONTH);
        int year = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
        // 按照减法原理，先day相减，不够向month借；然后month相减，不够向year借；最后year相减。
        if (day < 0) {
            month -= 1;
            now.add(Calendar.MONTH, -1);// 得到上一个月，用来得到上个月的天数。
            day = day + now.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        if (month < 0) {
            month = (month + 12) % 12;
            year--;
        }
        ////System.out.println("年龄：" + year + "岁" + month + "月" + day + "天");
        StringBuffer tag = new StringBuffer();
        if (year > 0) {
            tag.append(year + "岁");
        }
        if (month > 0) {
            tag.append(month + "个月");
        }
        if (day > 0) {
            tag.append(day + "天");
        }
        if (year == 0 && month == 0 && day == 0) {
            tag.append("今日出生");
        }
        return String.valueOf(tag);
    }

    public static String getClassDateToolMain(String destime) {
        ////System.out.println("新的时间");
        if (TextUtils.isEmpty(destime)) {
            return "";
        }
        String result = "";
        Date nowDate = new Date();
//        ////System.out.println("当前时间"+nowDate);
        Date endDate = null;
        long nd = 1000 * 24 * 60 * 60;//每天毫秒数
        long nh = 1000 * 60 * 60;//每小时毫秒数
        long nm = 1000 * 60;//每分钟毫秒数
        long ns = 1000;//每秒毫秒数
        try {
            endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(destime);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        long diff = nowDate.getTime() - endDate.getTime();
        ////System.out.println(diff);
        if (diff < 0) {
            String dd = destime.split(" ")[0];
            String dd2 = destime.split(" ")[1];
            result = dd.split("-")[1] + "-" + dd.split("-")[2] + " " + dd2.split(":")[0] + ":" + dd2.split(":")[1];
        } else if (diff < nm && diff >= 0) {//小于一分钟
            String dd = destime.split(" ")[1];
            result = "今天 " + dd.split(":")[0] + ":" + dd.split(":")[1];
        } else if (nm <= diff && diff < nh) {//大于1分钟到1小时
            String dd = destime.split(" ")[1];
            result = "今天 " + dd.split(":")[0] + ":" + dd.split(":")[1];
        } else if (nh <= diff && diff < nd) {//大于1小时小于24小时
            if (IsYesterday(destime)) {
                String dd = destime.split(" ")[1];
                result = "昨天 " + dd.split(":")[0] + ":" + dd.split(":")[1];
            } else {
                String dd = destime.split(" ")[1];
                result = "今天 " + dd.split(":")[0] + ":" + dd.split(":")[1];
            }
        } else if (nd <= diff && diff < 2 * nd) {//大于24小于48小时
            if (IsYesterday(destime)) {
                String dd = destime.split(" ")[1];
                result = "昨天 " + dd.split(":")[0] + ":" + dd.split(":")[1];
            } else {
                String dd = destime.split(" ")[0];
                String dd2 = destime.split(" ")[1];
                result = dd.split("-")[1] + "-" + dd.split("-")[2] + " " + dd2.split(":")[0] + ":" + dd2.split(":")[1];
            }
        } else if (2 * nd <= diff && diff < 365 * nd) {//大于48小时小于1年
            String dd = destime.split(" ")[0];
            String dd2 = destime.split(" ")[1];
            result = dd.split("-")[1] + "-" + dd.split("-")[2] + " " + dd2.split(":")[0] + ":" + dd2.split(":")[1];
        } else {//大于1年
            result = destime.split(" ")[0];
        }
        if ("0 秒前".equals(result)) {
            String dd = destime.split(" ")[1];
            result = "今天 " + dd.split(":")[0] + ":" + dd.split(":")[1];
        }
        return result;
    }


    //获得阶段性时间的显示要求
    public static String getClassDatePost(String destime) {
        ////System.out.println("新的时间");
        if (TextUtils.isEmpty(destime)) {
            return "";
        }
        String result = "";
        Date nowDate = new Date();
//        ////System.out.println("当前时间"+nowDate);
        Date endDate = null;
        long nd = 1000 * 24 * 60 * 60;//每天毫秒数
        long nh = 1000 * 60 * 60;//每小时毫秒数
        long nm = 1000 * 60;//每分钟毫秒数
        long ns = 1000;//每秒毫秒数
        try {
            endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(destime);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        long diff = nowDate.getTime() - endDate.getTime();
        ////System.out.println(diff);
        if (diff < 0) {
            String dd = destime.split(" ")[0];
            String dd2 = destime.split(" ")[1];
            result = dd.split("-")[1] + "-" + dd.split("-")[2] + " " + dd2.split(":")[0] + ":" + dd2.split(":")[1];
        } else if (diff < nm && diff >= 0) {//小于一分钟
            result = diff / ns + " 秒前";
        } else if (nm <= diff && diff < nh) {//大于1分钟到1小时
            result = diff / nm + " 分钟前";
        } else if (nh <= diff && diff < nd) {//大于1小时小于24小时
            if (IsYesterday(destime)) {
                String dd = destime.split(" ")[1];
                result = "昨天 " + dd.split(":")[0] + ":" + dd.split(":")[1];
            } else {

                result = diff / nh + " 小时前";
            }
        } else if (nd <= diff && diff < 2 * nd) {//大于24小于48小时
            if (IsYesterday(destime)) {
                String dd = destime.split(" ")[1];
                result = "昨天 " + dd.split(":")[0] + ":" + dd.split(":")[1];
            } else {
                String dd = destime.split(" ")[0];
                String dd2 = destime.split(" ")[1];
                result = dd.split("-")[1] + "-" + dd.split("-")[2] + " " + dd2.split(":")[0] + ":" + dd2.split(":")[1];
            }
        } else if (2 * nd <= diff && diff < 365 * nd) {//大于48小时小于1年
            String dd = destime.split(" ")[0];
            String dd2 = destime.split(" ")[1];
            result = dd.split("-")[1] + "-" + dd.split("-")[2] + " " + dd2.split(":")[0] + ":" + dd2.split(":")[1];
        } else {//大于1年
            result = destime.split(" ")[0];
        }
        if ("0 秒前".equals(result)) {
            result = "刚刚";
        }
        return result;
    }

    public static boolean IsYesterday(String day, String format) {

        try {
            Calendar pre = Calendar.getInstance();
            Date predate = new Date(System.currentTimeMillis());
            pre.setTime(predate);

            Calendar cal = Calendar.getInstance();
            Date date = new SimpleDateFormat(format).parse(day);
            cal.setTime(date);

            if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
                int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                        - pre.get(Calendar.DAY_OF_YEAR);

                if (diffDay == -1) {
                    return true;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * end 比 date 大于0 就是 明天 后天
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static int getDayDiffer(Date startDate, Date endDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long startDateTime = dateFormat.parse(dateFormat.format(startDate)).getTime();
        long endDateTime = dateFormat.parse(dateFormat.format(endDate)).getTime();
        return (int) ((endDateTime - startDateTime) / (1000 * 3600 * 24));
    }

    public static int withToday(String day, String format) {

        try {
            Calendar pre = Calendar.getInstance();
            Date predate = new Date(System.currentTimeMillis());
            pre.setTime(predate);

            Calendar cal = Calendar.getInstance();
            Date date = new SimpleDateFormat(format).parse(day);
            cal.setTime(date);

            if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
                int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                        - pre.get(Calendar.DAY_OF_YEAR);
                return diffDay;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 1000;
    }

    public static boolean IsYesterday(String day) {
        return IsYesterday(day, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatLongAll(String longtime, String format) {
        try {

//            ////System.out.println("转化的时间戳:"+longtime);
            long longs = Long.parseLong(longtime);
            Date date = new Date();
            date.setTime(longs);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.format(date);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return longtime;
        }
    }

    public static String formatLongSecAll(String longtime, String format) {
        try {

//            ////System.out.println("转化的时间戳:"+longtime);
            long longs = Long.parseLong(longtime);
            longs = longs * 1000;
            Date date = new Date();
            date.setTime(longs);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.format(date);
        } catch (NumberFormatException e) {
            return longtime;
        }
    }

    public static String formatLongAll(String longtime) {
        return formatLongAll(longtime, "yyyy-MM-dd HH:mm:ss");
    }

    public static int getAge(Date birthDay, Date des) {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        cal.setTime(des);
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;   //计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;//当前日期在生日之前，年龄减一
                }
            } else {
                age--;//当前月份在生日之前，年龄减一
            }
        }
        return age;
    }

    public static int getAgeMonth(Date birthDay, Date des) {
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();
        bef.setTime(birthDay);
        aft.setTime(des);
        int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
        int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
        return Math.abs(month + result);
    }

    public static int getAgeDay(Date d1, Date d2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        //long类型的日期也支持
//    cal1.setTimeInMillis(long);
//    cal2.setTimeInMillis(long);
        cal1.setTime(d1);
        cal2.setTime(d2);

        //获取日期在一年(月、星期)中的第多少天
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);//第335天
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);//第365天

        //获取当前日期所在的年份
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);

        //如果两个日期的是在同一年，则只需要计算两个日期在一年的天数差；
        //不在同一年，还要加上相差年数对应的天数，闰年有366天

        if (year1 != year2) //不同年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) //闰年
                {
                    timeDistance += 366;
                } else //不是闰年
                {
                    timeDistance += 365;
                }
            }
            ////System.out.println(timeDistance + (day2 - day1));
            return timeDistance + (day2 - day1);
        } else //同年
        {
            ////System.out.println("判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        }
    }


    //获得阶段性时间的显示要求
    public static String getClassDate(String destime) {
        if (TextUtils.isEmpty(destime)) {
            ////System.out.println("时间为空");
            return "";
        }
        String result = "";
        Date nowDate = new Date();
        ////System.out.println("目标时间" + destime);
        Date endDate = null;
        long nd = 1000 * 24 * 60 * 60;//每天毫秒数
        long nh = 1000 * 60 * 60;//每小时毫秒数
        long nm = 1000 * 60;//每分钟毫秒数
        long ns = 1000;//每秒毫秒数
        try {
            endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(destime);
        } catch (ParseException e) {
            e.printStackTrace();
            ////System.out.println("时间出错");
            return "";
        }
        long diff = nowDate.getTime() - endDate.getTime();
        if (diff < 0) {
            String dd = destime.split(" ")[0];
            String dd2 = destime.split(" ")[1];
            result = dd.split("-")[1] + "-" + dd.split("-")[2] + " " + dd2.split(":")[0] + ":" + dd2.split(":")[1];
        } else if (diff < nm && diff >= 0) {
            result = diff / ns + " 秒前";
        } else if (nm <= diff && diff < nh) {//大于1分钟到1小时
            result = diff / nm + " 分钟前";
        } else if (nh <= diff && diff < nd) {//大于1小时小于24小时
            if (IsYesterday(destime)) {
                String dd = destime.split(" ")[1];
                result = "昨天 " + dd.split(":")[0] + ":" + dd.split(":")[1];
            } else {
                result = diff / nh + " 小时前";
            }
        } else if (nd <= diff && diff < 2 * nd) {//大于24小于48小时

            if (IsYesterday(destime)) {
                String dd = destime.split(" ")[1];
                result = "昨天 " + dd.split(":")[0] + ":" + dd.split(":")[1];
            } else {
                String dd = destime.split(" ")[0];
                String dd2 = destime.split(" ")[1];
                result = dd.split("-")[1] + "-" + dd.split("-")[2] + " " + dd2.split(":")[0] + ":" + dd2.split(":")[1];
            }
            String dd = destime.split(" ")[1];
            result = "昨天 " + dd.split(":")[0] + ":" + dd.split(":")[1];
        } else if (2 * nd <= diff && diff < 365 * nd) {//大于48小时小于1年
            String dd = destime.split(" ")[0];
            String dd2 = destime.split(" ")[1];
            result = dd.split("-")[1] + "-" + dd.split("-")[2] + " " + dd2.split(":")[0] + ":" + dd2.split(":")[1];
        } else {//大于1年
            result = destime.split(" ")[0];
        }
        if ("0 秒前".equals(result)) {
            result = "刚刚";
        }
        ////System.out.println("结果时间" + result);
        return result;
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String[] getDistanceTimeNoDouble(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String timeIntervals[] = new String[]{day + "", hour + "", min + "", sec + ""};
//        ////System.out.println(day + "天" + hour + "小时" + min + "分" + sec + "秒");
        return timeIntervals;
    }


    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String[] getDistanceTime(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String timeIntervals[] = new String[]{day + "", String.format("%02d", hour) + "", String.format("%02d", min) + "", String.format("%02d", sec) + ""};
//        ////System.out.println(day + "天" + hour + "小时" + min + "分" + sec + "秒");
        return timeIntervals;
    }

    /**
     * 两个时间相差距离多少秒
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return long 返回值为：秒
     */
    public static long getDistanceSec(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }

    /**
     * 两个时间相差距离多少分钟
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return long 返回值为：秒
     */
    public static long getDistanceHour(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            hour = diff / ( 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hour;
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String[] getDistanceTimeOnlySecond(long diff) {

        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {

            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String timeIntervals[] = new String[]{day + "", String.format("%02d", hour) + "", String.format("%02d", min) + "", String.format("%02d", sec) + ""};
        return timeIntervals;
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String[] getDistanceTimeNoDay(long diff) {

        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {

            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String timeIntervals[] = new String[]{String.format("%02d", day * 24 + hour) + "", String.format("%02d", min) + "", String.format("%02d", sec) + ""};
        return timeIntervals;
    }


    public static String getDistanceTimeNoDayString(long diff) {

        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {

            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String timeIntervals[] = new String[]{String.format("%02d", day * 24 + hour) + "", String.format("%02d", min) + "", String.format("%02d", sec) + ""};
        String result = "";
        if (!"00".equals(timeIntervals[0])) {
            result = result + timeIntervals[0] + ":";
        }
        result = result + timeIntervals[1] + ":";
        result = result + timeIntervals[2] + ":";
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public static String getLivePlayerTime(long diff) {

        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            if (diff > 60) {
                sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            } else {
                sec = diff;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String timeIntervals[] = new String[]{String.format("%02d", day * 24 + hour) + "", String.format("%02d", min) + "", String.format("%02d", sec) + ""};
        String result = "";
        if (!"00".equals(timeIntervals[0])) {
            result = result + timeIntervals[0] + ":";
        }
        result = result + timeIntervals[1] + ":";
        result = result + timeIntervals[2] + ":";
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public static long[] getDistanceTimeOnlySecondStringArray(long diff) {

        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {

            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long timetip[] = new long[]{day, hour, min, sec};

        return timetip;
    }

    public static String getDistanceTimeOnlySecondString(long diff) {

        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {

            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String timetip[] = new String[]{day + "", String.format("%02d", hour) + "", String.format("%02d", min) + "", String.format("%02d", sec) + ""};
        String timeresult = "";
        if (!"0".equals(timetip[0])) {
            timeresult = timeresult + timetip[0] + ":";
        }
        timeresult = timeresult + timetip[1] + ":";
        timeresult = timeresult + timetip[2] + ":";
        timeresult = timeresult + timetip[3] + ":";
        if (timeresult.length() > 0) {
            timeresult = timeresult.substring(0, timeresult.length() - 1);
        }
        return timeresult;
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String[] getDistanceTimeOnlySecondNoDouble(long diff) {

        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {

            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String timeIntervals[] = new String[]{day + "", hour + "", min + "", sec + ""};
        return timeIntervals;
    }


    public static String getDistanceTimeInt(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        ////System.out.println("str1:"+str1+"，str2:"+str2);
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String timeIntervals[] = new String[]{day + "", hour + "", min + "", sec + ""};
        return String.format("%02d", hour) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec) + "";
    }


    public static String[] getDistanceTimeNoDay(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        ////System.out.println("str1:"+str1+"，str2:"+str2);
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String timeIntervals[] = new String[]{String.format("%02d", (hour + day * 24)) + "", String.format("%02d", min) + "", String.format("%02d", sec) + ""};
        return timeIntervals;
    }


    public static String getDistanceTimeString(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        ////System.out.println("str1:"+str1+"，str2:"+str2);
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String timeIntervals[] = new String[]{day + "", hour + "", min + "", sec + ""};
        return String.format("%02d", hour + day * 24) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec) + "";
    }

    @SuppressLint("SimpleDateFormat")
    public static String dateToStr(String dateDate) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date returnDate = dateFormat.parse(dateDate);
            SimpleDateFormat formatter = new SimpleDateFormat(DF_MM_DD_HH_MM);
            String dateString = formatter.format(returnDate);
            return dateString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 是否是同一天
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 结果
     */
    public static boolean isSameDate(Date date1, Date date2) {
        if (date1 == null) {
            return false;
        }
        if (date2 == null) {
            return false;
        }
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    public static long getTimeMill(Date date) {
        return date.getTime();
    }

    public static boolean isBetween(Date date, Date startDate, Date endDate) {

        return date != null && startDate != null && endDate != null &&
                getYearMonthDay(date) >= getYearMonthDay(startDate) &&
                getYearMonthDay(date) <= getYearMonthDay(endDate);

    }

    public static long getYearMonthDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 判断是否为闰年
     *
     * @param year 年份
     * @return 是：闰年 否：平年
     */
    public static boolean isLeapYear(int year) {
        return (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);
    }

    /**
     * 根据年份和月份获取当月天数
     *
     * @param year  年份
     * @param month 月份
     * @return 天数
     */
    public static int getDays(int year, int month) {
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else {
            return 31;
        }
    }


    /**
     * 时间戳转字符串
     *
     * @param timestamp     时间戳
     * @param isPreciseTime 是否包含时分
     * @return 格式化的日期字符串
     */
    public static String long2Str(long timestamp, boolean isPreciseTime) {
        return long2Str(timestamp, getFormatPattern(isPreciseTime));
    }

    public static String long2Str(long timestamp, String pattern) {
        return new SimpleDateFormat(pattern, Locale.CHINA).format(new Date(timestamp));
    }

    /**
     * 字符串转时间戳
     *
     * @param dateStr       日期字符串
     * @param isPreciseTime 是否包含时分
     * @return 时间戳
     */
    public static long str2Long(String dateStr, boolean isPreciseTime) {
        return str2Long(dateStr, getFormatPattern(isPreciseTime));
    }

    public static long str2Long(String dateStr, String pattern) {
        try {
            return new SimpleDateFormat(pattern, Locale.CHINA).parse(dateStr).getTime();
        } catch (Throwable ignored) {
        }
        return 0;
    }

    private static String getFormatPattern(boolean showSpecificTime) {
        if (showSpecificTime) {
            return DATE_FORMAT_PATTERN_YMD_HM;
        } else {
            return DATE_FORMAT_PATTERN_YMD;
        }
    }

    //12点以后为下午
    public static Boolean isAm(String timeStr) {
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat("HH");
        try {
            date = df.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int time = Integer.parseInt(df.format(date));
        if (time < 12) {
            return true;
        } else {
            return false;
        }

    }

    public static String getWeek(Date dates) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date d = dates;
        cal.setTime(d);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String week = "";
        switch (w) {
            case 0:
                week = "周日";
                break;
            case 1:
                week = "周一";
                break;
            case 2:
                week = "周二";
                break;
            case 3:
                week = "周三";
                break;
            case 4:
                week = "周四";
                break;
            case 5:
                week = "周五";
                break;
            case 6:
                week = "周六";
                break;
        }
        return week;
    }

    /**
     * 根据返回的数据进行设置周几
     *
     * @param week
     * @return
     */
    public static String getWeek(String week) {
        switch (week) {
            case "0":
                week = "周日";
                break;
            case "1":
                week = "周一";
                break;
            case "2":
                week = "周二";
                break;
            case "3":
                week = "周三";
                break;
            case "4":
                week = "周四";
                break;
            case "5":
                week = "周五";
                break;
            case "6":
                week = "周六";
                break;
            case "7":
                week = "周日";
                break;
        }
        return week;
    }

    /**
     * 时间转换格式 10:00:00 转 10:00
     *
     * @param time
     * @return
     */
    public static String getDateTimeSplit(@Nullable String time) {
        if (TextUtils.isEmpty(time)) return "";

        if (time.contains(":")) {
            String[] split = time.split(":");
            //如果最后秒数还为00进行截取
            if ("00".equals(split[split.length - 1])) {
                time = time.substring(0, time.lastIndexOf(":"));
            }
        }
        return time;
    }

    /**
     * 以空格截取时间
     * yyyy-MM-dd HH:mm:ss
     */
    public static String[] getDateArray(String time) {
        if (TextUtils.isEmpty(time)) {
            return null;
        }

        return time.split("\\s+");
    }
}