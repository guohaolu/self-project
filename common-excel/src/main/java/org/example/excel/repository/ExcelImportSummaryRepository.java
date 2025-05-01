package org.example.excel.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.excel.entity.ExcelImportSummaryEntity;
import org.example.excel.mapper.IExcelImportSummaryDao;
import org.springframework.stereotype.Repository;

@Repository
public class ExcelImportSummaryRepository extends ServiceImpl<IExcelImportSummaryDao, ExcelImportSummaryEntity> {
}
