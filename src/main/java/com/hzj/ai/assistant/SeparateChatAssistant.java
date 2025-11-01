package com.hzj.ai.assistant;


import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

@AiService(
        wiringMode = EXPLICIT,
        chatMemory = "chatMemory",
        chatMemoryProvider = "chatMemoryProvider",
        chatModel = "qwenChatModel"
)
public interface SeparateChatAssistant {

    String chat(@MemoryId Integer memoryId, @UserMessage String message);
}
