package org.example.common;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志管理器
 *
 * @author guohao.lu
 */
@Component
public class OperationLogManager {
    /**
     * 配置FreeMarker模板引擎
     */
    private static final Configuration FREEMARKER_CFG = new Configuration(Configuration.VERSION_2_3_31);

    /**
     * 操作日志模板的名称
     */
    private static final  String TEMPLATE_NAME = "operation-log.ftl";

    // 静态初始化块，用于配置FreeMarker模板引擎的加载路径和默认编码
    static {
        FREEMARKER_CFG.setClassForTemplateLoading(OperationLogContext.class, "/templates");
        FREEMARKER_CFG.setDefaultEncoding("UTF-8");
    }

    /**
     * 根据OperationLogContext上下文提取操作日志的内容
     *
     * @param context 操作日志的上下文，包含日志的相关信息
     * @return 渲染后的操作日志内容字符串
     * @throws RuntimeException 如果在生成操作日志内容过程中发生异常，则抛出运行时异常
     */
    public String extractContent(OperationLogContext context) {
        try {
            // 1. 准备数据模型
            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put("fieldChanges", context.getFieldChanges());
            dataModel.put("operationType", context.getOperationType());
            dataModel.putAll(context.getExtraParams());

            // 2. 获取模板
            Template template = FREEMARKER_CFG.getTemplate(TEMPLATE_NAME);

            // 3. 渲染模板
            StringWriter writer = new StringWriter();
            template.process(dataModel, writer);

            // 移除换行符，确保日志内容为一行
            return StringUtils.replaceChars(writer.toString(), "\r\n", "");
        } catch (Exception e) {
            throw new RuntimeException("生成操作日志内容失败", e);
        }
    }
}

