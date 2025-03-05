package org.example.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WordDocumentServiceTest {
    @Autowired
    private WordDocumentService wordDocumentService;

    @Test
    void generateWordDocument() throws Exception {
        wordDocumentService.generateWordDocument("hhhh");
    }
}