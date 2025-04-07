package org.example.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作日志上下文
 *
 * @author guohao.lu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationLogContext {
    /**
     * 业务ID，表示业务实体的唯一标识，如订单ID、用户ID等。
     */
    private String busId;

    /**
     * 操作类型，表示具体的操作行为，如新增、修改、删除等。
     */
    private OperationLogEnum operationType;

    /**
     * 操作结果，表示操作的结果，如成功、失败等。
     */
    private Boolean opStatus;

    /**
     * 字段变化，表示业务实体的字段变化情况，如字段名称、旧值和新值等。
     */
    @Builder.Default
    private List<FieldChange> fieldChanges = new ArrayList<>();

    /**
     * 额外参数，用于存储操作日志中需要记录的其他自定义参数。
     */
    @Builder.Default
    private Map<String, Object> extraParams = new HashMap<>();

    private String errorMessage;

    /**
     * 操作人，表示执行操作的用户或系统。
     */
    private String operator;

    /**
     * 操作时间，表示操作发生的时间。
     */
    private LocalDateTime operationTime;
}

