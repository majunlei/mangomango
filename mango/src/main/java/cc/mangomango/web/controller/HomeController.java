package cc.mangomango.web.controller;

import cc.mangomango.util.MvcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 作者:majunlei
 * 日期:2017/10/12 上午10:19
 */
@Controller
public class HomeController {

    private static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("")
    public String index(Map<String,Object> map, HttpServletRequest request) {
        map.put("days", String.valueOf((int) ((System.currentTimeMillis()/1000 - 1483113600)/86400 + 1)));
        String username = MvcUtil.getCookieValue(request, "username");
        map.put("username", username);
        return "index";
    }

    @RequestMapping("editor")
    public String editor() {
        return "editor";
    }

    @RequestMapping("adduser")
    public String adduser(Map<String,Object> map, HttpServletRequest request) {
        String username = MvcUtil.getCookieValue(request, "username");
        map.put("username", username);
        return "adduser";
    }

    @RequestMapping("newxq")
    public String newXq() {
        return "newxq";
    }

    @RequestMapping("status")
    @ResponseBody
    public String status() {
        return "ok";
    }

    @RequestMapping("devtest")
    public String justDevTest() {
        return "devtest";
    }

}
