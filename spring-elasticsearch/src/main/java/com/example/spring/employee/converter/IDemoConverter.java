package com.example.spring.employee.converter;

import com.example.spring.employee.pojo.dto.Demo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.Map;

/**
 *
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IDemoConverter {
    Demo toDemo(Map<String, String> objectMap);

    Demo toDemo(Demo demo);
}
