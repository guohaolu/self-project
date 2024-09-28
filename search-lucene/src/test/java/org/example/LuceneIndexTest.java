package org.example;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Lucene的索引
 */
public class LuceneIndexTest {
    @Test
    @DisplayName("创建索引")
    public void createIndex() throws IOException {
        // 创建一个 MMapDirectory，用于存储索引
        Directory directory = new MMapDirectory(Paths.get("D://work//lucene"));

        // 创建一个分析器,StandardAnalyzer为英文的分词器
        Analyzer analyzer = new StandardAnalyzer();

        // 创建一个 IndexWriterConfig，用于配置 IndexWriter
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        // 创建一个 IndexWriter，用于写入索引
        IndexWriter indexWriter = new IndexWriter(directory, config);

        // 创建文档
        Document document = new Document();

        // 添加字段
        document.add(new TextField("title", "Lucene in Action", Field.Store.YES));
        document.add(new TextField("content", "This book provides a comprehensive guide to Lucene.", Field.Store.YES));
        document.add(new TextField("author", "Erik Hatcher and Otis Gospodnetic", Field.Store.YES));

        // 将文档添加到索引中
        indexWriter.addDocument(document);

        // 关闭 IndexWriter
        indexWriter.close();

        // 打印完成信息
        System.out.println("索引创建完成！");
    }

    @Test
    @DisplayName("搜索")
    public void searchIndex() throws IOException, ParseException {
        // 创建一个 RAMDirectory，用于存储索引
        Directory directory = new RAMDirectory();

        // 创建一个分析器
        Analyzer analyzer = new StandardAnalyzer();

        // 创建一个 IndexWriterConfig，用于配置 IndexWriter
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        // 创建一个 IndexWriter，用于写入索引
        IndexWriter indexWriter = new IndexWriter(directory, config);

        // 创建文档
        Document document1 = new Document();
        document1.add(new TextField("title", "Lucene in Action", Field.Store.YES));
        document1.add(new TextField("content", "This book provides a comprehensive guide to Lucene.", Field.Store.YES));
        document1.add(new TextField("author", "Erik Hatcher and Otis Gospodnetic", Field.Store.YES));
        indexWriter.addDocument(document1);

        Document document2 = new Document();
        document2.add(new TextField("title", "Learning Lucene", Field.Store.YES));
        document2.add(new TextField("content", "A practical introduction to Lucene.", Field.Store.YES));
        document2.add(new TextField("author", "Michael McCandless", Field.Store.YES));
        indexWriter.addDocument(document2);

        // 关闭 IndexWriter
        indexWriter.close();

        // 创建一个 IndexSearcher
        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        // 创建查询解析器
        String[] fields = {"title", "content", "author"};
        QueryParser parser = new MultiFieldQueryParser(fields, analyzer);

        // 构造查询
        String queryStr = "Lucene guide";
        Query query = parser.parse(queryStr);

        // 执行查询
        TopDocs topDocs = searcher.search(query, 10);

        // 输出查询结果
        System.out.println("查询结果：");
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println("标题: " + doc.get("title"));
            System.out.println("内容: " + doc.get("content"));
            System.out.println("作者: " + doc.get("author"));
            System.out.println("得分: " + scoreDoc.score);
            System.out.println("--------------------");
        }

        // 关闭资源
        reader.close();
    }
}
