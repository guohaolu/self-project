package org.example.redis.user.pojo;

import lombok.Data;

/**
 * UserDTO
 */
@Data
public class UserDTO {
    private String name;
    private Integer age;
    private String gender;
    private String address;
    private String phone;
    private String email;
}
