package org.example.pool;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 使用{@link CompletableFuture}实现功能：提交一批任务给线程池，然后等待所有任务完成后再执行后面的逻辑
 */
public class MultiOrderQueryService {
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        MultiOrderQueryService service = new MultiOrderQueryService();
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L); // 示例ID列表
        List<String> orders = service.queryOrders(ids);
        System.out.println("All orders: " + orders);
    }

    public List<String> queryOrders(List<Long> ids) {
        List<CompletableFuture<String>> futures = ids.stream()
                .map(this::queryOrderByIdAsync)
                .collect(Collectors.toList());

        // 等待所有任务完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // 收集结果
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    private CompletableFuture<String> queryOrderByIdAsync(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            // 调用第三方接口查询单个订单
            String order = callThirdPartyApi(id);
            System.out.println("Order for ID " + id + ": " + order);
            return order;
        }, executor);
    }

    private String callThirdPartyApi(Long id) {
        // 模拟调用第三方API
        return "Order for ID " + id;
    }
}
