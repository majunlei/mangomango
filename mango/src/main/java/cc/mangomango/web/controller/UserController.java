package cc.mangomango.web.controller;

import cc.mangomango.constants.Constants;
import cc.mangomango.domain.User;
import cc.mangomango.service.UserService;
import cc.mangomango.util.MvcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return MvcUtil.returnJSON(1, "用户名或密码不能为空", null);
        }
        try {
            User thisUser = new User();
            thisUser.setUsername(username);
            thisUser.setPassword(password);
            String token = userService.login(thisUser);
            if (StringUtils.isEmpty(token)) {
                return MvcUtil.returnJSON(1, "用户名或密码错误", token);
            }
            String headPhoto = thisUser.getHeadPhoto();
            MvcUtil.setCookieValue(response, "username", username, 30 * 24 * 60 * 60, "/");
            MvcUtil.setCookieValue(response, "token", token, 30 * 24 * 60 * 60, "/");
            if (!StringUtils.isEmpty(headPhoto)) {
                MvcUtil.setCookieValue(response, "headPhoto", Constants.PIC_URL_PREFIX + headPhoto, 30 * 24 * 60 * 60, "/");
            }
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

    @RequestMapping(value = "/set/photo", method = RequestMethod.POST)
    public String setPhoto(@RequestParam("file") MultipartFile file, @CookieValue("username") String username,
                           HttpServletResponse response) {
        if (file == null) {
            return MvcUtil.returnJSON(1, "文件不能为空", null);
        }
        try {
            String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length()).toLowerCase();
            if (!"jpg".equalsIgnoreCase(fileType) && !"jpeg".equalsIgnoreCase(fileType) && !"png".equalsIgnoreCase(fileType)) {
                return MvcUtil.returnJSON(1, "\"文件格式只能是jpg或png\"", null);
            }
            String filePath = userService.setPhoto(fileType, file, username);
            if (!StringUtils.isEmpty(filePath)){
                MvcUtil.setCookieValue(response, "headPhoto", Constants.PIC_URL_PREFIX + filePath, 30 * 24 * 3600 * 3600, "/");
                return MvcUtil.returnSuc();
            }
        } catch (Exception e) {
            logger.error("setPhoto Exception. fileName username:{}", file.getOriginalFilename(), username, e);
        }
        return MvcUtil.returnJSON(1, "添加失败", null);
    }
}
