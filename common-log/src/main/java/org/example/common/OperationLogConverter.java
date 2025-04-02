package org.example.common;

import com.ewayt.erp.admin.api.entity.SysAuditLog;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 操作日志类型转换
 *
 * @author guohao.lu
 */
@Mapper
public interface OperationLogConverter {
    OperationLogConverter INSTANCE = Mappers.getMapper(OperationLogConverter.class);

    /**
     * 更新日志上下文实体
     *
     * @param oldValue 旧值
     * @param newValue 新值
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "fieldChanges", ignore = true)
    @Mapping(target = "extraParams", ignore = true)
    void update(@MappingTarget OperationLogContext oldValue, OperationLogContext newValue);

    /**
     * 操作日志上下文实体转换成系统审计日志实体
     *
     * @param operationLogContext 操作日志上下文实体
     * @return 系统审计日志实体
     */
    @Mapping(source = "operationType.code", target = "auditType")
    @Mapping(source = "operationType.desc", target = "auditTypeName")
    @Mapping(source = "busId", target = "busId") // 未变化，因为 source 和 target 相同
    @Mapping(source = "operationType.code", target = "busType")
    @Mapping(source = "operationType.desc", target = "busTypeName")
    @Mapping(source = "opStatus", target = "opStatus", qualifiedByName = "booleanToString")
    @Mapping(source = "operator", target = "createBy")
    @Mapping(source = "operationTime", target = "createTime")
    @Mapping(source = "operationType.desc", target = "auditName")
    SysAuditLog toSysAuditLog(OperationLogContext operationLogContext);

    List<SysAuditLog> toSysAuditLog(List<OperationLogContext> operationLogContext);


    /**
     * 自定义映射方法：将 Boolean 转换为字符串（"0" 表示 false，"1" 表示 true）。
     */
    @Named("booleanToString")
    default String booleanToString(Boolean value) {
        return value != null && value ? "1" : "0";
    }
}
