package ru.itis.chats.handlers;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.util.WebUtils;

import java.util.Map;

@Component
public class AuthHandshakeHandler implements HandshakeHandler {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private DefaultHandshakeHandler handshakeHandler = new DefaultHandshakeHandler();
    @Override
    public boolean doHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws HandshakeFailureException {
        ServletServerHttpRequest request = (ServletServerHttpRequest)serverHttpRequest;
        String cookie = WebUtils.getCookie(request.getServletRequest(), "X-Authorization").getValue();

        String id = Jwts.parser().setSigningKey(jwtSecret)
                .parseClaimsJws(cookie).getBody().getSubject();

        if(id != null){
            return handshakeHandler.doHandshake(serverHttpRequest, serverHttpResponse, webSocketHandler, map);
        } else {
            serverHttpResponse.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }
    }
}
