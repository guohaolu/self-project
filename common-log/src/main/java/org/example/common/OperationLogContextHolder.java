package org.example.common;

import com.ewayt.erp.common.security.util.SecurityUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * OperationLogContextHolder类用于管理操作日志的上下文。
 * 它使用ThreadLocal来存储每个线程独有的操作日志信息，确保线程安全性。
 * 通过这种方式，避免了在多线程环境下共享操作日志信息，从而提高系统性能和线程安全性。
 *
 * @author guohao.lu
 */
public class OperationLogContextHolder {
    // 定义一个ThreadLocal变量来存储OperationLogContext对象，确保每个线程都有自己的操作日志上下文
    private static final ThreadLocal<List<OperationLogContext>> OPERATION_LOG_THREAD_LOCAL = ThreadLocal.withInitial(ArrayList::new);

    /**
     * 获取当前线程的操作日志上下文。
     *
     * @return 当前线程的OperationLogContext对象。如果没有设置，则返回null。
     */
    public static List<OperationLogContext> list() {
        return OPERATION_LOG_THREAD_LOCAL.get();
    }

    public static void addAll(List<OperationLogContext> operationLogList) {
        if (CollectionUtils.isEmpty(operationLogList)) {
            return;
        }
        operationLogList.forEach(log -> {
            if (log.getOperator() == null) {
                log.setOperator(Objects.isNull(SecurityUtils.getUser()) ? "admin" : SecurityUtils.getUser().getUsername());
            }
            if (log.getOperationTime() == null) {
                log.setOperationTime(LocalDateTime.now());
            }
        });
        OPERATION_LOG_THREAD_LOCAL.get().addAll(operationLogList);
    }

    public static void add(OperationLogContext operationLog) {
        if (operationLog == null) {
            return;
        }
        if (operationLog.getOperator() == null) {
            operationLog.setOperator(Objects.isNull(SecurityUtils.getUser()) ? "admin" : SecurityUtils.getUser().getUsername());
        }
        if (operationLog.getOperationTime() == null) {
            operationLog.setOperationTime(LocalDateTime.now());
        }
        OPERATION_LOG_THREAD_LOCAL.get().add(operationLog);
    }

    /**
     * 移除当前线程的操作日志上下文。
     * <p>
     * 这个方法通常在操作日志信息被处理完毕后调用，以避免内存泄漏。
     */
    public static void clear() {
        OPERATION_LOG_THREAD_LOCAL.remove();
    }
}
