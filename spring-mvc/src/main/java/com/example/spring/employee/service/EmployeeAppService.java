package com.example.spring.employee.service;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service
public class EmployeeAppService {
    @Resource
    private RestHighLevelClient restHighLevelClient;

    public void query() {

    }
}
