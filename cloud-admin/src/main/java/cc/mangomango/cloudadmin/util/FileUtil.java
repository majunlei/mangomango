package cc.mangomango.cloudadmin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 作者:majunlei
 * 日期:2017/10/12 下午9:01
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static boolean uploadImg(MultipartFile file, String filePath) {
        try (OutputStream os = new FileOutputStream(filePath);
            InputStream is = file.getInputStream()) {
            int temp;
            while ((temp = is.read()) != -1) {
                os.write(temp);
            }
        } catch (Exception e) {
            logger.error("uploadImg Exception. filePath:{}", filePath, e);
            return false;
        }
        return true;
    }

}
