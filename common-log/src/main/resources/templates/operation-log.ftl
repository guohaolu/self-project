<#-- 未知操作 -->
<#compress>
    <#if operationType?? && operationType.code == 'A000000'>
        未知操作
    </#if>
</#compress>

<#------------------------------------------------------------------------ 新增基础信息 ------------------------------------------------------------------------>
<#assign A000001Fields = ["categoryName", "brandName", "skuName", "skuUnit", "skuStatusName"]>
<#compress>
    <#if operationType?? && operationType.code == 'A000001'>
        <#assign filteredChanges = fieldChanges?filter(change ->
        A000001Fields?seq_contains(change.fieldName) &&
        change.newValue?has_content &&
        change.newValue != "空"
        )>
        <#if filteredChanges?size gt 0>
            新增基础信息：
            <#list filteredChanges as change>
                ${getFieldLabel(change.fieldName)}【${change.newValue!"空"}】<#sep>，</#sep>
            </#list>
        </#if>
    </#if>
</#compress>

<#-- 新增SKU -->
<#compress>
    <#if operationType?? && operationType.code == 'A000002'>
        新增SKU【${skuName!"空"}】成功。
    </#if>
</#compress>

<#------------------------------------------------------------------------ 新增采购信息 ------------------------------------------------------------------------>
<#compress>
    <#if operationType?? && operationType.code == 'A000003'>
    <#-- 处理基础信息 -->
        <#assign A000003basicFields = {
        "purchasePersonName": "采购人",
        "purchaseDelivery": "采购周期（天）",
        "purchaseRemark": "采购备注",
        "purchasePrice": "采购成本"
        }>
        <#assign basicInfo = basic!{}>
        <#assign basicContent = []>

        <#list A000003basicFields?keys as field>
            <#assign label = A000003basicFields[field]>
            <#if basicInfo[field]?? && basicInfo[field]?has_content && basicInfo[field]?string != "空">
                <#assign basicContent = basicContent + ["${label}【${basicInfo[field]?string}】"]>
            </#if>
        </#list>

    <#-- 处理规格信息（如果有的话） -->
        <#assign A000003specFields = {
        "specSingleWeight": "单品净重（kg）",
        "specMaterial": "产品材质"
        }>
        <#assign specInfo = spec!{}>
        <#assign specContent = []>
        <#list A000003specFields?keys as field>
            <#assign label = A000003specFields[field]>
            <#if specInfo[field]?? && specInfo[field]?has_content && specInfo[field]?string != "空">
                <#assign specContent = specContent + ["${label}【${specInfo[field]?string}】"]>
            </#if>
        </#list>
    <#-- 处理规格信息，长宽高 -->
        <#if specInfo??>
            <#if specInfo.specSingleLength?? || specInfo.specSingleLength?has_content || specInfo.specSingleWidth?? || specInfo.specSingleWidth?has_content || specInfo.specSingleHeight?? || specInfo.specSingleHeight?has_content>
                <#assign specContent = specContent + ["单品规格【${specInfo.specSingleLength?string}**${specInfo.specSingleWidth?string}**${specInfo.specSingleHeight?string} cm】"]>
            </#if>
        </#if>

    <#-- 处理规格包装列表（如果有的话） -->
        <#assign specPackList = specPack![]>
        <#assign packContent = []>
        <#if specPackList?? && specPackList?size gt 0>
            <#list specPackList as pack>
                <#assign packItem = []>
                <#if pack.specBoxTitle?? && pack.specBoxTitle?has_content>
                    <#assign packItem = packItem + ["箱规名称【${pack.specBoxTitle?string}】"]>
                </#if>
                <#if pack.boxPcs?? && pack.boxPcs?has_content>
                    <#assign packItem = packItem + ["单箱数量【${pack.boxPcs?string}】"]>
                </#if>
            <#-- 处理外箱规格（合并长宽高） -->
                <#if (pack.specBoxLength?? && pack.specBoxLength?has_content)
                || (pack.specBoxWidth?? && pack.specBoxWidth?has_content)
                || (pack.specBoxHeight?? && pack.specBoxHeight?has_content)>
                    <#assign boxLength = pack.specBoxLength!0>
                    <#assign boxWidth = pack.specBoxWidth!0>
                    <#assign boxHeight = pack.specBoxHeight!0>
                    <#assign packItem = packItem + ["外箱规格【${boxLength}**${boxWidth}**${boxHeight} cm】"]>
                </#if>
            <#-- 合并包装规格（长**宽**高） -->
                <#if (pack.packageLength?? && pack.packageLength?has_content)
                || (pack.packageWidth?? && pack.packageWidth?has_content)
                || (pack.packageHeight?? && pack.packageHeight?has_content)>
                    <#assign pkgLength = pack.packageLength!0>
                    <#assign pkgWidth = pack.packageWidth!0>
                    <#assign pkgHeight = pack.packageHeight!0>
                    <#assign packItem = packItem + ["包装规格【${pkgLength}**${pkgWidth}**${pkgHeight} cm】"]>
                </#if>
                <#if pack.singleBoxWeight?? && pack.singleBoxWeight?has_content>
                    <#assign packItem = packItem + ["单箱重量【${pack.singleBoxWeight?string} kg】"]>
                </#if>
                <#if pack.singlePackWeight?? && pack.singlePackWeight?has_content>
                    <#assign packItem = packItem + ["单品毛重【${pack.singlePackWeight?string} kg】"]>
                </#if>

                <#if packItem?size gt 0>
                    <#assign packContent = packContent + ["第${pack?index + 1}箱信息：" + packItem?join("，")]>
                </#if>
            </#list>
        </#if>

    <#-- 组合所有内容 -->
        <#assign allContent = []>
        <#if basicContent?size gt 0>
            <#assign allContent = allContent + ["采购基础信息：" + basicContent?join("，")]>
        </#if>
        <#if specContent?size gt 0>
            <#assign allContent = allContent + ["采购规格信息：" + specContent?join("，")]>
        </#if>
        <#if packContent?size gt 0>
            <#assign allContent = allContent + ["包装规格信息：" + packContent?join("；")]>
        </#if>

        <#if allContent?size gt 0>
            ${allContent?join("；")}
        </#if>
    </#if>
