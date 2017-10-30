package cc.mangomango.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by majunlei on 2017/10/27.
 */
public class DateUtil {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getNowStr() {
        return format.format(new Date());
    }

    public static String getTimeDesc(int time) {
        int now = (int) (System.currentTimeMillis() / 1000);

        int differ = now - time;

        if (differ < 60) {
            return differ + "秒前";
        }
        if (differ >= 60 && differ < 3600) {
            return differ / 60 + "分钟前";
        }
        if (differ >= 3600 && differ < 3600 * 24) {
            return differ / 3600 + "小时前";
        }
        if (differ >= 3600 * 24) {
            return differ / (3600 * 24) + "天前";
        }
        return "未知";
    }
}
