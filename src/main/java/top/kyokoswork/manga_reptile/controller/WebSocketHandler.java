package top.kyokoswork.manga_reptile.controller;

import org.springframework.web.bind.annotation.RestController;
import top.kyokoswork.manga_reptile.utils.WebSocketUtil;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@RestController
@ServerEndpoint("/websocket/{userName}")
public class WebSocketHandler {

    //建立连接
    @OnOpen
    public void openSession(@PathParam("userName") String userName, Session session) {
        //加入用户名单
        WebSocketUtil.USER_MAP.put(userName, session);
        WebSocketUtil.sendMessage(session,"已连接到服务器");
    }

    @OnMessage
    public void onMessage(@PathParam("userName") String userName, String message) {
        message = userName + ":" + message;
        System.out.println(message);
    }
}
