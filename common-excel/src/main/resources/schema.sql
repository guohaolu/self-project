CREATE TABLE `excel_import_summary`
(
    `id`             bigint      PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `process_number` varchar(64) NOT NULL COMMENT '操作编号，区分不同的导入操作',
    `process_userid` bigint      NOT NULL COMMENT '操作人',
    `app_name`       varchar(32) NOT NULL COMMENT '本次导入操作的微服务系统',
    `biz_type`       varchar(32) NOT NULL COMMENT '业务类型，大写单字母+6位编码，如：产品标签导入(C000001)',
    `rows`           bigint      NOT NULL DEFAULT 0 COMMENT '记录本次导入的Excel行数',
    `processed_rows` bigint      NOT NULL DEFAULT 0 COMMENT '已成功处理的行数',
    `status`         int         NOT NULL DEFAULT 0 COMMENT '导入状态：未导入(0)，已导入(1)，已校验(2)，已实施(3)',
    `is_finished`    tinyint(1) NOT NULL DEFAULT 0 COMMENT '任务是否结束',
    `create_by`      bigint      NOT NULL DEFAULT COMMENT '创建人',
    `create_time`    datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`      bigint      NOT NULL DEFAULT COMMENT '更新人',
    `update_time`    datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='Excel导入汇总表';

CREATE TABLE `excel_import_data`
(
    `id`               bigint      PRIMARY KEY  AUTO_INCREMENT COMMENT '主键ID',
    `process_number`   varchar(64) NOT NULL COMMENT '操作编号，关联汇总表',
    `source`           json                 DEFAULT NULL COMMENT '存储用户导入Excel中的行数据，用于数据解析',
    `validate_status`  tinyint(1) DEFAULT 0 COMMENT '行数据是否通过校验',
    `validate_message` json                 DEFAULT NULL COMMENT '行数据的校验结果信息',
    `impl_status`      tinyint(1) DEFAULT 0 COMMENT '行数据是否导入成功',
    `impl_message`     varchar(1024)        DEFAULT NULL COMMENT '行数据的导入结果信息',
    `create_by`        bigint      NOT NULL DEFAULT COMMENT '创建人',
    `create_time`      datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`        bigint      NOT NULL DEFAULT COMMENT '更新人',
    `update_time`      datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='Excel导入数据表';