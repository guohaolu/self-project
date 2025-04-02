<#-- 更新基础信息 -->
<#compress>
<#if operationType?? && operationType.code == 'A000001'>
    更新
    <#list fieldChanges as change>
        <#if change?is_first><#else>,</#if>
        ${getFieldLabel(change.fieldName)}: ${change.oldValue!"空"} => ${change.newValue!"空"}
    </#list>
</#if>
</#compress>



<#-- 公共函数：字段名转可读标签 -->
<#function getFieldLabel field>
    <#switch field>
        <#case "skuName"> <#return "SKU名称">
        <#case "status"> <#return "状态">
        <#case "price"> <#return "价格">
        <#case "amount"> <#return "金额">
        <#default> <#return field>
    </#switch>
</#function>