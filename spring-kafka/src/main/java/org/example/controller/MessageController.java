package org.example.controller;

import org.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping
    public String sendMessage(@RequestBody String content) {
        boolean result = messageService.sendMessage(content);
        return result ? "消息发送成功!" : "消息发送失败!";
    }
}
