package top.kyokoswork.manga_reptile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@SpringBootApplication
public class MangaReptileApplication {

    public static void main(String[] args) {
        SpringApplication.run(MangaReptileApplication.class, args);
    }

    @Bean    //在容器中创建bean对象，在WebSocketUtil中需要用到的RemoteEndpoint对象
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
