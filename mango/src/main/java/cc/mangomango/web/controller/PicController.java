package cc.mangomango.web.controller;

import cc.mangomango.constants.Constants;
import cc.mangomango.domain.Pic;
import cc.mangomango.service.PicService;
import cc.mangomango.util.MvcUtil;
import com.alibaba.fastjson.JSONObject;
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
@RequestMapping("/pic")
public class PicController {

    private static Logger logger = LoggerFactory.getLogger(PicController.class);

    @Autowired
    private PicService picService;

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public String getByPage(@RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 8;
        try {
            int count = picService.getCount();
            int pages = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            int offset = (page - 1) * pageSize;
            List<Pic> picList = picService.getByPage(offset, pageSize);
            for (Pic pic : picList) {
                pic.setUrl(Constants.PIC_URL_PREFIX + pic.getUrl());
                pic.setThumbUrl(Constants.PIC_URL_PREFIX + pic.getThumbUrl());
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 0);
            jsonObject.put("msg", "success");
            jsonObject.put("data", picList);
            jsonObject.put("pages", pages);
            return jsonObject.toJSONString();
        } catch (Exception e) {
            logger.error("getByPage Exception. page:{} pageSize:{}", page, pageSize, e);
        }
        return MvcUtil.returnJSON(1, "获取图片异常", null);
    }

    @RequestMapping(value = "del", method = RequestMethod.GET)
    public String del(@RequestParam("id") long id) {
        try {
            int num = picService.del(id);
            if (num > 0) {
                return MvcUtil.returnSuc();
            }
        } catch (Exception e) {
            logger.error("del Exception. id:{}", id, e);
        }
        return MvcUtil.returnJSON(1, "删除失败", null);
    }

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
            String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length()).toLowerCase();
            if (!"jpg".equalsIgnoreCase(fileType) && !"jpeg".equalsIgnoreCase(fileType) && !"png".equalsIgnoreCase(fileType)) {
                return MvcUtil.returnJSON(1, "\"文件格式只能是jpg或png\"", null);
            }
            boolean flag = picService.save(fileType, file);
            if (!flag) {
                return MvcUtil.returnJSON(1, "上传图片异常", null);
            }
        }
        return MvcUtil.returnSuc();
    }

}
