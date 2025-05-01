package org.example.excel.dto;

import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import lombok.Data;

import com.alibaba.excel.annotation.ExcelProperty;

@Data
@HeadFontStyle(fontName = "宋体", fontHeightInPoints = 10)
public class AmazonVcListingExcelDTO {
    @ExcelProperty("店铺ID")
    private Long storeId;

    @ExcelProperty("店铺名称")
    private String storeName;

    @ExcelProperty("店铺代码")
    private String storeCode;
}
