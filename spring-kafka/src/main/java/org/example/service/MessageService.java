package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.model.DemoMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class MessageService {
    @Autowired
    private StreamBridge streamBridge;

    /**
     * 发送消息到Kafka
     */
    public boolean sendMessage(String content) {
        DemoMessage message = DemoMessage.builder()
                .id(UUID.randomUUID().toString())
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("发送消息: {}", message);

        // 发送到输出通道
        return streamBridge.send("output-channel", message);
    }
}
