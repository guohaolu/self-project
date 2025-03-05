package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.net.URL;

/**
 * MinioServiceTest
 */
@Slf4j
@SpringBootTest
public class MinioServiceTest {
    @Autowired
    private MinioService minioService;

    @Test
    public void testUpload() {
        String url = "https://la.btc620.com//mp43/1020346.mp4?st=IfHrcUahgdz43kD1Osh2-w&e=1731238369&f=d8bdlQUSQoRAturF5jWlrvyZOEZxbM3w1sGy01jHt1dptNawCIWpdW13RWikdbJ9RivrFvGAcjrzuvFP/gcHO7BOA7teGNSHjCYZCHGGfYvUw4cBHME";
        try (InputStream inputStream = new URL(url).openStream()) {
            minioService.uploadFile("p-video", "video2", inputStream, "video/mp4");
            log.info( "File uploaded successfully.");
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage(), e);
        }
    }
}
