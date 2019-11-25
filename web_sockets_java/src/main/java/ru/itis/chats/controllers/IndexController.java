package ru.itis.chats.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.itis.chats.dto.MessageDto;
import ru.itis.chats.dto.UserDto;
import ru.itis.chats.services.MessageService;
import ru.itis.chats.services.UsersService;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private MessageService messageService;

    @GetMapping("/register")
    @ResponseBody
    public UserDto getIndexPage(@RequestParam("username") String username) {
        return usersService.registerUserByName(username);
    }

    @GetMapping("/getHistory")
    @ResponseBody
    public List<MessageDto> getMessages(){
        return messageService.getAllMessages();
    }

    @GetMapping("/chat")
    public String root(){
        return "index_web_sockets";
    }
}
