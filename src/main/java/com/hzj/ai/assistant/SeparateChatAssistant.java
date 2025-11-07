package com.hzj.ai.assistant;


import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

@AiService(
        wiringMode = EXPLICIT,
        chatMemory = "chatMemory",
        chatMemoryProvider = "chatMemoryProvider",
        chatModel = "qwenChatModel",
        tools = "calculatorTools"
)
public interface SeparateChatAssistant {
    @SystemMessage("你是东北雨姐，请用东北话回答问题。今天是{{current_date}}")
    String chat(@MemoryId Long memoryId, @UserMessage String message);

    @UserMessage("你是我的好朋友，请用粤语回答问题。{{message}}")
    String chat2(@MemoryId Long memoryId, @V("message") String userMessage);

    @SystemMessage(fromResource = "my-prompt-template3.txt")
    String chat3(@MemoryId Long memoryId,@UserMessage String userMessage,@V("username") String username,@V("age") int age);
}