</#compress>

<#------------------------------------------------------------------------ 新增供应商报价 ------------------------------------------------------------------------>
<#assign SUPPLIER_FIELDS = {
"supplierName": "供应商名称",
"supplierCode": "供应商代码",
"purchaseLink": "采购链接",
"quotesRemark": "报价备注"
}>

<#assign QUOTE_FIELDS = {
"currencyCode": "货币类型",
"containTax": "是否含税",
"taxRate": "税率（%）"
}>

<#compress>
    <#if operationType?? && operationType.code == 'A000004'>
        <#assign supplierQuoteList = supplierQuoteList![]>
        <#assign supplierContent = []>

        <#if supplierQuoteList?? && supplierQuoteList?size gt 0>
            <#list supplierQuoteList as supplier>
                <#assign supplierItem = []>

            <#-- 处理供应商基本信息 -->
                <#list SUPPLIER_FIELDS?keys as field>
                    <#assign label = SUPPLIER_FIELDS[field]>
                    <#if supplier[field]?? && supplier[field]?has_content && supplier[field]?string != "空">
                        <#if field == "containTax">
                            <#assign value = (supplier[field] == "1")?string("是", "否")>
                            <#assign supplierItem = supplierItem + ["${label}【${value}】"]>
                        <#else>
                            <#assign supplierItem = supplierItem + ["${label}【${supplier[field]?string}】"]>
                        </#if>
                    </#if>
                </#list>

            <#-- 处理报价信息 -->
                <#if supplier.quotes?? && supplier.quotes?size gt 0>
                    <#assign quoteContent = []>
                    <#list supplier.quotes as quote>
                        <#assign quoteItem = []>

                    <#-- 处理报价基本信息 -->
                        <#list QUOTE_FIELDS?keys as field>
                            <#assign label = QUOTE_FIELDS[field]>
                            <#if quote[field]?? && quote[field]?has_content && quote[field]?string != "空">
                                <#if field == "containTax">
                                    <#assign value = (quote[field] == "1")?string("是", "否")>
                                    <#assign quoteItem = quoteItem + ["${label}【${value}】"]>
                                <#else>
                                    <#assign quoteItem = quoteItem + ["${label}【${quote[field]?string}】"]>
                                </#if>
                            </#if>
                        </#list>

                    <#-- 处理阶梯价格 -->
                        <#if quote.stepPrices?? && quote.stepPrices?size gt 0>
                            <#assign priceContent = []>
                            <#list quote.stepPrices as step>
                                <#if step.minQty?? && step.price??>
                                    <#assign priceContent = priceContent + ["≥${step.minQty}件:${step.price}${quote.currencyCode!''}"]>
                                </#if>
                            </#list>
                            <#if priceContent?size gt 0>
                                <#assign quoteItem = quoteItem + ["阶梯价格:${priceContent?join('，')}"]>
                            </#if>
                        </#if>

                        <#if quoteItem?size gt 0>
                            <#assign quoteContent = quoteContent + ["报价${quote?index + 1}:${quoteItem?join('，')}"]>
                        </#if>
                    </#list>

                    <#if quoteContent?size gt 0>
                        <#assign supplierItem = supplierItem + ["报价明细:${quoteContent?join('；')}"]>
                    </#if>
                </#if>

                <#if supplierItem?size gt 0>
                    <#assign supplierContent = supplierContent + ["供应商${supplier?index + 1}:${supplierItem?join('，')}"]>
                </#if>
            </#list>

            <#if supplierContent?size gt 0>
                新增供应商报价信息：
                ${supplierContent?join("；")}
            </#if>
        </#if>
    </#if>
</#compress>

