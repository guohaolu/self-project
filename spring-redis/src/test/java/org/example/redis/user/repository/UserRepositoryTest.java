package org.example.redis.user.repository;

import org.example.redis.user.pojo.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void produceMessage() {
        UserDTO userDTO1 = new UserDTO();
        userDTO1.setName("张三");
        userDTO1.setAge(18);
        userDTO1.setGender("男");
        userDTO1.setAddress("深圳");
        userDTO1.setPhone("13888888888");
        userDTO1.setEmail("zhangsan@example.com");
        UserDTO userDTO2 = new UserDTO();
        userDTO2.setName("李四");
        userDTO2.setAge(19);
        userDTO2.setGender("女");
        userDTO2.setAddress("广州");
        userDTO2.setPhone("13888888889");
        userDTO2.setEmail("lisi@example.com");
        UserDTO userDTO3 = new UserDTO();
        userDTO3.setName("王五");
        userDTO3.setAge(20);
        userDTO3.setGender("男");
        userDTO3.setAddress("上海");
        userDTO3.setPhone("13888888890");
        userDTO3.setEmail("wangwu@example.com");

        List<UserDTO> userDTOList = Arrays.asList(userDTO1, userDTO2, userDTO3);
        userRepository.produceMessage(userDTOList);
    }

    @Test
    void consumeMessages() {
        userRepository.consumeMessages();
    }
}