package cc.mangomango.cloudadmin.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 作者:majunlei
 * 日期:2017/10/12 上午10:19
 */
@Controller
public class HomeController {

    private static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("")
    public String home() {
        return "home";
    }

    @RequestMapping("status")
    @ResponseBody
    public String status() {
        return "ok";
    }
}
