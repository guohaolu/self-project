package org.example.excel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.excel.entity.ExcelImportDataEntity;

@Mapper
public interface IExcelImportDataDao extends BaseMapper<ExcelImportDataEntity> {
}
