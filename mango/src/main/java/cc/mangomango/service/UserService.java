package cc.mangomango.service;

import cc.mangomango.dao.UserMapper;
import cc.mangomango.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

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

    public String login(String username, String password) {
        String passwordMD5 = passwordMD5(password);
        User user = userMapper.getByUsername(username);
        String token = "";
        if (user.getPassword().equals(passwordMD5)) {
            token = UUID.randomUUID().toString();
            userMapper.saveToken(user.getId(), token);
        }
        return token;
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
