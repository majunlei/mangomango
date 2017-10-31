package cc.mangomango.web.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket")
@Component
public class MyWebSocket {

    private static Logger logger = LoggerFactory.getLogger(MyWebSocket.class);

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    @Override
    public String toString() {
        return session.getId();
    }

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        logger.info("当前用户数:{} 用户ID:{}", webSocketSet.size(), webSocketSet);
        cast("当前用户数:" + webSocketSet.size() + " 用户ID:" + webSocketSet.toString());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        cast("当前用户数:" + webSocketSet.size() + " 用户ID:" + webSocketSet.toString());

    }

    /**
     * 收到消息
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("收到消息:{} 发送者sessionId:{}", message, session.getId());
        cast(message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("sessionId:{} 发生错误", session.getId(), error);
    }

    /**
     * 单发消息
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 广播
     * @param message
     * @throws IOException
     */
    public static void cast(String message) {
        for (MyWebSocket myWebSocket : webSocketSet) {
            try {
                myWebSocket.sendMessage(message);
            } catch (IOException e) {
                logger.error("cast IOException. sessionId:{}", myWebSocket.getSession().getId(), e);
            }
        }
    }

    public static Set<MyWebSocket> getWebSocketSet() {
        return webSocketSet;
    }

    public Session getSession() {
        return session;
    }

}