<#------------------------------------------------------------------------ 新增关联辅料 ------------------------------------------------------------------------>
<#-- 新增关联辅料信息 -->
<#compress>
    <#if operationType?? && operationType.code == 'A000005'>
    <#-- 安全处理可能为null的辅料列表 -->
        <#assign auxRelationList = auxRelationList![]>

    <#-- 只有当辅料列表不为空时才处理 -->
        <#if auxRelationList?size gt 0>
            <#assign auxContent = []>

            <#list auxRelationList as aux>
                <#if aux??>  <#-- 防止单个辅料对象为null -->
                    <#assign auxItem = []>

                <#-- 处理辅料基本信息 -->
                    <#if aux.auxName?? && aux.auxName?has_content>
                        <#assign auxItem = auxItem + ["辅料名称【${aux.auxName}】"]>
                    </#if>

                    <#if aux.auxSku?? && aux.auxSku?has_content>
                        <#assign auxItem = auxItem + ["SKU编码【${aux.auxSku}】"]>
                    </#if>

                <#-- 处理数量（必填字段） -->
                    <#assign auxItem = auxItem + ["数量【${aux.quantity!0}】"]>

                <#-- 处理单位成本 -->
                    <#if aux.auxUnitCost??>
                        <#assign numberFormat = ",##0.00">
                        <#assign auxItem = auxItem + ["单位成本【${aux.auxUnitCost?string(numberFormat)}】"]>
                    <#else>
                        <#assign auxItem = auxItem + ["单位成本【未设置】"]>
                    </#if>

                <#-- 处理备注 -->
                    <#if aux.remark?? && aux.remark?has_content>
                        <#assign cleanedRemark = aux.remark?replace("\n", " ")?replace("\r", "")>
                        <#assign shortRemark = (cleanedRemark?length > 20)?then(cleanedRemark?substring(0, 20) + "...", cleanedRemark)>
                        <#assign auxItem = auxItem + ["备注【${shortRemark}】"]>
                    </#if>

                    <#if auxItem?size gt 0>
                        <#assign auxContent = auxContent + ["辅料${aux?index + 1}：" + auxItem?join("，")]>
                    </#if>
                </#if>
            </#list>

        <#-- 最终输出 -->
            <#if auxContent?size gt 0>
                新增关联辅料信息：
                ${auxContent?join("；")}
            <#else>
                新增关联辅料信息：未提供有效辅料详情
            </#if>
        <#else>
            新增关联辅料信息：未添加任何辅料
        </#if>
    </#if>
</#compress>

