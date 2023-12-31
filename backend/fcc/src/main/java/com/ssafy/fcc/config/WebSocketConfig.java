package com.ssafy.fcc.config;

import com.ssafy.fcc.config.security.JwtTokenProvider;
import com.ssafy.fcc.handler.CamWebSocketHandler;
import com.ssafy.fcc.handler.MyWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final MyWebSocketHandler myWebSocketHandler;
    private final CamWebSocketHandler camWebSocketHandler;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler, "/ws")
                .addInterceptors(new HttpSessionHandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                        try {
                            // URI에서 토큰을 추출
                            String token = request.getURI().getQuery().split("token=")[1];
                            if (jwtTokenProvider.validateToken(token)) {
                                int memberId = Integer.parseInt(jwtTokenProvider.getUserPk(token));
                                attributes.put("memberId", memberId);
                            } else {
                                throw new IllegalArgumentException("Invalid token");
                            }
                        } catch (IllegalArgumentException e) {
                            response.setStatusCode(HttpStatus.UNAUTHORIZED);
                            return false;
                        }
                        return true;
                    }
                })
                .setAllowedOrigins("*");


        registry.addHandler(camWebSocketHandler, "/cam").setAllowedOrigins("*");




    }
}

