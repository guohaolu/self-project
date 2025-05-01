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
 * @author guohao.lu
 * @param <T> Excel导入模版类
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
    private final ThreadLocal<Boolean> headerValidated = ThreadLocal.withInitial(() -> false);
    // 总处理记录数
    @Getter
    private int totalCount = 0;

    /**
     * 构造函数，初始化BatchProcessListener实例
     *
     * @param clazz         数据类型的Class对象
     * @param processNumber 处理编号
     */
    public BatchProcessListener(Class<T> clazz, String processNumber) {
        this(clazz, processNumber, obj -> new ExcelRowDTO(clazz.getName(), JSONObject.toJSONString(obj)));
    }

    /**
     * 构造函数，初始化BatchProcessListener实例
     *
     * @param clazz         数据类型的Class对象
     * @param processNumber 处理编号
     * @param converter     数据转换函数，将T类型数据转换为ExcelRowDTO
     */
    public BatchProcessListener(Class<T> clazz, String processNumber, Function<T, ExcelRowDTO> converter) {
        this.processNumber = processNumber;
        this.converter = converter;
        this.expectedHeaders = ExcelHeaderValidator.extractExpectedHeaders(clazz);
    }

    /**
     * 处理读取Excel时发生的异常
     *
     * @param exception 异常对象
     * @param context   分析上下文
     * @throws Exception 抛出异常
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        headerValidated.remove();
        throw exception;
    }

    /**
     * 处理每一行数据
     *
     * @param data    当前行数据
     * @param context 分析上下文
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        if (!headerValidated.get()) {
            throw new IllegalStateException("表头未经验证，数据读取已中止");
        }

        batchList.add(data);
        totalCount++;

        // 达到批次大小时处理
        if (batchList.size() >= batchSize) {
            processBatch();
        }
    }

    /**
     * 处理表头数据，验证表头是否符合预期
     *
     * @param headMap 表头数据
     * @param context 分析上下文
     */
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        if (!headerValidated.get()) {
            try {
                ExcelHeaderValidator.validateHeaderMap(headMap, expectedHeaders);
                headerValidated.set(true);
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 所有数据解析完成后调用，处理剩余数据
     *
     * @param context 分析上下文
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 处理剩余数据
        if (!batchList.isEmpty()) {
            processBatch();
        }
    }

    /**
     * 处理当前批次数据，将数据转换为ExcelRowDTO并保存
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