<#------------------------------------------------------------------------ 新增物流报关清关 ------------------------------------------------------------------------>
<#compress>
    <#if operationType?? && operationType.code == 'A000006'>
    <#-- 安全处理可能为null的logistics对象 -->
        <#assign logistics = logistics!{}>
        <#assign logisticsBasic = logistics.logisticsBasicDto!{}>
        <#assign declarationInfo = logistics.declarationInfo!{}>
        <#assign clearanceInfo = logistics.clearanceInfo!{}>
        <#assign clearanceCostInfo = logistics.clearanceCostInfo!{}>

        <#assign logisticsContent = []>

    <#-- 1. 处理物流基本信息 -->
        <#if logisticsBasic??>
            <#assign basicContent = []>

        <#-- 基础信息字段 -->
            <#if logisticsBasic.declareZhName?? && logisticsBasic.declareZhName?has_content>
                <#assign basicContent = basicContent + ["报关中文名【${logisticsBasic.declareZhName}】"]>
            </#if>
            <#if logisticsBasic.declareEnName?? && logisticsBasic.declareEnName?has_content>
                <#assign basicContent = basicContent + ["报关英文名【${logisticsBasic.declareEnName}】"]>
            </#if>
            <#if logisticsBasic.material?? && logisticsBasic.material?has_content>
                <#assign basicContent = basicContent + ["材质【${logisticsBasic.material}】"]>
            </#if>
            <#if logisticsBasic.purpose?? && logisticsBasic.purpose?has_content>
                <#assign basicContent = basicContent + ["用途【${logisticsBasic.purpose}】"]>
            </#if>
            <#if logisticsBasic.brandTypeName?? && logisticsBasic.brandTypeName?has_content>
                <#assign basicContent = basicContent + ["品牌类型【${logisticsBasic.brandTypeName}】"]>
            </#if>
            <#if logisticsBasic.declareHscode?? && logisticsBasic.declareHscode?has_content>
                <#assign basicContent = basicContent + ["HS编码【${logisticsBasic.declareHscode}】"]>
            </#if>

        <#-- 特殊属性 -->
            <#if logisticsBasic.specialAttrsStr?? && logisticsBasic.specialAttrsStr?has_content>
                <#assign basicContent = basicContent + ["特殊属性【${logisticsBasic.specialAttrsStr}】"]>
            </#if>

            <#if basicContent?size gt 0>
                <#assign logisticsContent = logisticsContent + ["物流基本信息：" + basicContent?join("，")]>
            </#if>
        </#if>

    <#-- 2. 处理报关信息 -->
        <#if declarationInfo??>
            <#assign declareContent = []>

            <#if declarationInfo.declarePrice??>
                <#assign declareContent = declareContent + ["报关单价【${declarationInfo.declarePrice}${declarationInfo.declarePriceCurrencyCode!'无'}】"]>
            </#if>
            <#if declarationInfo.declareHscode?? && declarationInfo.declareHscode?has_content>
                <#assign declareContent = declareContent + ["HS编码【${declarationInfo.declareHscode}】"]>
            </#if>
            <#if declarationInfo.declareArea?? && declarationInfo.declareArea?has_content>
                <#assign declareContent = declareContent + ["报关地区【${declarationInfo.declareArea}】"]>
            </#if>
            <#if declarationInfo.taxRefundRate??>
                <#assign declareContent = declareContent + ["退税率【${declarationInfo.taxRefundRate}%】"]>
            </#if>
            <#if declarationInfo.commercialCheckNeed??>
                <#assign checkNeed = (declarationInfo.commercialCheckNeed == '1')?string("需要", "不需要")>
                <#assign declareContent = declareContent + ["商检【${checkNeed}】"]>
            </#if>
            <#if declarationInfo.authTypeName?? && declarationInfo.authTypeName?has_content>
                <#assign declareContent = declareContent + ["认证类型【${declarationInfo.authTypeName}】"]>
            </#if>

            <#if declareContent?size gt 0>
                <#assign logisticsContent = logisticsContent + ["报关信息：" + declareContent?join("，")]>
            </#if>
        </#if>

    <#-- 3. 处理清关信息 -->
        <#if clearanceInfo??>
            <#assign clearanceContent = []>

            <#if clearanceInfo.clearanceModel?? && clearanceInfo.clearanceModel?has_content>
                <#assign clearanceContent = clearanceContent + ["产品型号【${clearanceInfo.clearanceModel}】"]>
            </#if>
            <#if clearanceInfo.clearanceRemark?? && clearanceInfo.clearanceRemark?has_content>
                <#assign clearanceContent = clearanceContent + ["清关备注【${clearanceInfo.clearanceRemark}】"]>
            </#if>
            <#if clearanceInfo.weavingModeName?? && clearanceInfo.weavingModeName?has_content>
                <#assign clearanceContent = clearanceContent + ["织造方式【${clearanceInfo.weavingModeName}】"]>
            </#if>

            <#if clearanceContent?size gt 0>
                <#assign logisticsContent = logisticsContent + ["清关信息：" + clearanceContent?join("，")]>
            </#if>
        </#if>

    <#-- 4. 处理清关费用信息 -->
        <#if clearanceCostInfo.clearanceCostDTOList?? && clearanceCostInfo.clearanceCostDTOList?size gt 0>
            <#assign costContent = []>

            <#list clearanceCostInfo.clearanceCostDTOList as cost>
                <#if cost??>
                    <#assign costItem = []>

                    <#if cost.countryName?? && cost.countryName?has_content>
                        <#assign costItem = costItem + ["国家【${cost.countryName}】"]>
                    </#if>
                    <#if cost.clearanceHscode?? && cost.clearanceHscode?has_content>
                        <#assign costItem = costItem + ["HS编码【${cost.clearanceHscode}】"]>
                    </#if>
                    <#if cost.clearancePrice??>
                        <#assign costItem = costItem + ["单价【${cost.clearancePrice}${cost.clearancePriceCurrencyCode!'无'}】"]>
                    </#if>
                    <#if cost.clearanceTaxRate??>
                        <#assign costItem = costItem + ["税率【${cost.clearanceTaxRate}%】"]>
                    </#if>
                    <#if cost.isDefault??>
                        <#assign isDefault = (cost.isDefault == '1')?string("是", "否")>
                        <#assign costItem = costItem + ["默认【${isDefault}】"]>
                    </#if>

                    <#if costItem?size gt 0>
                        <#assign costContent = costContent + ["费用${cost?index + 1}：" + costItem?join("，")]>
                    </#if>
                </#if>
            </#list>

            <#if costContent?size gt 0>
                <#assign logisticsContent = logisticsContent + ["清关费用：" + costContent?join("；")]>
            </#if>
        </#if>

    <#-- 最终输出 -->
        <#if logisticsContent?size gt 0>
            新增物流清报关信息：
            ${logisticsContent?join("；")}
        <#else>
            新增物流清报关信息：未提供详细物流清报关信息
        </#if>
    </#if>
</#compress>


