package cn.nanphonfy;

/**
 * @author nanphonfy(南风zsr)
 * @date 2018/11/5
 */
public class Test2 {
    public static void main(String[] args) {
        String str = "脑膜炎\n[G03.900]";
        System.out.println(str.substring(str.indexOf("[")).replace("[","").replace("]",""));
    }
}
