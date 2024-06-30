package com.example.spring.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
