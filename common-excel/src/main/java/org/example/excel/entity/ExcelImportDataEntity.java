package org.example.excel.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("excel_import_data")
public class ExcelImportDataEntity extends Model<ExcelImportDataEntity> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("process_number")
    private String processNumber;

    @TableField("source")
    private String source; // 实际使用时可以定义具体的DTO类

    @TableField("validate_status")
    private Boolean validateStatus;

    @TableField(value = "validate_message")
    private List<String> validateMessage;

    @TableField("impl_status")
    private Boolean implStatus;

    @TableField("impl_message")
    private String implMessage;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
