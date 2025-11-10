package com.hzj.ai.controller;

import com.hzj.ai.assistant.XiaozhiAgent;
import com.hzj.ai.bean.ChatForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @Author: Hzj
 * @CreateTime: 2025-11-06  15:16
 * @Description: ToDo
 * @Version: 1.0
 */

@Tag(name = "硅谷小智")
@RestController
@RequestMapping("/xiaozhi")
public class XiaozhiController {

    @Autowired
    XiaozhiAgent xiaozhiAgent;

    @Operation(summary = "对话")
    @PostMapping(value = "/chat",produces = "text/stream;charset=utf-8")
    public Flux<String> chat(@RequestBody ChatForm chatForm){
        return xiaozhiAgent.chat(chatForm.getMemoryId(),chatForm.getMessage());
    }
}
