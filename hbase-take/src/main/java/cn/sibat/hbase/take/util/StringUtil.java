package cn.sibat.hbase.take.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import org.apache.hadoop.hbase.filter.CompareFilter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by nanphonfy on 2017/8/3.
 */
public class StringUtil {
    private static final SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
    private static final int GIVE_UP_ZERO = 100000;//5*60*1000 舍弃5个零
    public static final String PATTERN_yyMMdd = "yyMMdd";
    public static final String PATTERN_yy_MM_dd = "yy-MM-dd";
    public static final int ROW_KEY_SIZE = "0000047489:16-12-24 18:40:00,".length();

    /**
     * 日期单位数补0
     *
     * @param i
     * @return
     */
    public static String getDay(int i) {
        String day = "";
        if (i < 10) {
            day = "0" + i;
        } else {
            day = "" + i;
        }
        return day;
    }

    /**
     * 字符串转int
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static int getInt(String str, int defaultValue) {
        if (str == null)
            return defaultValue;
        if (isInt(str)) {
            return Integer.parseInt(str);
        } else {
            return defaultValue;
        }
    }

    /**
     * 判断一个字符串是否为数字
     */
    public static boolean isInt(String str) {
        return str.matches("\\d+");
    }

    public static long getTimestamp(String str) throws ParseException {
        return df.parse(str).getTime();
    }

    public static long getTimestamp(String str, String pattern) throws ParseException {
        if (str == null) {
            return 0;
        }
        SimpleDateFormat _df = new SimpleDateFormat(pattern);
        return _df.parse(str).getTime();
    }

    public static long getLongMaxSubTimestamp(long timestamp) {
        return (Long.MAX_VALUE - timestamp) / GIVE_UP_ZERO;
    }

    /**
     * 得到五分钟间隔的时间片
     *
     * @param str
     * @return
     * @throws ParseException
     */
    public static int getTimesLice(String str) throws ParseException {
        String[] arr = str.split(":");
        int h = getInt(arr[0], 0);
        int m = getInt(arr[1], 0);

        return (h * 60 + m) / 5;
    }

    /**
     * 时间片不足3位补0
     *
     * @param timeslie
     * @return
     * @throws ParseException
     */
    public static String getQualifier(int timeslie) throws ParseException {
        String qualifier = "";
        if (timeslie < 10) {
            qualifier = "00" + timeslie;
        } else if (timeslie < 100) {
            qualifier = "0" + timeslie;
        } else {
            qualifier = "" + timeslie;
        }
        return qualifier;
    }

    /**
     * 算出可视化时间单位
     *
     * @param duration 是 毫秒
     * @return
     */
    public static String getDuration(long duration) {
        duration /= 1000;
        if (duration <= 0) {
            return "0";
        }
        try {
            long hour = duration / 3600;
            long min = (duration % 3600) / 60;
            long s = (duration % 3600) % 60;
            if (hour == 0) {
                if (min == 0) {
                    return s + "秒";
                } else {
                    return min + "分钟" + s + "秒";
                }
            } else {
                return hour + "小时" + min + "分钟" + s + "秒";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String subOndDay(String dateTime) throws ParseException {
        Date beginDate = parseStringToDate(dateTime);
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
        return df.format(date.getTime());
    }

    public static Date parseStringToDate(String date) throws ParseException {
        if (date == null) {
            return null;
        }
        return df.parse(date);
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(CompareFilter.CompareOp.LESS);
        System.out.println("92233705370659".length());
        System.out.println(System.currentTimeMillis());
        System.out.println(getTimesLice("06:05:00"));
        System.out.println(getTimesLice("23:55:00"));
        System.out.println(getTimestamp("17-07-12", StringUtil.PATTERN_yy_MM_dd));//毫秒 分钟*秒 60*5=300 所以可以取舍掉最后的5个零
        System.out.println((Long.MAX_VALUE - getTimestamp("17-07-12", StringUtil.PATTERN_yy_MM_dd)) / 100000);
        System.out.println((Long.MAX_VALUE - getTimestamp("17-07-12", StringUtil.PATTERN_yy_MM_dd)));
        System.out.println((Long.MAX_VALUE - getTimestamp("17-07-13", StringUtil.PATTERN_yy_MM_dd)));
        System.out.println("===============");


        String str = "0001097960:17-07-12 06:05:00|1,54.119998931884766,1,54.12,0,0.0,0,0.0,0,0.0,0,0.0,0,0.0";
        String[] arr = str.split("\\|");
        String key = arr[0];
        String subStr = key.substring(0, 10);//日期
        System.out.println(subStr);
        System.out.println(key.substring(11));
        System.out.println(Long.MAX_VALUE);


//        long timestamp = StringUtil.getTimestamp(key.substring(11));

//        System.out.println(timestamp);
        System.out.println(getTimestamp("17-07-12 06:05:00"));
        System.out.println(Long.MAX_VALUE - getTimestamp("17-07-12 06:05:00"));

        System.out.println(getTimestamp("17-07-12 06:05:00") - getTimestamp("170712", PATTERN_yyMMdd));
//        System.out.println("0001097960:17-07-12".length());
//        System.out.println("0001097960:1".length());
        System.out.println(getDay(10));
    }

}
