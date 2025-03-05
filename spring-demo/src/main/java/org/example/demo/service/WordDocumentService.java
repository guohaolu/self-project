package org.example.demo.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Service
public class WordDocumentService {
    @Autowired
    private Configuration freemarkerConfig;

    public void generateWordDocument(String name) throws Exception {
        // Set the template directory
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");

        // Load template
        Template template = freemarkerConfig.getTemplate("document-template.ftl");

        // Template data
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);

        // Merge template with data
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);

        // Create a Word document
        try (XWPFDocument document = new XWPFDocument();
             OutputStream out = Files.newOutputStream(Paths.get("generated-document.docx"))) {

            // Add content to the document
            document.createParagraph().createRun().setText(content);

            // Write the document
            document.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
