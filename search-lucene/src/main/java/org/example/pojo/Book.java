package org.example.pojo;

import lombok.Data;

/**
 * 书本
 */
@Data
public class Book {
    private Integer id;
    private String name;
    private Float price;
    private String desc;
}
