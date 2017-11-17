package cc.mangomango.web.controller;

import cc.mangomango.constants.Constants;
import cc.mangomango.domain.Story;
import cc.mangomango.service.StoryService;
import cc.mangomango.util.MvcUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by majunlei on 2017/10/15.
 */
@Controller
@RequestMapping("/story")
public class StoryController {

    private static Logger logger = LoggerFactory.getLogger(StoryController.class);

    @Autowired
    private StoryService storyService;

    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public String getByPage(@RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 10;
        try {
            int offset = (page - 1) * pageSize;
            int count = storyService.getCount();
            int pages = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            List<Story> storyList = storyService.getByPage(offset, pageSize);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 0);
            jsonObject.put("msg", "success");
            jsonObject.put("data", storyList);
            jsonObject.put("pages", pages);
            return jsonObject.toJSONString();
        } catch (Exception e) {
            logger.error("getByPage Exception. page:{} pageSize:{}", page, pageSize, e);
        }
        return MvcUtil.returnJSON(1, "获取列表异常", null);
    }

    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getById(@PathVariable("id") long id) {
        try {
            Story story = storyService.getDetailByStoryId(id);
            return MvcUtil.returnJSON(0, "success", story);
        } catch (Exception e) {
            logger.error("getById Exception. id:{}", id, e);
        }
        return MvcUtil.returnJSON(1, "获取详情异常", null);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getDetailById(@PathVariable("id") long id, Map<String,Object> map) {
        try {
            Story story = storyService.getDetailByStoryId(id);
            if (story != null) {
                map.put("title", story.getTitle());
                map.put("author", story.getAuthor());
                String dt = String.valueOf(story.getDt()).substring(0, 4) + "/" + String.valueOf(story.getDt()).substring(4, 6) + "/" + String.valueOf(story.getDt()).substring(6, 8);
                map.put("dt", dt);
                map.put("content", story.getContent());
            }
        } catch (Exception e) {
            logger.error("getDetailById Exception. id:{}", id, e);
        }
        return "story_detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public String add(@RequestParam("title") String title, @RequestParam("content") String content,
                      @RequestParam(value = "author") String author) {
        logger.info("add start. title:{} content:{} author:{}", title, content, author);
        try {
            storyService.save(title, author, content);
        } catch (Exception e) {
            logger.error("add Exception. title:{} content:{} author:{}", title, content, author, e);
            return MvcUtil.returnJSON(1, "提交失败", null);
        }
        return MvcUtil.returnSuc();
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length()).toLowerCase();
        if (!"jpg".equalsIgnoreCase(fileType) && !"jpeg".equalsIgnoreCase(fileType) && !"png".equalsIgnoreCase(fileType)) {
            return MvcUtil.returnJSON(1, "文件格式只能是jpg或png", null);
        }
        String fileName = storyService.upload(fileType, file);
        if (!StringUtils.isEmpty(fileName)) {
            JSONObject data = new JSONObject();
            data.put("src", Constants.PIC_URL_PREFIX + fileName);
            return MvcUtil.returnJSON(0, "success", data);
        }
        return MvcUtil.returnErr();
    }

}
