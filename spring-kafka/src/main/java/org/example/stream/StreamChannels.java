package org.example.stream;

import org.example.model.DemoMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

/**
 *
 *
 * @author guohao.lu
 */
@Configuration
public class StreamChannels {

    @Bean
    public Consumer<Message<DemoMessage>> inputChannel() {
        return message -> {
            DemoMessage payload = message.getPayload();
            System.out.println("收到消息: " + payload);
        };
    }

    // 注意：实际应用中，通常不会定义Supplier Bean，而是通过StreamBridge发送消息
}
