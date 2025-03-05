package org.example.common;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * word处理
 */
@Slf4j
@Service
public class WordManager {
    public void export(FreemarkerTemplateBO templateBO, HttpServletResponse response) {
        File file;
        try {
            file = DynamicWordUtils.createDoc(templateBO);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
        try (InputStream fin = Files.newInputStream(file.toPath());
             OutputStream out = response.getOutputStream()) {


            String encodedFileName = URLEncoder.encode(templateBO.getTitle() + "_" + templateBO.getDate() + ".doc",
                    StandardCharsets.UTF_8.toString());

            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");
            response.setHeader("Content-Disposition", "attachment;" +
                    "filename=\"" + encodedFileName + "\"");

            byte[] buffer = new byte[512];
            int bytesToRead;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while ((bytesToRead = fin.read(buffer)) != -1) {
                out.write(buffer, 0, bytesToRead);
            }
        } catch (Exception e) {
            throw new RuntimeException("导出失败", e);
        } finally {
            if (file.exists()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    // Log warning if deletion failed
                    log.error("Failed to delete temporary file: {}", file.getAbsolutePath());
                }
            }
        }
    }
}
