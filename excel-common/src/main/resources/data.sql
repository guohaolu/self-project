-- 初始化汇总表数据
INSERT INTO excel_import_summary (process_number, process_userid, app_name, biz_type, rows, processed_rows, status,
                                  is_finished)
VALUES ('PN20230001', 1, 'Report', 'C000001', 100, 95, 3, 1),
       ('PN20230002', 1, 'Order', 'O000001', 50, 0, 0, 0);

-- 初始化数据表数据
INSERT INTO excel_import_data (process_number, source, validate_status, impl_status)
VALUES ('PN20230001',
        '{"type":"com.ewayt.entity.StudentEntity","payload":{"name":"张三","age":"14"},"time":"2025-03-27 10:01:34"}',
        1, 1),
       ('PN20230001',
        '{"type":"com.ewayt.entity.StudentEntity","payload":{"name":"李四","age":"15"},"time":"2025-03-27 10:02:15"}',
        1, 1);