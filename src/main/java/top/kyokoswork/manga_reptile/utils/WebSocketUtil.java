package top.kyokoswork.manga_reptile.utils;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketUtil {
    public static final Map<String, Session> USER_MAP = new ConcurrentHashMap<>();

    // 发送消息给客户端
    public static void sendMessage(Session session, String message) {
        if (session != null) {
            final RemoteEndpoint.Basic basic = session.getBasicRemote();
            if (basic != null) {
                try {
                    basic.sendText(message);//发送消息回客户端
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 循环发送
    public static void sendMessageToAll(String message) {
        USER_MAP.forEach((sessionId, session) -> sendMessage(session, message));
    }

    // 指定用户发送
    public static void sendMessageToOne(String user, String message) {
        USER_MAP.forEach((sessionId, session) -> {
            if (sessionId.equals(user)){
                sendMessage(session, message);
            }
        });
    }
}
