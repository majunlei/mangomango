package cc.mangomango.web.controller;

import cc.mangomango.domain.Story;
import cc.mangomango.domain.TimeLine;
import cc.mangomango.service.TimeLineService;
import cc.mangomango.util.MvcUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/we")
public class TimeLineController {

    private static Logger logger = LoggerFactory.getLogger(TimeLineController.class);

    @Autowired
    private TimeLineService timeLineService;

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public String getByPage(@RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 10;
        try {
            int offset = (page - 1) * pageSize;
            int count = timeLineService.getCount();
            int pages = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            List<TimeLine> timeLineList = timeLineService.getByPage(offset, pageSize);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 0);
            jsonObject.put("msg", "success");
            jsonObject.put("data", timeLineList);
            jsonObject.put("pages", pages);
            return jsonObject.toJSONString();
        } catch (Exception e) {
            logger.error("getByPage Exception. page:{} pageSize:{}", page, pageSize, e);
        }
        return MvcUtil.returnJSON(1, "获取列表异常", null);
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@RequestParam("date") String date, @RequestParam("description") String description,
                      @CookieValue(value = "username") String username) {
        logger.info("add start. date:{} description:{} username:{}", date, description, username);
        try {
            TimeLine timeLine = new TimeLine();
            timeLine.setDate(date);
            timeLine.setDescription(description);
            timeLineService.save(timeLine);
        } catch (Exception e) {
            logger.error("add Exception. date:{} description:{} username:{}", date, description, username, e);
            return MvcUtil.returnJSON(1, "添加失败", null);
        }
        return MvcUtil.returnSuc();
    }

}
