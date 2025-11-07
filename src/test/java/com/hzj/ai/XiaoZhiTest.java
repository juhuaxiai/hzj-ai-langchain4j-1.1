package com.hzj.ai;

import com.hzj.ai.assistant.XiaozhiAgent;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: Hzj
 * @CreateTime: 2025-11-06  15:06
 * @Description: ToDo
 * @Version: 1.0
 */

@SpringBootTest
public class XiaoZhiTest {
    @Autowired
    XiaozhiAgent xiaozhiAgent;

    @Test
    public void test01(){

        System.out.println("xiaozhiAgent:" + xiaozhiAgent.chat(1L, "你是谁"));

    }

}
