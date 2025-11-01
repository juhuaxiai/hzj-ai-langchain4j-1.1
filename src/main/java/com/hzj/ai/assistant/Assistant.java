package com.hzj.ai.assistant;

import dev.langchain4j.service.spring.AiService;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

/**
 * @Author: Hzj
 * @CreateTime: 2025-10-31  20:34
 * @Description: ToDo
 * @Version: 1.0
 */

@AiService(wiringMode = EXPLICIT, chatModel = "qwenChatModel",chatMemory = "chatMemory")
public interface Assistant {

    String chat(String msg);
}
