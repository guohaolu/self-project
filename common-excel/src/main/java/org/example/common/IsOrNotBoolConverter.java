package org.example.common;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class IsOrNotBoolConverter implements Converter<Boolean> {
    @Override
    public Class<Boolean> supportJavaTypeKey() {
        return Boolean.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Boolean convertToJavaData(ReadCellData<?> cellData,
                                     ExcelContentProperty contentProperty,
                                     GlobalConfiguration globalConfiguration) {
        // 获取注解配置
        String trueText = "是";
        String falseText = "否";

        if (contentProperty != null && contentProperty.getField() != null) {
            ExcelBooleanFormat annotation = contentProperty.getField()
                    .getAnnotation(ExcelBooleanFormat.class);
            if (annotation != null) {
                trueText = annotation.trueText();
                falseText = annotation.falseText();
            }
        }

        // 处理各种输入类型
        switch (cellData.getType()) {
            case STRING:
                String strValue = cellData.getStringValue().trim();
                if (strValue.equalsIgnoreCase(trueText)) {
                    return true;
                }
                if (strValue.equalsIgnoreCase(falseText)) {
                    return false;
                }
                break;
            case BOOLEAN:
                return cellData.getBooleanValue();
            case NUMBER:
                return cellData.getNumberValue().doubleValue() != 0;
            case EMPTY:
                return null;
            default:
                break;
        }

        // 默认处理其他情况
        return null;
    }

    @Override
    public WriteCellData<?> convertToExcelData(Boolean value,
                                               ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) {
        String trueText = "是";
        String falseText = "否";

        if (contentProperty != null && contentProperty.getField() != null) {
            ExcelBooleanFormat annotation = contentProperty.getField()
                    .getAnnotation(ExcelBooleanFormat.class);
            if (annotation != null) {
                trueText = annotation.trueText();
                falseText = annotation.falseText();
            }
        }

        return new WriteCellData<>(value ? trueText : falseText);
    }
}
