package org.example.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Freemarker模版业务实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreemarkerTemplateBO {
    private String title;

    private FreemarkerTemplateEnum type;

    @Builder.Default
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

    @Builder.Default
    private Map<String, Object> dataModel = new HashMap<>();
}
