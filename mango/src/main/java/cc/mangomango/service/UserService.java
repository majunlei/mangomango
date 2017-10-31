package cc.mangomango.service;

import cc.mangomango.constants.Constants;
import cc.mangomango.dao.UserMapper;
import cc.mangomango.domain.User;
import cc.mangomango.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Created by majunlei on 2017/10/30.
 */
@Service
public class UserService {

    private static final String SALT = "ohUJx8UP1XGnSKn6";

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    public String login(User thisUser) {
        String passwordMD5 = passwordMD5(thisUser.getPassword());
        User user = userMapper.getByUsername(thisUser.getUsername());
        String token = "";
        if (user != null && passwordMD5.equals(user.getPassword())) {
            token = UUID.randomUUID().toString();
            userMapper.saveToken(user.getId(), token);
            thisUser.setHeadPhoto(user.getHeadPhoto());
        }
        return token;
    }

    public String setPhoto(String fileType, MultipartFile file, String username) {
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        fileName = fileName + "." + fileType;
        try {
            boolean success = ImageUtil.uploadHeadPhoto(file, Constants.PIC_DIR + fileName);
            if (!success) {
                return "";
            }
            userMapper.updateHeadPhoto(fileName, username);
        } catch (Exception e) {
            logger.error("保存头像发生错误. fileType:{} fileName:{}", fileType, fileName, e);
        }
        return fileName;
    }

    public User getByUsername(String username) {
        return userMapper.getByUsername(username);
    }

    public boolean addUser(String username, String password) {
        String passwordMD5 = passwordMD5(password);
        try {
            userMapper.save(username, passwordMD5);
            return true;
        } catch (Exception e) {
            logger.error("userMapper.save Exception. username:{} password:{}", username, password, e);
            return false;
        }
    }

    public boolean checkToken(String username, String token) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(token)) {
            return false;
        }
        int count = userMapper.countToken(username, token);
        return count > 0;
    }

    public String passwordMD5(String password) {
        return DigestUtils.md5DigestAsHex((password + SALT).getBytes());
    }

}