<#------------------------------------------------------------------------ 新增图片信息 ------------------------------------------------------------------------>
<#compress>
    <#if operationType?? && operationType.code == 'A000007'>
    <#-- 安全处理可能为null的图片列表 -->
        <#assign picList = picList![]>

    <#-- 只有当图片列表不为空时才处理 -->
        <#if picList?? && picList?size gt 0>
            <#assign picContent = []>

            <#list picList as pic>
                <#if pic??>  <#-- 防止单个图片对象为null -->
                    <#assign picItem = []>

                <#-- 处理图片基本信息 -->
                    <#if pic.picName?? && pic.picName?has_content>
                        <#assign picItem = picItem + ["图片名称【${pic.picName}】"]>
                    </#if>

                <#-- 处理主图标识 -->
                    <#if pic.isPrimary??>
                        <#assign isPrimary = (pic.isPrimary == '1')?string("(主图)", "")>
                        <#assign picItem = picItem + ["${isPrimary}"]>
                    </#if>

                <#-- 处理图片URL -->
                    <#if pic.picUrl?? && pic.picUrl?has_content>
                        <#assign picItem = picItem + ["图片链接【${pic.picUrl}】"]>
                    </#if>

                <#-- 处理图片尺寸 -->
                    <#if pic.picSizeTxt?? && pic.picSizeTxt?has_content>
                        <#assign picItem = picItem + ["尺寸【${pic.picSizeTxt}】"]>
                    <#elseif pic.picWidth?? && pic.picHeight??>
                        <#assign picItem = picItem + ["尺寸【${pic.picWidth}*${pic.picHeight}px】"]>
                    </#if>

                <#-- 处理图片大小 -->
                    <#if pic.picSpaceTxt?? && pic.picSpaceTxt?has_content>
                        <#assign picItem = picItem + ["大小【${pic.picSpaceTxt}】"]>
                    <#elseif pic.picSpace?? && pic.picSpace?has_content>
                    <#-- 先将字符串转换为数字，然后计算KB大小 -->
                        <#assign sizeInBytes = pic.picSpace?number>
                        <#assign sizeKB = (sizeInBytes / 1024)?string("0.##")>
                        <#assign picItem = picItem + ["大小【${sizeKB}KB】"]>
                    </#if>

                    <#if picItem?size gt 0>
                        <#assign picContent = picContent + ["图片${pic?index + 1}：" + picItem?join(" ")]>
                    </#if>
                </#if>
            </#list>

        <#-- 最终输出 -->
            <#if picContent?size gt 0>
                新增图片信息：
                ${picContent?join("；")}
            <#else>
                新增图片信息：未提供有效图片详情
            </#if>
        <#else>
            新增图片信息：未添加任何图片
        </#if>
    </#if>
</#compress>

