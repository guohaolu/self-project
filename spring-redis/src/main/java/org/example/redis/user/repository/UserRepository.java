package org.example.redis.user.repository;

import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.example.redis.user.pojo.UserDTO;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamMessageId;
import org.redisson.api.stream.StreamAddArgs;
import org.redisson.api.stream.StreamReadArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * UserRepository
 */
@Repository
public class UserRepository {
    private static final String STREAM_NAME = "queue:user";

    @Autowired
    private RedissonClient redissonClient;

    public void produceMessage(List<UserDTO> dataList) {
        RStream<String, String> stream = redissonClient.getStream(STREAM_NAME);
        List<Map<String, String>> data = dataList.stream().map(this::desc).collect(Collectors.toList());
        data.forEach(map -> {
            StreamAddArgs<String, String> args = StreamAddArgs.entries(map);
            stream.add(args);
        });
    }

    public void consumeMessages() {
        RStream<String, String> stream = redissonClient.getStream(STREAM_NAME);
        StreamMessageId streamMessageId = StreamMessageId.ALL;
        Map<StreamMessageId, Map<String, String>> messages = stream.read(StreamReadArgs. greaterThan(streamMessageId));

        for (Map.Entry<StreamMessageId, Map<String, String>> entry : messages.entrySet()) {
            StreamMessageId id = entry.getKey();
            Map<String, String> msg = entry.getValue();
            // 处理消息
            System.out.println("Received message: " + msg);

            // 可以根据需求确认消息
            stream.ack("myGroup", id);
        }
    }

    @SneakyThrows
    private Map<String, String> desc(Object bean) {
        return BeanUtils.describe(bean);
    }
}
