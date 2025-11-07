package com.hzj.ai;

import com.hzj.ai.assistant.SeparateChatAssistant;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: Hzj
 * @CreateTime: 2025-11-06  16:29
 * @Description: ToDo
 * @Version: 1.0
 */

@SpringBootTest
public class ToolsTest {

    @Autowired
    private SeparateChatAssistant separateChatAssistant;

    @Test
    public void Test01(){
        String answer = separateChatAssistant.chat(64849616464L, "1+2等于几，475695037565的平方根是多少？");
        System.out.println("answer = " + answer);
    }

}
