package org.example.common;

import cn.hutool.core.map.MapUtil;
import com.ewayt.erp.product.dto.sku.ProductSkuDto;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 操作日志提取管理器
 *
 * @author guohao.lu
 */
@Component
public class OperationLogExtractManager {
    /**
     * 提取产品SKU创建日志
     * 该方法用于记录产品SKU创建过程中的操作日志，包括SKU新增、基础信息新增和采购信息新增
     *
     * @param productSkuDto 产品SKU数据传输对象，包含SKU的基本信息和采购信息
     * @return 返回一个操作日志上下文列表，每个上下文代表一个特定操作的日志信息
     */
    public List<OperationLogContext> extractSkuCreateLog(ProductSkuDto productSkuDto) {
        // 1. 新增SKU
        OperationLogContext skuContext = OperationLogContext.builder()
                .operationType(OperationLogEnum.ADD_SKU)
                .extraParams(MapUtil.of("skuName", productSkuDto.getBasic().getSkuName()))
                .build();
        // 2. 新增基础信息
        OperationLogContext basicContext = OperationLogContext.builder()
                .operationType(OperationLogEnum.ADD_BASE_INFO)
                .fieldChanges(FieldChangeUtil.extract(productSkuDto.getBasic()))
                .build();
        // 3. 新增采购信息
        OperationLogContext purchaseContext = wrapContext(OperationLogEnum.ADD_PROCUREMENT_INFO, ImmutableMap.of(
                "basic", productSkuDto.getPurchaseInfo().getBasicInfo(),
                "spec", productSkuDto.getPurchaseInfo().getSpecInfo(),
                "specPack", ListUtils.emptyIfNull(productSkuDto.getPurchaseInfo().getSpecInfo().getSpecPackList())));
        // 4. 新增供应商报价信息
        OperationLogContext supplierContext = wrapContext(OperationLogEnum.ADD_VENDOR_QUOTE, ImmutableMap.of(
                "supplierQuoteList", ListUtils.emptyIfNull(productSkuDto.getSupplierQuotes())));
        // 5. 新增关联辅料信息
        OperationLogContext auxRelationContext = wrapContext(OperationLogEnum.ADD_ASSOCIATED_MATERIAL, ImmutableMap.of(
                "auxRelationList", ListUtils.emptyIfNull(productSkuDto.getAuxRelationList())));
        // 6. 新增物流报关清关
        OperationLogContext logisticsContext = wrapContext(OperationLogEnum.ADD_LOGISTICS_INFO, ImmutableMap.of(
                "logistics", productSkuDto.getLogistics()));
        // 7. 新增图片信息
        OperationLogContext picContext = wrapContext(OperationLogEnum.ADD_IMAGE_INFO, ImmutableMap.of(
                "picList", ListUtils.emptyIfNull(productSkuDto.getPicList())));
        // 8. 新增质检信息
        OperationLogContext qcStandardContext = wrapContext(OperationLogEnum.ADD_QUALITY_INSPECTION, ImmutableMap.of(
                "qcStandard", productSkuDto.getQcStandard()));
        // 9. 新增msku信息
        OperationLogContext mskuContext = wrapContext(OperationLogEnum.ADD_MSKU, ImmutableMap.of(
                "mskuList", ListUtils.emptyIfNull(productSkuDto.getMskuList())));
        // 将所有操作日志上下文收集到一个列表中并返回
        return Stream.of(skuContext, basicContext, purchaseContext, supplierContext, auxRelationContext, logisticsContext, picContext, qcStandardContext, mskuContext)
                .filter(Objects::nonNull).toList();
    }

    public List<OperationLogContext> extractSkuUpdateLog(ProductSkuDto oldDto, ProductSkuDto newDto) {
        // 2. 更新基础信息
        OperationLogContext basicContext = wrapContext(OperationLogEnum.UPDATE_BASE_INFO, ImmutableMap.of(
                "value", JaversObjectComparator.extractChanges(oldDto, newDto, ProductSkuDto::getBasic)));
        // 3. 新增采购信息
        OperationLogContext purchaseContext = wrapContext(OperationLogEnum.UPDATE_PROCUREMENT_INFO, ImmutableMap.of(
                "value", JaversObjectComparator.extractChanges(oldDto, newDto, ProductSkuDto::getPurchaseInfo)));
        // 4. 新增供应商报价信息
        OperationLogContext supplierContext = wrapContext(OperationLogEnum.UPDATE_VENDOR_QUOTE, ImmutableMap.of(
                "value", JaversObjectComparator.extractChanges(oldDto, newDto, ProductSkuDto::getSupplierQuotes)));
        // 5. 新增关联辅料信息
        OperationLogContext auxRelationContext = wrapContext(OperationLogEnum.UPDATE_ASSOCIATED_MATERIAL, ImmutableMap.of(
                "value", JaversObjectComparator.extractChanges(oldDto, newDto, ProductSkuDto::getAuxRelationList)));
        // 6. 新增物流报关清关
        OperationLogContext logisticsContext = wrapContext(OperationLogEnum.UPDATE_LOGISTICS_INFO, ImmutableMap.of(
                "value", JaversObjectComparator.extractChanges(oldDto, newDto, ProductSkuDto::getLogistics)));
        // 7. 新增图片信息
        OperationLogContext picContext = wrapContext(OperationLogEnum.UPDATE_IMAGE_INFO, ImmutableMap.of(
                "value", JaversObjectComparator.extractChanges(oldDto, newDto, ProductSkuDto::getPicList)));
        // 8. 新增质检信息
        OperationLogContext qcStandardContext = wrapContext(OperationLogEnum.UPDATE_QUALITY_INSPECTION, ImmutableMap.of(
                "value", JaversObjectComparator.extractChanges(oldDto, newDto, ProductSkuDto::getQcStandard)));
        // 9. 新增msku信息
        OperationLogContext mskuContext = wrapContext(OperationLogEnum.UPDATE_MSKU, ImmutableMap.of(
                "value", JaversObjectComparator.extractChanges(oldDto, newDto, ProductSkuDto::getMskuList)));
        // 将所有操作日志上下文收集到一个列表中并返回
        return Stream.of(basicContext, purchaseContext, supplierContext, auxRelationContext, logisticsContext, picContext, qcStandardContext, mskuContext)
                .filter(Objects::nonNull).toList();
    }


    /**
     * 包装操作日志上下文
     * 此方法用于根据提供的操作日志枚举和额外参数创建一个操作日志上下文对象
     * 它首先检查额外参数映射是否所有值均为空，如果是，则返回null，表示无需创建上下文
     * 这是因为空的额外参数映射没有提供任何有用的上下文信息
     * 如果额外参数映射中至少有一个非空值，则构建并返回一个包含这些信息的操作日志上下文对象
     *
     * @param operationLogEnum 操作日志的枚举类型，代表某种特定的操作或事件
     * @param extraParams      与操作日志相关的额外参数，以键值对形式提供
     * @return 如果额外参数映射为空（即所有值均为空），则返回null；否则返回构建的操作日志上下文对象
     */
    @Nullable
    private OperationLogContext wrapContext(OperationLogEnum operationLogEnum, Map<String, Object> extraParams) {
        // 检查额外参数映射是否所有值均为空，如果是，则返回null
        if (MapUtils.emptyIfNull(extraParams).values().stream().allMatch(ObjectUtils::isEmpty)) {
            return null;
        }
        // 构建并返回操作日志上下文对象
        return OperationLogContext.builder()
                .operationType(operationLogEnum)
                .extraParams(extraParams)
                .build();
    }
}
