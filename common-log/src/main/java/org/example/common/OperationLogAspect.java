package org.example.common;

import com.ewayt.erp.admin.api.entity.SysAuditLog;
import com.ewayt.erp.product.util.DataProcessorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * 操作日志切面类，用于拦截带有@OperationLog注解的方法，并记录操作日志。
 * 该类通过AOP机制实现对方法执行前后的日志记录处理。
 *
 * @author guohao.lu
 */
@Aspect
@Slf4j
@Component
public class OperationLogAspect {
    @Autowired
    private DataProcessorUtil dataProcessorUtil;

    @Autowired
    private OperationLogManager operationLogManager;

    @Autowired
    private SpelExpressionEvaluator spelExpressionEvaluator;

    /**
     * 环绕通知方法，用于处理带有@OperationLog注解的方法。
     * 该方法在目标方法执行前后进行日志记录处理，包括初始化上下文、执行目标方法、捕获异常以及保存日志。
     *
     * @param point        切入点对象，提供了关于方法执行的详细信息，例如方法签名、参数等。
     * @param operationLog 注解对象，包含@OperationLog注解的元数据，用于控制日志记录行为。
     * @return 目标方法的执行结果。
     * @throws Throwable 如果目标方法执行过程中抛出异常，则该异常会被重新抛出。
     */
    @SuppressWarnings("unchecked")
    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint point, OperationLog operationLog) throws Throwable {
        Object result;

        try {
            // 确保operationLog注解不为空
            Assert.notNull(operationLog, "OperationLog is null");

            // 更新操作类型到上下文中
            if (operationLog.operationType() != OperationLogEnum.OTHER) {
                OperationLogContextHolder.list().forEach(log -> log.setOperationType(operationLog.operationType()));
            }

            // 如果注解中定义了业务ID，则解析并更新到上下文中
            if (StringUtils.isNotBlank(operationLog.busId())) {
                String busId = spelExpressionEvaluator.evaluate(point, operationLog.busId(), String.class);
                OperationLogContextHolder.list().forEach(log -> log.setBusId(busId));
            }

            // 如果注解中定义了额外参数，则解析并添加到上下文中
            if (StringUtils.isNotBlank(operationLog.extraParams())) {
                Map<String, Object> extraParams = spelExpressionEvaluator.evaluate(point, operationLog.extraParams(), Map.class);
                OperationLogContextHolder.list().forEach(log -> log.setExtraParams(extraParams));
            }

            // 执行目标方法
            result = point.proceed();

            // 如果注解未启用或上下文为空，则跳过日志记录
            if (!operationLog.enable() || CollectionUtils.isEmpty(OperationLogContextHolder.list())) {
                log.warn("OperationLog is not enable or context is null, skip operation log save.");
                return result;
            }

            // 更新上下文为操作成功状态，并生成系统审计日志对象
            OperationLogContextHolder.list().stream().filter(log -> Objects.isNull(log.getOpStatus())).forEach(log -> log.setOpStatus(true));
            List<SysAuditLog> sysAuditLogs = OperationLogConverter.INSTANCE.toSysAuditLog(OperationLogContextHolder.list());

            IntStream.range(0, OperationLogContextHolder.list().size()).forEach(i -> {
                SysAuditLog sysAuditLog = sysAuditLogs.get(i);
                OperationLogContext context = OperationLogContextHolder.list().get(i);
                if (StringUtils.isBlank(sysAuditLog.getLogDetail())) {
                    sysAuditLog.setLogDetail(operationLogManager.extractContent(context));
                }
            });

            // 保存操作日志到数据库
            dataProcessorUtil.saveAuditLog(sysAuditLogs);
        } catch (Throwable throwable) {
            // 捕获异常并更新上下文为操作失败状态
            OperationLogContextHolder.list().stream().filter(log -> Objects.isNull(log.getOpStatus())).forEach(log -> log.setOpStatus(false));
            List<SysAuditLog> sysAuditLogs = OperationLogConverter.INSTANCE.toSysAuditLog(OperationLogContextHolder.list());

            sysAuditLogs.stream().filter(log -> StringUtils.isBlank(log.getLogDetail())).forEach(log -> log.setLogDetail(StringUtils.abbreviate(throwable.getMessage(), 100)));

            // 保存操作日志到数据库
            dataProcessorUtil.saveAuditLog(sysAuditLogs);
            throw throwable;
        } finally {
            // 清理操作日志上下文，避免内存泄漏
            OperationLogContextHolder.clear();
        }
        return result;
    }
}
