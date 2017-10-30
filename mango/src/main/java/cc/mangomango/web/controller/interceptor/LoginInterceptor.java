package cc.mangomango.web.controller.interceptor;

import cc.mangomango.service.UserService;
import cc.mangomango.util.MvcUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String username = MvcUtil.getCookieValue(request, "username");
        String token = MvcUtil.getCookieValue(request, "token");
        boolean checkToken = userService.checkToken(username, token);
        if (!checkToken) {
            MvcUtil.delCookie(response, "username");
            MvcUtil.delCookie(response, "token");

            if (MvcUtil.isAjax(request)) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                PrintWriter printWriter = response.getWriter();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", 1);
                jsonObject.put("msg", "对不起，没有权限");
                printWriter.write(jsonObject.toJSONString());
                printWriter.flush();
            } else {
                response.sendRedirect("/");
            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }
}
