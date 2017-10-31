package cc.mangomango.web.controller;

import cc.mangomango.constants.Constants;
import cc.mangomango.domain.PicUrl;
import cc.mangomango.domain.Story;
import cc.mangomango.domain.Xq;
import cc.mangomango.service.XqService;
import cc.mangomango.util.DateUtil;
import cc.mangomango.util.MvcUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by majunlei on 2017/10/29.
 */
@RestController
@RequestMapping("/xq")
public class XqController {

    private static Logger logger = LoggerFactory.getLogger(XqController.class);

    @Autowired
    private XqService xqService;

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public String getByPage(@RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 5;
        try {
            int offset = (page - 1) * pageSize;
            int count = xqService.getCount();
            int pages = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            List<Xq> xqList = xqService.getByPage(offset, pageSize);
            for (Xq xq: xqList) {
                List<PicUrl> urls = xq.getUrls();
                if (!CollectionUtils.isEmpty(urls)) {
                    for (PicUrl url : urls) {
                        url.setUrl(Constants.PIC_URL_PREFIX + url.getUrl());
                        url.setThumbUrl(Constants.PIC_URL_PREFIX + url.getThumbUrl());
                    }
                }
                xq.setTimeDesc(DateUtil.getTimeDesc(xq.getCtime()));
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 0);
            jsonObject.put("msg", "success");
            jsonObject.put("data", xqList);
            jsonObject.put("pages", pages);
            return jsonObject.toJSONString();
        } catch (Exception e) {
            logger.error("getByPage Exception. page:{} pageSize:{}", page, pageSize, e);
        }
        return MvcUtil.returnJSON(1, "获取列表异常", null);
    }

    @RequestMapping(value = "like/{id}", method = RequestMethod.GET)
    public String like(@PathVariable("id") long id) {
        try {
            xqService.like(id);
            return MvcUtil.returnSuc();
        } catch (Exception e) {
            logger.error("like Exception. id:{}", id, e);
        }
        return MvcUtil.returnJSON(1, "点赞异常", null);
    }

    @RequestMapping(value = "view/add/{id}", method = RequestMethod.GET)
    public String viewAdd(@PathVariable("id") long id) {
        try {
            xqService.viewAdd(id);
            return MvcUtil.returnSuc();
        } catch (Exception e) {
            logger.error("viewAdd Exception. id:{}", id, e);
        }
        return MvcUtil.returnJSON(1, "ViewAdd异常", null);
    }

    @RequestMapping(value = "comment/add/{id}", method = RequestMethod.GET)
    public String commentAdd(@PathVariable("id") long id, @RequestParam("comment") String comment) {
        logger.info("commentAdd start. id:{} comment:{}", id, comment);
        try {
            xqService.commentAdd(id, comment);
            return MvcUtil.returnSuc();
        } catch (Exception e) {
            logger.error("commentAdd Exception. id:{} comment:{}", id, comment, e);
        }
        return MvcUtil.returnJSON(1, "添加评论异常", null);
    }

    @RequestMapping(value = "add/{stamp}", method = RequestMethod.POST)
    public String add(@RequestParam("xq") String xq, @PathVariable("stamp") int stamp,
                      @RequestParam(value = "author") String author) {
        logger.info("add start. xq:{} stamp:{} author:{}", xq, stamp, author);
        try {
            xqService.save(xq, stamp, author);
            return MvcUtil.returnSuc();
        } catch (Exception e) {
            logger.error("add Exception. xq:{} stamp:{} author:{}", xq, stamp, author, e);
            return MvcUtil.returnJSON(1, "提交失败", null);
        }
    }

    @RequestMapping(value = "/upload/{stamp}", method = RequestMethod.POST)
    public String uploadImage(@PathVariable("stamp") int stamp, @RequestParam("file") MultipartFile file) {
        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length()).toLowerCase();
        if (!"jpg".equalsIgnoreCase(fileType) && !"jpeg".equalsIgnoreCase(fileType) && !"png".equalsIgnoreCase(fileType)) {
            return MvcUtil.returnJSON(1, "文件格式只能是jpg或png", null);
        }
        boolean success = xqService.upload(stamp, fileType, file);
        if (success) {
            return MvcUtil.returnSuc();
        }
        return MvcUtil.returnErr();
    }
}