<#------------------------------------------------------------------------ 新增质检信息 ------------------------------------------------------------------------>
<#compress>
    <#if operationType?? && operationType.code == 'A000008'>
    <#-- 安全处理可能为null的质检对象 -->
        <#assign qcStandard = qcStandard!{}>
        <#assign templateInfo = qcStandard.templateInfo!{}>
        <#assign customTemplate = qcStandard.customTemplate!{}>

        <#assign qcContent = []>

    <#-- 1. 处理基础质检信息 -->
        <#if qcStandard.qcMethodName?? && qcStandard.qcMethodName?has_content>
            <#assign qcContent = qcContent + ["质检方式【${qcStandard.qcMethodName}】"]>
        </#if>

    <#-- 2. 处理标准模板信息 -->
        <#if templateInfo??>
            <#assign templateContent = []>

        <#-- 模板基本信息 -->
            <#if templateInfo.templateName?? && templateInfo.templateName?has_content>
                <#assign templateContent = templateContent + ["标准模板【${templateInfo.templateName}】"]>
            </#if>

        <#-- 质检项列表 -->
            <#if templateInfo.productQcTemplateItemList?? && templateInfo.productQcTemplateItemList?size gt 0>
                <#assign itemContent = []>
                <#list templateInfo.productQcTemplateItemList as item>
                    <#if item?? && item.qcItem?? && item.qcItem?has_content>
                        <#assign itemDesc = []>
                        <#assign itemDesc = itemDesc + ["${item.qcItem}"]>

                    <#-- 处理质检内容完全兼容版 -->
                        <#if item.qcContent?? && item.qcContent?size gt 0>
                            <#assign cleanedContent = []>
                            <#list item.qcContent as content>
                            <#-- 基础处理 -->
                                <#assign noTags = content?replace("<", "<")?replace(">", ">")>
                                <#assign plainText = noTags?split("<")?map(part -> part?split(">")?last)?join("")>

                            <#-- 清理空白 -->
                                <#assign trimmedText = plainText?trim?replace("[\n\r]+", " ", "r")?replace("  ", " ")>

                            <#-- 安全截取 -->
                                <#assign textLength = trimmedText?length>
                                <#if textLength gt 50>
                                    <#assign displayText = trimmedText[0..49] + "...">
                                <#else>
                                    <#assign displayText = trimmedText>
                                </#if>

                                <#assign cleanedContent = cleanedContent + [displayText]>
                            </#list>
                            <#assign itemDesc = itemDesc + ["要求：${cleanedContent?join(' | ')}"]>
                        </#if>

                    <#-- 质检项图片 -->
                        <#if item.itemImg?? && item.itemImg?has_content>
                            <#assign itemDesc = itemDesc + ["参考图】"]>
                        </#if>

                        <#assign itemContent = itemContent + [itemDesc?join('，')]>
                    </#if>
                </#list>

                <#if itemContent?size gt 0>
                    <#assign templateContent = templateContent + ["质检项：${itemContent?join('；')}"]>
                </#if>
            </#if>

        <#-- 模板图片 -->
            <#if templateInfo.templateImageList?? && templateInfo.templateImageList?size gt 0>
                <#assign imgCount = templateInfo.templateImageList?size>
                <#assign templateContent = templateContent + ["包含${imgCount}张模板图片"]>
            </#if>

            <#if templateContent?size gt 0>
                <#assign qcContent = qcContent + ["标准模板信息：" + templateContent?join("，")]>
            </#if>
        </#if>

    <#-- 3. 处理自定义模板信息 -->
        <#if customTemplate??>
            <#assign customContent = []>

        <#-- 自定义模板基本信息 -->
            <#if customTemplate.templateName?? && customTemplate.templateName?has_content>
                <#assign customContent = customContent + ["自定义模板【${customTemplate.templateName}】"]>
            </#if>

        <#-- 自定义质检项列表 -->
            <#if customTemplate.productQcTemplateItemList?? && customTemplate.productQcTemplateItemList?size gt 0>
                <#assign customItemContent = []>
                <#list customTemplate.productQcTemplateItemList as item>
                    <#if item?? && item.qcItem?? && item.qcItem?has_content>
                        <#assign itemDesc = []>
                        <#assign itemDesc = itemDesc + ["${item.qcItem}"]>

                    <#-- 质检内容（与上方保持一致的完全兼容版） -->
                        <#if item.qcContent?? && item.qcContent?size gt 0>
                            <#assign cleanedContent = []>
                            <#list item.qcContent as content>
                            <#-- 基础处理 -->
                                <#assign noTags = content?replace("<", "<")?replace(">", ">")>
                                <#assign plainText = noTags?split("<")?map(part -> part?split(">")?last)?join("")>

                            <#-- 清理空白 -->
                                <#assign trimmedText = plainText?trim?replace("[\n\r]+", " ", "r")?replace("  ", " ")>

                            <#-- 安全截取 -->
                                <#assign textLength = trimmedText?length>
                                <#if textLength gt 50>
                                    <#assign displayText = trimmedText[0..49] + "...">
                                <#else>
                                    <#assign displayText = trimmedText>
                                </#if>

                                <#assign cleanedContent = cleanedContent + [displayText]>
                            </#list>
                            <#assign itemDesc = itemDesc + ["要求：${cleanedContent?join(' | ')}"]>
                        </#if>



                    <#-- 质检项图片 -->
                        <#if item.itemImg?? && item.itemImg?has_content>
                            <#assign itemDesc = itemDesc + ["参考图】"]>
                        </#if>

                        <#assign customItemContent = customItemContent + [itemDesc?join('，')]>
                    </#if>
                </#list>

                <#if customItemContent?size gt 0>
                    <#assign customContent = customContent + ["质检项：${customItemContent?join('；')}"]>
                </#if>
            </#if>

        <#-- 自定义模板图片 -->
            <#if customTemplate.templateImageList?? && customTemplate.templateImageList?size gt 0>
                <#assign customImgCount = customTemplate.templateImageList?size>
                <#assign customContent = customContent + ["包含${customImgCount}张自定义模板图片"]>
            </#if>

            <#if customContent?size gt 0>
                <#assign qcContent = qcContent + ["自定义模板信息：" + customContent?join("，")]>
            </#if>
        </#if>

    <#-- 最终输出 -->
        <#if qcContent?size gt 0>
            新增质检信息：
            ${qcContent?join("；")}
        <#else>
            新增质检信息：未配置质检标准
        </#if>
    </#if>
</#compress>

<#------------------------------------------------------------------------ 新增MSKU ------------------------------------------------------------------------>
<#assign MSKUFields = {
"mskuCode": "MSKU编码",
"userName": "负责人",
"mskuRemark": "备注"
}>

<#compress>
    <#if operationType?? && operationType.code == 'A000009'>
        <#assign mskuList = mskuList![]>
        <#assign mskuContent = []>

        <#if mskuList?? && mskuList?size gt 0>
            <#list mskuList as msku>
                <#assign mskuItem = []>
            <#-- 合并店铺和站点信息 -->
                <#if (msku.storeName?? && msku.storeName?has_content) || (msku.marketCode?? && msku.marketCode?has_content)>
                    <#assign storeName = msku.storeName!"">
                    <#assign marketCode = msku.marketCode!"">
                    <#assign mskuItem = mskuItem + ["店铺/站点【${storeName}：${marketCode}】"]>
                </#if>

                <#list MSKUFields?keys as field>
                    <#assign label = MSKUFields[field]>
                    <#if msku[field]?? && msku[field]?has_content && msku[field]?string != "空">
                        <#assign mskuItem = mskuItem + ["${label}【${msku[field]?string}】"]>
                    </#if>
                </#list>

                <#if mskuItem?size gt 0>
                    <#assign mskuContent = mskuContent + ["第${msku?index + 1}个MSKU：" + mskuItem?join("，")]>
                </#if>
            </#list>

            <#if mskuContent?size gt 0>
                新增MSKU信息：
                ${mskuContent?join("；")}
            </#if>
        </#if>
    </#if>
