package org.example.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作日志枚举类，定义了系统中常见的操作类型及其对应的编码和描述。
 * 该枚举类主要用于记录和标识操作日志的具体内容。
 *
 * @author guohao.lu
 */
@Getter
@AllArgsConstructor
public enum OperationLogEnum {
    OTHER("A000000", "未知操作"),
    UPDATE_BASE_INFO("A000001", "更新基础信息"),
    ADD_SKU("A000002", "新增SKU");

    /**
     * 操作编码，唯一标识每种操作类型。
     */
    private final String code;

    /**
     * 操作描述，用于说明操作的具体含义。
     */
    private final String desc;
}
