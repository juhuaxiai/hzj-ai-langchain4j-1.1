package com.hzj.ai.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.Documented;

/**
 * @Author: Hzj
 * @CreateTime: 2025-11-01  10:36
 * @Description: 测试代码实体类
 * @Version: 1.0
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("chat_message")
public class ChatMessages {
    @Id
    private ObjectId id;

    private Integer messageId;

    private String content;
}
