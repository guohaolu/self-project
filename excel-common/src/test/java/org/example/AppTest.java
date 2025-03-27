package org.example;


import com.alibaba.excel.EasyExcel;
import org.example.common.BatchProcessListener;
import org.example.common.ProcessNumberUtil;
import org.example.excel.dto.AmazonVcListingExcelDTO;
import org.example.excel.service.ExcelImportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
public class AppTest {
    @Autowired
    private ExcelImportService excelImportService;

    @Test
    public void testImport() {
        // 生成processNumber
        String processNumber = ProcessNumberUtil.generate();


        BatchProcessListener<AmazonVcListingExcelDTO> listener = new BatchProcessListener<>(AmazonVcListingExcelDTO.class, processNumber);

        try (InputStream inputStream = new FileInputStream("src/test/resources/111.xlsx")) {
            EasyExcel.read(inputStream)
                    .sheet()
                    .registerReadListener(listener)
                    .headRowNumber(1) // 跳过表头
                    .doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(processNumber);
    }
}
