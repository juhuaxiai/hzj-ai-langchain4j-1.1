package com.hzj.ai.assistant;

import dev.langchain4j.service.UserMessage;
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

    @UserMessage("你是我的好朋友，请用粤语回答问题，并且添加一些表情符号。 {{it}}")//it表示唯一占位符
    String chat(String msg);
}
