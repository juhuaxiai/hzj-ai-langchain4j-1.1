package com.hzj.ai;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: Hzj
 * @CreateTime: 2025-11-08  11:44
 * @Description: ToDo
 * @Version: 1.0
 */
@SpringBootTest
public class EmbeddingTest {

    @Autowired
    EmbeddingModel embeddingModel;

    @Autowired
    EmbeddingStore embeddingStore;

    @Test
    void Test01(){
        Response<Embedding> embed = embeddingModel.embed("你好");

        System.out.println("embed.content().vector().length = " + embed.content().vector().length);

        System.out.println("embed.toString() = " + embed.toString());
    }

    @Test
    void Test02(){
        TextSegment segment1 = TextSegment.from("我喜欢羽毛球");
        Embedding embedding1 = embeddingModel.embed(segment1).content();

        embeddingStore.add(embedding1);

        TextSegment segment2 = TextSegment.from("我喜欢乒乓球");
        Embedding embedding2 = embeddingModel.embed(segment2).content();

        embeddingStore.add(embedding2);
    }

    @Test
    void testUploadKnowledgeLibrary(){
        //使用FileSystemDocumentLoader读取指定目录下的知识库文档
        //并使用默认的文档解析器对文档进行解析

        Document document1 = FileSystemDocumentLoader.loadDocument("knowledge/医院信息.md");

        Document document2 = FileSystemDocumentLoader.loadDocument("knowledge/神经内科.md");

        Document document3 = FileSystemDocumentLoader.loadDocument("knowledge/科室信息.md");

        List<Document> documents = Arrays.asList(document1,document2,document3);

        EmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build()
                .ingest(documents);
    }
}
