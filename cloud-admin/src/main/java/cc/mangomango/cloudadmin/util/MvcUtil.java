package cc.mangomango.cloudadmin.util;

import com.alibaba.fastjson.JSONObject;

/**
 * 作者:majunlei
 * 日期:2017/10/12 下午8:18
 */
public class MvcUtil {

    public static String returnJSON(int code, String msg, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        jsonObject.put("data", data);
        return jsonObject.toJSONString();
    }

    public static String returnSuc() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "success");
        jsonObject.put("data", null);
        return jsonObject.toJSONString();
    }

    public static String returnErr() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        jsonObject.put("msg", "error");
        jsonObject.put("data", null);
        return jsonObject.toJSONString();
    }
}
