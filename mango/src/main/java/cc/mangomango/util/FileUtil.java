package cc.mangomango.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 作者:majunlei
 * 日期:2017/10/12 下午9:01
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static void delFile(String filePath) {
        File file = new File(filePath);
        file.deleteOnExit();
    }

}