</#compress>

<#------------------------------------------------------------------------ 更新基础信息 ------------------------------------------------------------------------>
<#compress>
    <#if operationType?? && operationType.code == 'B000001'>
    <#-- 基础信息更新日志模板 - 适配Javers 6.10 -->
        <#if value?? && value?size gt 0>
            <#list value as change>
            <#-- 获取属性名、旧值和新值 -->
                <#assign propertyName = change.propertyName!"">
                <#assign oldValue = change.left!>
                <#assign newValue = change.right!>

            <#-- 根据属性名显示不同的文本描述 -->
                <#if propertyName == "skuName">
                    SKU名称：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                <#elseif propertyName == "sku">
                    SKU编码：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                <#elseif propertyName == "mnemonicCode">
                    助记码：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                <#elseif propertyName == "skuSpecBrief">
                    型号：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                <#elseif propertyName == "skuInvoicingName">
                    开票名称：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                <#elseif propertyName == "skuUnit">
                    单位：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                <#elseif propertyName == "skuStatus">
                    销售状态：<#if oldValue?? && oldValue?string != "null">
                    <#if oldValue == "0">开发中<#elseif oldValue == "1">在售<#elseif oldValue == "2">清仓<#elseif oldValue == "3">停售<#else>${oldValue}</#if>
                <#else>无</#if> →
                    <#if newValue?? && newValue?string != "null">
                        <#if newValue == "0">开发中<#elseif newValue == "1">在售<#elseif newValue == "2">清仓<#elseif newValue == "3">停售<#else>${newValue}</#if>
                    <#else>无</#if>
                <#elseif propertyName == "skuOpenStatus">
                    启停状态：<#if oldValue?? && oldValue?string != "null">
                    <#if oldValue == "1">启用<#elseif oldValue == "0">停用<#else>${oldValue}</#if>
                <#else>无</#if> →
                    <#if newValue?? && newValue?string != "null">
                        <#if newValue == "1">启用<#elseif newValue == "0">停用<#else>${newValue}</#if>
                    <#else>无</#if>
                <#elseif propertyName == "skuDesc">
                    描述：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                <#elseif propertyName == "skuType">
                    产品类型：<#if oldValue?? && oldValue?string != "null">
                    <#if oldValue == "1">普通产品<#elseif oldValue == "2">组合产品<#elseif oldValue == "3">捆绑产品<#else>${oldValue}</#if>
                <#else>无</#if> →
                    <#if newValue?? && newValue?string != "null">
                        <#if newValue == "1">普通产品<#elseif newValue == "2">组合产品<#elseif newValue == "3">捆绑产品<#else>${newValue}</#if>
                    <#else>无</#if>
                <#elseif propertyName == "developPersonName">
                    <#if propertyName == "developPersonName">开发人名称</#if>：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                <#elseif propertyName == "skuDeveloperUid">
                    开发人ID：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                <#elseif propertyName == "skuDutyUids">
                    产品负责人ID：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                <#elseif propertyName == "skuCategory">
                    分类ID：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                <#elseif propertyName == "categoryName">
                    分类名称：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                <#elseif propertyName == "prodId">
                    关联SPUI ID：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                <#elseif propertyName == "quaSpec">
                    质量标准：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                <#elseif propertyName == "unitProcessFee">
                    单位加工费：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                <#elseif propertyName == "processRemark">
                    加工备注：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                <#elseif propertyName == "isDeclare">
                    是否报关：<#if oldValue?? && oldValue?string != "null">
                    <#if oldValue == "0">不报关<#elseif oldValue == "1">报关<#else>${oldValue}</#if>
                <#else>无</#if> →
                    <#if newValue?? && newValue?string != "null">
                        <#if newValue == "0">不报关<#elseif newValue == "1">报关<#else>${newValue}</#if>
                    <#else>无</#if>
                <#elseif propertyName == "isFbm">
                    是否FBM：<#if oldValue?? && oldValue?string != "null">
                    <#if oldValue == "0">否<#elseif oldValue == "1">是<#else>${oldValue}</#if>
                <#else>无</#if> →
                    <#if newValue?? && newValue?string != "null">
                        <#if newValue == "0">否<#elseif newValue == "1">是<#else>${newValue}</#if>
                    <#else>无</#if>
                <#-- 对于嵌套字段的处理，例如在属性名中包含引用路径的情况 -->
                <#elseif propertyName?contains("attachmentList") && propertyName?contains("fileUrl")>
                    附件链接：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                <#elseif propertyName?contains("dutyPersonList") && propertyName?contains("name")>
                    产品负责人：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                <#elseif propertyName?contains("skuTagV") && propertyName?contains("tagName")>
                    产品标签：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                <#elseif propertyName?contains("productTeamAssociationVoList")>
                    <#if propertyName?contains("companyName")>
                        公司名称：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                    <#elseif propertyName?contains("teamName")>
                        团队名称：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                    <#elseif propertyName?contains("platformName")>
                        平台名称：<#if oldValue?? && oldValue?string != "null">${oldValue}<#else>无</#if> → <#if newValue?? && newValue?string != "null">${newValue}<#else>无</#if>
                    <#elseif propertyName?contains("status")>
                        状态：<#if oldValue?? && oldValue?string != "null">
                        <#if oldValue == "0">禁用<#elseif oldValue == "1">启用<#else>${oldValue}</#if>
                    <#else>无</#if> →
                        <#if newValue?? && newValue?string != "null">
                            <#if newValue == "0">禁用<#elseif newValue == "1">启用<#else>${newValue}</#if>
                        <#else>无</#if>
                    </#if>
                </#if>
                <#if change_has_next><br/></#if>
            </#list>
        <#else>
            未检测到基础信息变更
        </#if>
    </#if>
