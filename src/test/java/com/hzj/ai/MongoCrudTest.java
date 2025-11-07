//package com.hzj.ai;
//
//import com.hzj.ai.bean.ChatMessages;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//
///**
// * @Author: Hzj
// * @CreateTime: 2025-11-01  10:42
// * @Description: ToDo
// * @Version: 1.0
// */
//
//@SpringBootTest
//public class MongoCrudTest {
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//
//    @Test
//    public void test01(){
//        ChatMessages chatMessages = new ChatMessages();
//        chatMessages.setContent("新建聊天记录列表");
//        mongoTemplate.insert(chatMessages);
//    }
//
//    @Test
//    public void test02(){
//        ChatMessages byId = mongoTemplate.findById("69057ba921f6b90c8f8f86cd", ChatMessages.class);
//        System.out.println(byId);
//    }
//
//    @Test
//    public void test03(){
//        Criteria criteria = Criteria.where("_id").is("69057ba921f6b90c8f8f86cd");
//        Query query = new Query(criteria);
//
//        Update update = new Update();
//        update.set("context","尝尝毒药的味道");
//        mongoTemplate.upsert(query,update, ChatMessages.class);
//
//        //这个后面会创建一个Class对象的调用，因为这个是通过
//    }
//
//    @Test
//    public void test04(){
//        Criteria criteria = Criteria.where("_id").is("100");
//        Query query = new Query(criteria);
//
//        Update update = new Update();
//        update.set("context","我的力量强大无比");
//        mongoTemplate.upsert(query,update, ChatMessages.class);
//    }
//
//}
