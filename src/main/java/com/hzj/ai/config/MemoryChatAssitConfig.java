package com.hzj.ai.config;

import com.hzj.ai.assistant.Assistant;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Hzj
 * @CreateTime: 2025-10-31  20:51
 * @Description: ToDo
 * @Version: 1.0
 */

@Configuration
public class MemoryChatAssitConfig {

    @Bean
    ChatMemory chatMemory(){
        return MessageWindowChatMemory.withMaxMessages(20);
    }

}
