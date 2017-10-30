package cc.mangomango.cloudadmin.web.controller;

import cc.mangomango.cloudadmin.util.MvcUtil;
import cc.mangomango.cloudadmin.web.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 作者:majunlei
 * 日期:2017/10/12 下午2:01
 */
@RestController
@RequestMapping("")
public class FileController {

    private static Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadImage(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        if (CollectionUtils.isEmpty(files)) {
            return MvcUtil.returnJSON(1, "文件不能为空", null);
        }
        for (MultipartFile file : files) {
            if (file == null || StringUtils.isEmpty(file.getOriginalFilename())) {
                return MvcUtil.returnJSON(1, "文件不能为空", null);
            }
        }
        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            boolean flag = fileService.save(filename, file);
            if (!flag) {
                return MvcUtil.returnJSON(1, "上传图片异常", null);
            }
        }
        return MvcUtil.returnSuc();
    }

}
