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
    ADD_BASE_INFO("A000001", "新增基础信息"),
    ADD_SKU("A000002", "新增SKU"),
    ADD_PROCUREMENT_INFO("A000003", "新增采购信息"),
    ADD_VENDOR_QUOTE("A000004", "新增供应商报价"),
    ADD_ASSOCIATED_MATERIAL("A000005", "新增关联辅料"),
    ADD_LOGISTICS_INFO("A000006", "新增物流报关清关"),
    ADD_IMAGE_INFO("A000007", "新增图片信息"),
    ADD_QUALITY_INSPECTION("A000008", "新增质检信息"),
    ADD_MSKU("A000009", "新增MSKU"),
    UPDATE_BASE_INFO("B000001", "更新基础信息"),
    UPDATE_PROCUREMENT_INFO("B000003", "更新采购信息"),
    UPDATE_VENDOR_QUOTE("B000004", "更新供应商报价"),
    UPDATE_ASSOCIATED_MATERIAL("B000005", "更新关联辅料"),
    UPDATE_LOGISTICS_INFO("B000006", "更新物流报关清关"),
    UPDATE_IMAGE_INFO("B000007", "更新图片信息"),
    UPDATE_QUALITY_INSPECTION("B000008", "更新质检信息"),
    UPDATE_MSKU("B000009", "更新MSKU");

    /**
     * 操作编码，唯一标识每种操作类型。
     */
    private final String code;

    /**
     * 操作描述，用于说明操作的具体含义。
     */
    private final String desc;
}
