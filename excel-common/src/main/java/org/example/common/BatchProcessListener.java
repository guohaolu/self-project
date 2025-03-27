package org.example.common;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.excel.dto.ExcelRowDTO;
import org.example.excel.repository.ExcelImportDataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * 只允许通过构造器初始化
 *
 * @param <T> 类型
 */
@Slf4j
public class BatchProcessListener<T> extends AnalysisEventListener<T> {
    // 批次数据处理消费者
    private final Function<T, ExcelRowDTO> converter;
    @Getter
    private final String processNumber;
    private final Set<String> expectedHeaders;
    // 批量处理大小
    @Setter
    private int batchSize = 1000;
    // 临时存储批次数据
    private final List<T> batchList = new ArrayList<>(batchSize);
    private boolean headerValidated = false;
    // 总处理记录数
    @Getter
    private int totalCount = 0;

    public BatchProcessListener(Class<T> clazz, String processNumber) {
        this(clazz, processNumber, obj -> new ExcelRowDTO(clazz.getName(), JSONObject.toJSONString(obj)));
    }

    public BatchProcessListener(Class<T> clazz, String processNumber, Function<T, ExcelRowDTO> converter) {
        this.processNumber = processNumber;
        this.converter = converter;
        this.expectedHeaders = ExcelHeaderValidator.extractExpectedHeaders(clazz);
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        if (!headerValidated) {
            throw new IllegalStateException("表头未经验证，数据读取已中止");
        }

        batchList.add(data);
        totalCount++;

        // 达到批次大小时处理
        if (batchList.size() >= batchSize) {
            processBatch();
        }
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        if (!headerValidated) {
            try {
                ExcelHeaderValidator.validateHeaderMap(headMap, expectedHeaders);
                headerValidated = true;
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 处理剩余数据
        if (!batchList.isEmpty()) {
            processBatch();
        }
    }

    /**
     * 处理当前批次数据
     */
    private void processBatch() {
        try {
            List<ExcelRowDTO> sourceList = batchList.stream().map(converter).toList();
            SpringUtil.getBean(ExcelImportDataRepository.class).saveBatch(null);
        } finally {
            // 清空当前批次
            batchList.clear();
        }
    }
}
