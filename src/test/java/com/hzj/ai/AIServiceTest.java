package com.hzj.ai;

import com.hzj.ai.assistant.Assistant;
import com.hzj.ai.assistant.SeparateChatAssistant;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.spring.AiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: Hzj
 * @CreateTime: 2025-10-31  20:33
 * @Description: ToDo
 * @Version: 1.0
 */

@SpringBootTest
public class AIServiceTest {

    @Autowired
    private QwenChatModel qwenChatModel;

    @Autowired
    private Assistant assistant;



    @Test
    public void Test01(){
        Assistant assistant = AiServices.create(Assistant.class,qwenChatModel);

        String chat = assistant.chat("你是谁？");

        System.out.println("chat = " + chat);
    }

    @Test
    public void test02(){

        String chat = assistant.chat("你到底是谁");

        System.out.println("chat = " + chat);
    }

    @Test
    public void test03(){

        System.out.println(assistant.chat("你好,我是hzj"));

        System.out.println(assistant.chat("我是谁"));
    }

    @Autowired
    private SeparateChatAssistant separateChatAssistant;

    @Test
    public void test04(){

        System.out.println(separateChatAssistant.chat(1, "我是hzj"));

        System.out.println("用户一" + separateChatAssistant.chat(1,"我是谁"));

        System.out.println("用户二" + separateChatAssistant.chat(2,"我是谁"));

    }

}
