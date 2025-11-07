package com.hzj.ai;

import com.hzj.ai.assistant.Assistant;
import com.hzj.ai.assistant.SeparateChatAssistant;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.spring.AiService;
import org.bson.types.ObjectId;
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


        System.out.println(separateChatAssistant.chat(16465484648L,"我是hzj"));

        System.out.println("用户一" + separateChatAssistant.chat(16465484648L,"我是谁"));

        System.out.println("用户二" + separateChatAssistant.chat(1654164849749L,"我是谁"));

    }

    @Test
    public void test05(){


        String chat = separateChatAssistant.chat(156416489464L, "带派不老铁铁，今天是几号啊");
        System.out.println("chat = " + chat);
    }

    @Test
    public void test06(){
        System.out.println("assistant.chat(\"你好哇\") = " + assistant.chat("你家是哪里的"));

    }

    @Test
    public void test07(){
        String id1 = new ObjectId().toHexString();

        String answer1 = separateChatAssistant.chat2(79854L, "我是张三");
        System.out.println(answer1);
        String answer2 = separateChatAssistant.chat2(165141L, "我是谁");
        System.out.println(answer2);
    }

    @Test
    public void test08(){
        String chat3 = separateChatAssistant.chat3(156649194L, "今晚造点什么吃好呢", "王虎", 21);
        System.out.println("chat3 = " + chat3);
    }



}
