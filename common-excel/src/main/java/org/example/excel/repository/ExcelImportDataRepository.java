package org.example.excel.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.excel.entity.ExcelImportDataEntity;
import org.example.excel.mapper.IExcelImportDataDao;
import org.springframework.stereotype.Repository;

@Repository
public class ExcelImportDataRepository extends ServiceImpl<IExcelImportDataDao, ExcelImportDataEntity> {
}
