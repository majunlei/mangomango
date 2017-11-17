package cc.mangomango.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by majunlei on 2017/10/16.
 */
@Component
public class Constants {


    public static String PIC_DIR;


    public static String PIC_URL_PREFIX;

    @Value("${mango.pic.dir}")
    public void setPicDir(String picDir) {
        PIC_DIR = picDir;
    }

    @Value("${mango.url.preffix}")
    public void setPicUrlPrefix(String picUrlPrefix) {
        PIC_URL_PREFIX = picUrlPrefix;
    }
}
