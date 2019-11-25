package ru.itis.chats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.chats.dto.MessageDto;
import ru.itis.chats.models.Message;
import ru.itis.chats.models.MessageWithUser;
import ru.itis.chats.repositories.MessagesRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessagesRepository messagesRepository;

    public List<MessageDto> getAllMessages(){
        List<MessageDto> messageDtos = new ArrayList<>();

        for(MessageWithUser messageWithUser : messagesRepository.getMessages())
            messageDtos.add(MessageDto.builder()
                    .message(messageWithUser.getMessage())
                    .username(messageWithUser.getUser().getUsername())
                    .id(messageWithUser.getId())
                    .build());
        return messageDtos;
    }

    public void saveMessage(MessageDto messageDto){
        Message message = Message.builder()
                .message(messageDto.getMessage())
                .userId(messageDto.getUserId())
                .build();
        messagesRepository.saveMessage(message);
    }
}
