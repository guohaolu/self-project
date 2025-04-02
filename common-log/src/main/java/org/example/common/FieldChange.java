package org.example.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 字段变更记录
 *
 * @author guohao.lu
 */
@Data
@AllArgsConstructor
public class FieldChange {
    /**
     * 字段的名称，表示发生变更的字段。
     */
    private String fieldName;

    /**
     * 字段的旧值，表示变更前的值。
     */
    private String oldValue;

    /**
     * 字段的新值，表示变更后的值。
     */
    private String newValue;
}

