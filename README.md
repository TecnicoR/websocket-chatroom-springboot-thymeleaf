# Real-Time Chatroom Application with Spring Boot and WebSockets

Welcome to the **Real-Time Chatroom Application** built with **Spring Boot**, **WebSockets**, **STOMP**, and **SockJS**. This project demonstrates how to create a simple chatroom where users can join, send messages, and receive updates in real-time.

---

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
    - [Clone the Repository](#clone-the-repository)
    - [Import the Project](#import-the-project)
    - [Build the Project](#build-the-project)
    - [Run the Application](#run-the-application)
- [Project Structure](#project-structure)
- [Detailed Explanation](#detailed-explanation)
    - [WebSocket Configuration](#websocket-configuration)
    - [Message Model](#message-model)
    - [Controller](#controller)
    - [View (Thymeleaf Template)](#view-thymeleaf-template)
- [Testing the Application](#testing-the-application)
- [Understanding WebSockets, STOMP, and SockJS](#understanding-websockets-stomp-and-sockjs)
    - [What are WebSockets?](#what-are-websockets)
    - [Why Use STOMP?](#why-use-stomp)
    - [Role of SockJS](#role-of-sockjs)

---

## Introduction

This project is a step-by-step implementation of a real-time chatroom application using **WebSockets** in **Spring Boot**. It aims to help you understand how to set up a WebSocket connection, handle messaging between the client and server, and broadcast messages to all connected clients.

---

## Features

- Real-time communication between clients and server.
- Broadcast messages to all connected users.
- User-friendly interface built with Thymeleaf.
- Fallback options for browsers that do not support WebSockets via SockJS.

---

## Technologies Used

- **Java 8 or higher**
- **Spring Boot**
- **WebSockets**
- **STOMP Protocol**
- **SockJS**
- **Thymeleaf**
- **Maven**

---

## Prerequisites

- **Java Development Kit (JDK) 8 or higher**
- **Maven** installed on your machine
- **An IDE** like IntelliJ IDEA, Eclipse, or Spring Tool Suite

---

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/TecnicoR/websocket-chatroom-springboot-thymeleaf.git
```

### Import the Project

- Open your IDE.
- Choose **Import Project** and select the **`pom.xml`** file from the cloned repository.
- Follow the IDE's prompts to import a Maven project.

### Build the Project

```bash
mvn clean install
```

### Run the Application

You can run the application using your IDE or via the command line:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

---

## Project Structure

```
src
├── main
│   ├── java
│   │   └── com.example.chatroom
│   │       ├── ChatController.java
│   │       ├── Message.java
│   │       ├── ViewController.java
│   │       └── WebSocketConfig.java
│   └── resources
│       └── templates
│           └── chat.html
└── test
```

---

## Detailed Explanation

### WebSocket Configuration

**File:** `WebSocketConfig.java`

```java
package com.example.chatroom;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chatroom").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
}
```

**Explanation:**

- `@EnableWebSocketMessageBroker`: Enables WebSocket message handling, backed by a message broker.
- **registerStompEndpoints**: Registers the endpoint `/chatroom` for client connections and enables SockJS as a fallback.
- **configureMessageBroker**: Configures a simple in-memory message broker with destination prefixes.

### Message Model

**File:** `Message.java`

```java
package com.example.chatroom;

public class Message {
    private String from;
    private String text;

    // Getters and Setters
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
```

**Explanation:**

- Represents the structure of messages exchanged between the client and server.
- Contains two fields: `from` (the sender's name) and `text` (the message content).

### Controller

**File:** `ChatController.java`

```java
package com.example.chatroom;

import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message send(Message message) throws Exception {
        // Simulate a delay if needed
        // Thread.sleep(100);
        return message;
    }
}
```

**Explanation:**

- `@MessageMapping("/chat")`: Maps messages sent to `/app/chat` to this method.
- `@SendTo("/topic/messages")`: Broadcasts the returned message to all subscribers of `/topic/messages`.

**File:** `ViewController.java`

```java
package com.example.chatroom;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/chat")
    public String chat() {
        return "chat";
    }
}
```

**Explanation:**

- Serves the `chat.html` template when the user navigates to `/chat`.

### View (Thymeleaf Template)

**File:** `chat.html`

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Spring Boot Chatroom</title>
    <!-- Include SockJS and STOMP.js -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.6.1/sockjs.min.js" integrity="sha512-1QvjE7BtotQjkq8PxLeF6P46gEpBRXuskzIVgjFpekzFVF4yjRgrQvTG1MTOJ3yQgvTteKAcO7DSZI92+u/yZw==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js" integrity="sha512-iKDtgDyTHjAitUDdLljGhenhPwrbBfqTKWO1mkhSFH3A7blITC9MhYon6SjnMhp4o0rADGw9yAC6EW4t5a4K3g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <style>
        /* Basic styling */
        body { font-family: Arial, sans-serif; }
        #chat { max-width: 600px; margin: 0 auto; }
        #messages { border: 1px solid #ccc; height: 300px; overflow-y: scroll; padding: 10px; }
        #message-form { display: flex; margin-top: 10px; }
        #message-form input { flex: 1; padding: 10px; margin-right: 5px; }
        #message-form button { padding: 10px; }
    </style>
</head>
<body>
    <div id="chat">
        <h2>Real-Time Chatroom</h2>
        <div id="messages"></div>
        <div id="message-form">
            <input type="text" id="from" placeholder="Your name" />
            <input type="text" id="text" placeholder="Type a message..." />
            <button onclick="sendMessage()">Send</button>
        </div>
    </div>

    <script>
        var stompClient = null;

        function connect() {
            var socket = new SockJS('/chatroom');
            stompClient = Stomp.over(socket);

            stompClient.connect({}, function (frame) {
                console.log('Connected: ' + frame);

                stompClient.subscribe('/topic/messages', function (messageOutput) {
                    showMessage(JSON.parse(messageOutput.body));
                });
            });
        }

        function sendMessage() {
            var from = document.getElementById('from').value;
            var text = document.getElementById('text').value;

            stompClient.send("/app/chat", {}, JSON.stringify({'from': from, 'text': text}));

            document.getElementById('text').value = '';
        }

        function showMessage(message) {
            var messages = document.getElementById('messages');
            var messageElement = document.createElement('div');

            messageElement.appendChild(document.createTextNode(message.from + ": " + message.text));
            messages.appendChild(messageElement);

            messages.scrollTop = messages.scrollHeight;
        }

        window.onload = connect;
    </script>
</body>
</html>
```

**Explanation:**

- **Libraries**:
    - **SockJS**: Ensures compatibility across browsers by providing a WebSocket-like object.
    - **STOMP.js**: A JavaScript library for STOMP over WebSocket.
- **Functions**:
    - `connect()`: Establishes a connection to the server and subscribes to `/topic/messages`.
    - `sendMessage()`: Sends a message to the server at `/app/chat`.
    - `showMessage()`: Displays incoming messages in the chat window.

---

## Testing the Application

1. **Start the Application**:

   ```bash
   mvn spring-boot:run
   ```

2. **Access the Chatroom**:

   Open your browser and navigate to `http://localhost:8080/chat`.

3. **Open Multiple Clients**:

   Open multiple browser windows or tabs to simulate different users.

4. **Start Chatting**:

    - Enter your name in the "Your name" field.
    - Type a message in the "Type a message..." field.
    - Click **Send**.

   You should see messages appearing in all connected clients in real-time.

---

## Understanding WebSockets, STOMP, and SockJS

### What are WebSockets?

- **WebSockets** provide a persistent, full-duplex communication channel between the client and server over a single TCP connection.
- Allows real-time data transfer without the overhead of HTTP requests.

### Why Use STOMP?

- **STOMP (Simple Text Oriented Messaging Protocol)** is a simple messaging protocol that defines how clients and servers communicate.
- It provides commands like `CONNECT`, `SEND`, and `SUBSCRIBE`, making it easier to implement messaging patterns.
- **Benefits**:
    - Simplifies message routing.
    - Standardizes communication between clients and servers.
    - Easily integrates with Spring's messaging framework.

### Role of SockJS

- **SockJS** is a JavaScript library that provides a WebSocket-like object.
- Ensures compatibility by falling back to alternative protocols if WebSockets aren't supported by the browser.
- Enhances reliability and cross-browser support.

---

## Conclusion

You've successfully built a real-time chatroom application using Spring Boot, WebSockets, STOMP, and SockJS. This project serves as a foundation for creating more complex real-time applications.

**Possible Extensions:**

- **User Authentication**: Implement login functionality to identify users uniquely.
- **Private Messaging**: Allow users to send direct messages to each other.
- **Message History**: Store messages in a database for retrieval upon reconnection.
- **Enhanced UI**: Improve the front-end with better styling and user experience.

---