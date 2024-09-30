package com.chatbot.app.controller;

import com.chatbot.app.dto.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message send(Message message) throws Exception {
        // Simulate a delay of 100ms
        Thread.sleep(100);
        return message;
    }
}