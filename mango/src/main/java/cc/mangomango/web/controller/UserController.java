package cc.mangomango.web.controller;

import cc.mangomango.domain.User;
import cc.mangomango.service.UserService;
import cc.mangomango.util.MvcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(PicController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
                        HttpServletRequest request, HttpServletResponse response) {
        logger.info("login request start. username:{} password:{} ip:{}", username, password, MvcUtil.getIp(request));
        try {
            String token = userService.login(username, password);
            if (StringUtils.isEmpty(token)) {
                return MvcUtil.returnJSON(1, "用户名或密码错误", token);
            }
            MvcUtil.setCookieValue(response, "username", username, 30 * 24 * 60 * 60, "/");
            MvcUtil.setCookieValue(response, "token", token, 30 * 24 * 60 * 60, "/");
            return MvcUtil.returnJSON(0, "success", token);
        } catch (Exception e) {
            logger.error("login request Exception. username:{} password:{} ip:{}", username, password, MvcUtil.getIp(request), e);
        }
        return MvcUtil.returnJSON(1, "登录失败", null);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(@RequestParam("usernameNew") String usernameNew, @RequestParam("passwordNew") String passwordNew,
                      @CookieValue("username") String currentUsername) {
        logger.info("user add request start. usernameNew:{} passwordNew:{} currentUsername:{}", usernameNew, passwordNew, currentUsername);
        try {
            User user = userService.getByUsername(usernameNew);
            if (user != null) {
                return MvcUtil.returnJSON(1, "用户已存在", null);
            }
            boolean success = userService.addUser(usernameNew, passwordNew);
            if (success) {
                return MvcUtil.returnJSON(0, "添加成功", null);
            }
        } catch (Exception e) {
            logger.error("user add request Exception. usernameNew:{} passwordNew:{} currentUsername:{}", usernameNew, passwordNew, currentUsername, e);
        }
        return MvcUtil.returnJSON(1, "添加失败", null);
    }
}
