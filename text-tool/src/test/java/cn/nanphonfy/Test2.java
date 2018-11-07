package cn.nanphonfy;

/**
 * 特殊符号替换
 * @author nanphonfy(南风zsr)
 * @date 2018/11/5
 */
public class Test2 {
    public static void main(String[] args) {
        String str = "脑膜炎\n[G03.900]";
        System.out.println(str.substring(str.indexOf("[")).replace("[","").replace("]",""));
    }
}
