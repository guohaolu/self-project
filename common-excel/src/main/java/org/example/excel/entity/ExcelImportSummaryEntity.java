package org.example.excel.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("excel_import_summary")
public class ExcelImportSummaryEntity extends Model<ExcelImportSummaryEntity> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("process_number")
    private String processNumber;

    @TableField("process_userid")
    private Long processUserid;

    @TableField("app_name")
    private String appName;

    @TableField("biz_type")
    private String bizType;

    @TableField("rows")
    private Long rows;

    @TableField("processed_rows")
    private Long processedRows;

    @TableField("status")
    private Integer status;

    @TableField("is_finished")
    private Boolean isFinished;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
