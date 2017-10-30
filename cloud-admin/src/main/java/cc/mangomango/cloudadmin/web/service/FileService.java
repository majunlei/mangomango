package cc.mangomango.cloudadmin.web.service;

import cc.mangomango.cloudadmin.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * 作者:majunlei
 * 日期:2017/10/12 下午9:47
 */
@Service
public class FileService {

    private static String CLOUD_DIR = "/data/cloud/";

    private static Logger logger = LoggerFactory.getLogger(FileService.class);

    public boolean save(String fileName, MultipartFile file) {
        try {
            boolean success = FileUtil.uploadImg(file, CLOUD_DIR + fileName);
            if (!success) {
                return false;
            }
        } catch (Exception e) {
            logger.error("保存文件发生错误. fileName:{}", fileName, e);
            return false;
        }
        return true;
    }

}