</#compress>


<#-- 公共函数：字段名转可读标签 -->
<#function getFieldLabel field>
    <#switch field>
        <#case "skuName"> <#return "SKU名称">
        <#case "status"> <#return "状态">
        <#case "price"> <#return "价格">
        <#case "amount"> <#return "金额">
        <#case "skuId"> <#return "SKU ID">
        <#case "skuName"> <#return "SKU名称">
        <#case "sku"> <#return "SKU编码">
        <#case "mnemonicCode"> <#return "SKU助记码">
        <#case "skuSpecBrief"> <#return "SKU型号">
        <#case "skuInvoicingName"> <#return "SKU开票名称">
        <#case "skuUnit"> <#return "单位">
        <#case "skuStatus"> <#return "销售状态">
        <#case "skuStatusName"> <#return "销售状态名称">
        <#case "skuOpenStatus"> <#return "启停状态">
        <#case "skuDesc"> <#return "描述">
        <#case "attachmentList"> <#return "附件列表">
        <#case "skuType"> <#return "产品类型">
        <#case "createPersonName"> <#return "创建人名称">
        <#case "createBy"> <#return "创建人">
        <#case "developPersonName"> <#return "开发人名称">
        <#case "skuDeveloperUid"> <#return "开发人ID">
        <#case "dutyPersonList"> <#return "产品负责人">
        <#case "skuDutyUids"> <#return "产品负责人ID">
        <#case "skuTag"> <#return "产品标签">
        <#case "skuTagV"> <#return "产品标签">
        <#case "skuCategory"> <#return "分类ID">
        <#case "categoryName"> <#return "分类名称">
        <#case "skuBrand"> <#return "品牌ID">
        <#case "brandName"> <#return "品牌名称">
        <#case "brandNameEn"> <#return "品牌英文名称">
        <#case "simpleName"> <#return "品牌简称">
        <#case "attrValueList"> <#return "属性信息">
        <#case "prodId"> <#return "关联的SPU的ID">
        <#case "quaSpec"> <#return "质量标准">
        <#case "unitProcessFee"> <#return "单位加工费">
        <#case "processRemark"> <#return "加工备注">
        <#case "productTeamAssociationVoList"> <#return "公司/团队信息">
        <#case "isDeclare"> <#return "是否报关">
        <#case "isFbm"> <#return "是否FBA">
        <#case "updateBy"> <#return "修改人">
        <#case "tenantId"> <#return "租户ID">
        <#case "purchasePersonName"> <#return "采购人名称">
        <#case "purchasePersonId"> <#return "采购人的ID">
        <#case "purchaseDelivery"> <#return "采购周期">
        <#case "purchaseRemark"> <#return "采购备注">
        <#case "purchasePrice"> <#return "采购成本">
        <#case "specSingleLength"> <#return "单品规格长">
        <#case "specSingleWidth"> <#return "单品规格宽">
        <#case "specSingleHeight"> <#return "单品规格高">
        <#case "specSingleWeight"> <#return "单品净重">
        <#case "specMaterial"> <#return "产品材质">
        <#case "specPackList"> <#return "包装规格信息">
        <#case "specBoxTitle"> <#return "箱规名称">
        <#case "specBoxId"> <#return "箱规id">
        <#case "isDefault"> <#return "是否是默认:1是0否">
        <#case "boxPcs"> <#return "单箱数量">
        <#case "specBoxLength"> <#return "外箱规长">
        <#case "specBoxWidth"> <#return "外箱规宽">
        <#case "specBoxHeight"> <#return "外箱规高">
        <#case "packageLength"> <#return "包装规格长">
        <#case "packageWidth"> <#return "包装规格宽">
        <#case "packageHeight"> <#return "包装规格高">
        <#case "singleBoxWeight"> <#return "单箱重量">
        <#case "singlePackWeight"> <#return "单品毛重">

        <#default> <#return field>
    </#switch>
</#function>

<#-- 公共函数：转换销售状态 -->
<#function convertSkuStatus field>
    <#switch field>
        <#case "0"> <#return "开发中">
        <#case "1"> <#return "在售">
        <#case "2"> <#return "清仓">
        <#case "3"> <#return "停售">
        <#default> <#return field>
    </#switch>
</#function>