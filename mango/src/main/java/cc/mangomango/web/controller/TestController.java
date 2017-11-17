package cc.mangomango.web.controller;

import cc.mangomango.web.websocket.MyWebSocket;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;

@RequestMapping("test")
@Controller
public class TestController {

    @RequestMapping("ws")
    public String test() {
        return "test/ws";
    }

    @RequestMapping(value = "ws/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String testWs(@PathVariable("id") String id, @RequestParam("msg") String msg) throws IOException {
        Set<MyWebSocket> myWebSocketSet = MyWebSocket.getWebSocketSet();
        for (MyWebSocket myWebSocket : myWebSocketSet) {
            if (myWebSocket.getSession().getId().equals(id)) {
                myWebSocket.sendMessage(msg);
                return "发送成功";
            }
        }
        return "无此用户，发送失败";
    }
}
