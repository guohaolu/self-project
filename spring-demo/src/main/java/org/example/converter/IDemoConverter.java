package org.example.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.Map;

/**
 *
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IDemoConverter {
    Demo toDemo(Map<String, Object> objectMap);
}
