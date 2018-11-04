package cn.nanphonfy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtil {
    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    /**
     * 把字符型数字转换成整型.
     *
     * @param str 字符型数字
     * @return int 返回整型值。如果不能转换则返回默认值defaultValue.
     */
    public static int getInt(String str, int defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        if (isInt(str)) {
            return Integer.parseInt(str);
        } else {
            return defaultValue;
        }
    }

    public static double getDouble(String str, double defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static float getFloat(String str, float defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static long getLong(String str, long defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String getStr(Object str, String defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return String.valueOf(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 判断一个字符串是否为数字
     */
    public static boolean isInt(String str) {
        return str.matches("\\d+");
    }

    /**
     * 判断一个字符串是否空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
