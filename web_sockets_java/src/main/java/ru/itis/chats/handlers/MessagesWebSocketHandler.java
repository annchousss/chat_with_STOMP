package ru.itis.chats.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.itis.chats.dto.MessageDto;
import ru.itis.chats.services.MessageService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessagesWebSocketHandler extends TextWebSocketHandler {


    private static Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MessageService messageService;

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        HttpHeaders headers = session.getHandshakeHeaders();
        String messageAsString = (String) message.getPayload();
        MessageDto body = objectMapper.readValue(messageAsString, MessageDto.class);

        if (body.getMessage().equals("Hello!")) {
            sessions.put(body.getUsername(), session);
        }

        messageService.saveMessage(body);

        for (WebSocketSession currentSession : sessions.values()) {

            currentSession.sendMessage(new TextMessage(messageAsString)); // to all clients
        }
    }
}
