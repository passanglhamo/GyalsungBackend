package com.microservice.erp.config;

import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.HttpHandler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Redirect HTTP requests to HTTPS
 *
 * Now that we have enabled HTTPS in our Spring Boot application and blocked any HTTP request, we want to redirect all traffic to HTTPS.
 * Spring allows defining just one network connector in application.properties (or application.yml).
 * Since we have used it for HTTPS, we have to set the HTTP connector programmatically for our Tomcat web server.
 */

@Configuration
public class ServerConfig {

    private Environment env;
    private HttpHandler httpHandler;
    private WebServer webServer;

    public ServerConfig(Environment env, HttpHandler httpHandler) {
        this.env = env;
        this.httpHandler = httpHandler;
    }

//    @PostConstruct
//    public void start() {
//        int httpPort = Integer.valueOf(env.getProperty("server.http.port"));
//        ReactiveWebServerFactory factory = new NettyReactiveWebServerFactory(httpPort);
//        webServer = factory.getWebServer(httpHandler);
//        webServer.start();
//    }

    @PreDestroy
    public void stop() {
        webServer.stop();
    }
}
