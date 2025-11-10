package com.hzj.ai.config;

import com.hzj.ai.store.MongoChatMemoryStore;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;


/**
 * @Author: Hzj
 * @CreateTime: 2025-11-06  14:57
 * @Description: ToDo
 * @Version: 1.0
 */


@Configuration
public class XiaozhiAgentConfig {

    @Autowired
    private EmbeddingStore embeddingStore;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    MongoChatMemoryStore mongoChatMemoryStore;

    @Bean
    ChatMemoryProvider chatMemoryProviderXiaozhi(){
        return memoryId-> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(20)
                .chatMemoryStore(mongoChatMemoryStore)
                .build();
    }

    @Bean
    ContentRetriever contentRetrieverXiaozhi(){
        Document document1 = FileSystemDocumentLoader.loadDocument("knowledge/医院信息.md");

        Document document2 = FileSystemDocumentLoader.loadDocument("knowledge/神经内科.md");

        Document document3 = FileSystemDocumentLoader.loadDocument("knowledge/科室信息.md");

        List<Document>  documents = Arrays.asList(document1,document2,document3);

        //采用默认的内存向量存储
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        //采用默认的文档分割器
        EmbeddingStoreIngestor.ingest(documents,embeddingStore);

        //从嵌入存储（EmbeddingStore）里检索和查询内容相关的信息
        return EmbeddingStoreContentRetriever.from(embeddingStore);
    }

    @Bean
    ContentRetriever contentRetrieverXiaozhiPincone(){
        //从嵌入存储（EmbeddingStore）里检索和查询内容相关的信息
        return EmbeddingStoreContentRetriever
                .builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)

                // 设置最大检索结果数量，这里表示最多返回 1 条匹配结果
                .maxResults(1)

                // 设置最小得分阈值，只有得分大于等于 0.8 的结果才会被返回
                .minScore(0.8)
                .build();
    }

}
