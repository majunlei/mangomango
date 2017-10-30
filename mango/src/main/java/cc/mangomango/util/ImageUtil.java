package cc.mangomango.util;

import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by majunlei on 2017/10/16.
 */
public class ImageUtil {

    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    public static boolean uploadImg(MultipartFile file, String filePath, String thumbnailPath) {
        try (InputStream is = file.getInputStream()) {
            Thumbnails.of(is).size(1500, 2000).toFile(filePath);
            if (!StringUtils.isEmpty(thumbnailPath)) {
                File toFile = new File(filePath);
                Thumbnails.of(toFile).size(600, 800).toFile(thumbnailPath);
            }
        } catch (Exception e) {
            logger.error("uploadImg Exception. filePath:{} thumbnailPath:{}", filePath, thumbnailPath, e);
            return false;
        }
        return true;
    }

    /**
     * @Description: 将base64编码字符串转换为图片
     * @param imgStr
     * @param path
     * @return
     */
    public static boolean generateImage(String imgStr, String path) {
        if (StringUtils.isEmpty(imgStr) || StringUtils.isEmpty(path)) {
            return false;
        }
        try (OutputStream out = new FileOutputStream(path);) {
            BASE64Decoder decoder = new BASE64Decoder();
            // 解密
            byte[] b = decoder.decodeBuffer(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out.write(b);
            return true;
        } catch (Exception e) {
            logger.error("generateImage Exception. imgStr:{} path:{} thumbnailPath:{}");
            return false;
        }
    }

}
