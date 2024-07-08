package com.example.spring.employee.service;

import com.example.spring.employee.pojo.dto.Demo;
import com.example.spring.employee.converter.IDemoConverter;
import com.google.common.collect.Maps;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 *
 */
@Service
public class EsSearchExample {
    @Autowired
    private RestHighLevelClient client;

    @Resource
    private IDemoConverter converter;


    public void query() throws IOException {
        // 执行查询
        SearchResponse response = search("your_index", "your_query");
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, String> sourceAsMap = Maps.transformValues(hit.getSourceAsMap(), obj -> (String) obj);
            Demo demo = converter.toDemo(sourceAsMap);
        }
    }

    public SearchResponse search(String index, String query) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.queryStringQuery(query));
        searchRequest.source(searchSourceBuilder);
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }
}