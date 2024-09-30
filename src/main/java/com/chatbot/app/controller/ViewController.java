package com.chatbot.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/chat")
    public String chat() {
        // Returns the chat.html template
        return "chat";
    }
}