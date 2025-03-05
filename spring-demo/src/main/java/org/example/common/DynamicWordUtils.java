package org.example.common;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态word
 */
@Slf4j
public final class DynamicWordUtils {
    private static final Map<String, Template> ALL_TEMPLATES;

    static {
        Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_28);
        CONFIGURATION.setDefaultEncoding("utf-8");
        CONFIGURATION.setClassForTemplateLoading(DynamicWordUtils.class, "/freemarker/template");
        ALL_TEMPLATES = new HashMap<>();
        try {
            // 添加所有的模版
            for (FreemarkerTemplateEnum templateEnum : FreemarkerTemplateEnum.values()) {
                ALL_TEMPLATES.put(templateEnum.getName(), CONFIGURATION.getTemplate(templateEnum.getPath()));
            }
        } catch (IOException e) {
            log.error("动态word导出模版获取失败", e);
            throw new RuntimeException(e);
        }
    }

    private DynamicWordUtils() {
        // NOP
    }

    public static File createDoc(FreemarkerTemplateBO templateBO) throws IOException, TemplateException {
        Template template = ALL_TEMPLATES.get(templateBO.getType().getName());
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateBO.getDataModel());

        File outputFile = File.createTempFile(templateBO.getTitle() + "_" + templateBO.getDate(), ".doc");
        try (FileOutputStream fos = new FileOutputStream(outputFile);
             OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
            writer.write(content);
        }
        return outputFile;
    }
}