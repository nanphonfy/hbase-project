package cn.nanphonfy.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.ws.rs.core.Response;
import java.util.List;

public class FastJsonUtil {
    public static JSONObject getResponse(int status, String msg) {
        JSONObject obj = new JSONObject();
        obj.put("status", status);
        obj.put("msg", msg);
        return obj;
    }

    public static String error(String msg) {
        return getResponse(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), msg).toString();
    }

    public static String notData(String msg) {
        return getResponse(Response.Status.BAD_REQUEST.getStatusCode(), msg).toString();
    }

    public static String paramsError(String msg) {
        return getResponse(Response.Status.BAD_REQUEST.getStatusCode(), msg).toString();
    }

    public static String ok() {
        return getResponse(200, "ok").toString();
    }

    /**
     * 将对象转换为json数据
     *
     * @param data 对象数据
     * @return
     */
    public static String toJson(Object data) {
        //解决fastjson序列化乱序的问题
        return JSON.toJSONString(data);
    }

    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            // JSON串转用户对象列表
            T t = JSON.parseObject(jsonData, beanType);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        try {
            // JSON串转用户对象列表
            List<T> t = JSON.parseArray(jsonData, beanType);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
