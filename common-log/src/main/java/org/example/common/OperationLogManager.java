package org.example.common;

import com.ewayt.erp.admin.api.entity.SysAuditLog;
import com.ewayt.erp.common.security.util.SecurityUtils;
import com.ewayt.erp.product.util.DataProcessorUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

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
    private static final String TEMPLATE_NAME = "operation-log.ftl";

    @Autowired
    private DataProcessorUtil dataProcessorUtil;

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

    /**
     * 处理并持久化操作日志上下文内容
     * <p>
     * 方法流程：
     * 1. 将上下文中的操作日志转换为系统审计日志对象
     * 2. 遍历处理每个日志条目：
     * - 当审计日志详情和错误信息同时为空时，抽取上下文内容填充详情
     * - 当存在错误信息时，优先将错误信息设置为日志详情
     * 3. 批量保存处理后的审计日志
     * 4. 重置操作日志上下文
     */
    public void consumerContext() {
        Assert.noNullElements(OperationLogContextHolder.list().stream().map(OperationLogContext::getOpStatus).toList(), "操作日志上下文缺失状态");
        // 将操作日志上下文转换为系统审计日志对象列表
        List<SysAuditLog> sysAuditLogs = OperationLogConverter.INSTANCE.toSysAuditLog(OperationLogContextHolder.list());

        // 处理每个日志条目内容
        IntStream.range(0, OperationLogContextHolder.list().size()).forEach(i -> {
            SysAuditLog sysAuditLog = sysAuditLogs.get(i);
            OperationLogContext context = OperationLogContextHolder.list().get(i);

            // 处理日志详情内容逻辑：
            // 当审计日志和上下文都无详情时，抽取上下文内容
            // 存在错误信息时优先使用错误信息覆盖
            if (StringUtils.isBlank(sysAuditLog.getLogDetail()) && StringUtils.isBlank(context.getErrorMessage())) {
                sysAuditLog.setLogDetail(extractContent(context));
            }
            if (StringUtils.isNotBlank(context.getErrorMessage())) {
                sysAuditLog.setLogDetail(context.getErrorMessage());
            }
        });

        // 批量持久化审计日志记录
        dataProcessorUtil.saveAuditLog(sysAuditLogs);

        // 重置日志上下文避免数据重复
        OperationLogContextHolder.reset();
    }

    /**
     * 异步处理操作日志上下文
     *
     * @param context 操作日志上下文
     */
    @Async("securityTaskExecutor")
    public void consumerContext(OperationLogContext context) {
        // 检查上下文是否为空，为空则直接返回
        if (context == null) {
            return;
        }

        // 确保操作日志上下文的状态不为空
        Assert.notNull(context.getOpStatus(), "操作日志上下文缺失状态");

        context.setOperator(Objects.isNull(SecurityUtils.getUser()) ? "admin" : SecurityUtils.getUser().getUsername());
        context.setOperationTime(LocalDateTime.now());
        // 将操作日志上下文转换为系统审计日志对象
        SysAuditLog sysAuditLog = OperationLogConverter.INSTANCE.toSysAuditLog(context);

        // 处理日志详情内容逻辑：
        // 当审计日志和上下文都无详情时，抽取上下文内容
        // 存在错误信息时优先使用错误信息覆盖
        if (StringUtils.isBlank(sysAuditLog.getLogDetail()) && StringUtils.isBlank(context.getErrorMessage())) {
            sysAuditLog.setLogDetail(extractContent(context));
            if (StringUtils.isBlank(sysAuditLog.getLogDetail())) {
                return;
            }
        }
        if (StringUtils.isNotBlank(context.getErrorMessage())) {
            sysAuditLog.setOpStatus("0");
            sysAuditLog.setLogDetail(context.getErrorMessage());
        }

        // 批量持久化审计日志记录
        dataProcessorUtil.saveAuditLog(Collections.singletonList(sysAuditLog));
    }
}

