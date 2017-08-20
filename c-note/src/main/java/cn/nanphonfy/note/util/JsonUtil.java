package cn.nanphonfy.note.util;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.*;

public class JsonUtil {

    /**
     * 从一个JSON 对象字符格式中得到一个java对象
     * 形如： {"id" : idValue, "name" : nameValue,
     * "aBean" : {"aBeanId" : aBeanIdValue, ...}}
     *
     * @param object
     * @param clazz
     * @return
     */
    private static Logger log4j = Logger.getLogger(JsonUtil.class);

    public static Object convertJsonToObject(String jsonString, Class clazz) {
        JSONObject jsonObject = null;
        try {
            setDataFormat2JAVA();
            jsonObject = JSONObject.fromObject(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.toBean(jsonObject, clazz);
    }

    /**
     * 从一个JSON 对象字符格式中得到一个java对象
     * 其中beansList是一类的集合，形如： {"id" : idValue, "name" :
     * nameValue, "aBean" : {"aBeanId" : aBeanIdValue, ...}, beansList:[{}, {},
     * ...]}
     *
     * @param jsonString
     * @param clazz
     * @param map        集合属性的类型 (key : 集合属性名, value : 集合属性类型class) eg: ("beansList" :
     *                   Bean.class)
     * @return
     * @throws Exception
     */
    public static Object convertJsonToObject(String jsonString, Class clazz, Map map) throws Exception {
        JSONObject jsonObject = null;
        try {
            setDataFormat2JAVA();
            jsonObject = JSONObject.fromObject(jsonString);
        } catch (Exception e) {
            throw new Exception("将一个JSON字符串转化为java异常！", e);
        }
        return JSONObject.toBean(jsonObject, clazz, map);
    }

    /**
     * 把数据对象转换成json字符串
     * DTO对象形如：{"id" : idValue, "name" : nameValue, ...}
     * 数组对象形如：[{}, {}, {}, ...] map对象形如：{key1 : {"id" : idValue, "name" :
     * nameValue, ...}, key2 : {}, ...}
     *
     * @param object
     * @return
     */
    public static String getJSONString(Object object) throws Exception {
        String jsonString = null;
        JsonConfig jsonConfig = new JsonConfig();
        if (object != null) {
            if (object instanceof Collection || object instanceof Object[]) {
                jsonString = JSONArray.fromObject(object, jsonConfig).toString();
            } else {
                jsonString = JSONObject.fromObject(object, jsonConfig).toString();
            }
        }
        return jsonString == null ? "{}" : jsonString;
    }

    private static void setDataFormat2JAVA() {

        // 设定日期转换格式
        JSONUtils.getMorpherRegistry()
                .registerMorpher(new DateMorpher(new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" }));
    }

    /**
     * 拼接JSON字符串，有些特殊字符需要替换掉
     * 如果未替换这些特殊字符生成的JSON不会被正确解析
     *
     * @param str
     * @return
     */
    public static String ConvertStringToJson(String str) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.toCharArray()[i];
            switch (c) {
            case '\"':
                sb.append("\\\"");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '/':
                sb.append("\\/");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            default:
                sb.append(c);
                break;
            }
        }
        return sb.toString();
    }

    public static String toString(Object obj) {
        StringBuilder sb = null;
        try {
            Class<?> c = obj.getClass();
            Field[] fields = c.getDeclaredFields();

            sb = new StringBuilder();
            sb.append(obj.getClass().getName());
            sb.append(" {");

            int i = 1;
            for (Field fd : fields) {
                fd.setAccessible(true);
                sb.append(fd.getName());
                sb.append(":");
                sb.append(fd.get(obj));

                if (i != fields.length) {
                    sb.append(", ");
                }
                i++;
            }
            sb.append("}");
        } catch (Exception e) {
            log4j.error(e.getMessage(), e);
        }
        return sb.toString();
    }
}
