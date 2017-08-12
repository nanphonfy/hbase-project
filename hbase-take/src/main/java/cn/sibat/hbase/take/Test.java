package cn.sibat.hbase.take;

import cn.sibat.hbase.take.util.StringUtil;

import java.text.ParseException;

/**
 * Created by nanphonfy on 2017/8/7.
 */
public class Test {
    public static void main(String[] args) throws ParseException {
        System.out.println("0001097960:17-07-12 06:05:00".substring(0,10));
        System.out.println("2017-04".substring(2));

        System.out.println("17-07-12 18:50:00".length());
        System.out.println("17-07-12 ".length());
        System.out.println("0001097960:".length());

        String str = "0001097960:17-07-12 06:05:00|1,54.119998931884766,1,54.12,0,0.0,0,0.0,0,0.0,0,0.0,0,0.0";
        String[] arr = str.split("\\|");
        String key = arr[0];
        System.out.println(key.substring(11,19));
                //.substring(9,11));
        String value = arr[1];

        String prefixKey = key.substring(0, 10);
        long timestamp = StringUtil.getTimestamp(key.substring(11));
        String newKey = prefixKey + ":" + (Long.MAX_VALUE - timestamp);
        System.out.println(StringUtil.getTimestamp("17-07-12",StringUtil.PATTERN_yy_MM_dd));
        System.out.println(StringUtil.getTimestamp("17-07-15",StringUtil.PATTERN_yy_MM_dd));
        System.out.println(newKey);

        System.out.println("9223370537044075807".length());
        System.out.println("17-08-04 16:40:00".length());
    }
}
