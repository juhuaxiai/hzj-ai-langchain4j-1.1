# 硅谷小智 - 医疗智能助手



基于 Spring Boot 和 LangChain4j 构建的医疗智能助手系统，集成 RAG 检索、工具调用、聊天记忆等功能，为医院提供智能客服和医疗伴诊服务。

B站学习网站：https://www.bilibili.com/video/BV1cpLTz1EVp?spm_id_from=333.788.videopod.episodes&vd_source=243ca8690d4c74815ab1b229bfb6768c

## 🚀 项目特性

- **智能对话**: 基于阿里通义千问大模型的自然语言对话
- **RAG 检索**: 基于向量数据库的医疗知识检索
- **工具调用**: 支持预约挂号、取消预约等医疗业务操作
- **聊天记忆**: MongoDB 持久化聊天历史记录
- **流式输出**: 实时流式响应，提升用户体验
- **多模型支持**: 支持 DeepSeek、通义千问等多种大模型

## 📁 项目结构

```java
langchain4j/
├── src/main/java/com/perry/langchain4j/
│   ├── Langchain4jApplication.java          # 主启动类
│   ├── assistant/                           # AI助手模块
│   │   ├── XiaozhiAgent.java               # 小智智能助手
│   │   ├── MemoryAssistant.java            # 带记忆的助手
│   │   ├── SeparateChatAssistant.java      # 独立聊天助手
│   │   └── Assistant.java                  # 基础助手接口
│   ├── config/                             # 配置模块
│   │   ├── XiaozhiAgentConfig.java         # 小智助手配置
│   │   ├── MemoryChatAssistantConfig.java  # 聊天记忆配置
│   │   └── SeparateChatAssistantConfig.java # 独立聊天配置
│   ├── controller/                         # 控制器层
│   │   └── XiaozhiController.java          # 小智控制器
│   ├── domain/                             # 领域模型
│   │   ├── ChatForm.java                   # 聊天表单
│   │   └── ChatMessages.java               # 聊天消息
│   ├── entity/                             # 实体类
│   │   └── Appointment.java                # 预约实体
│   ├── mapper/                             # 数据访问层
│   │   └── AppointmentMapper.java          # 预约数据访问
│   ├── service/                            # 业务服务层
│   │   ├── AppointmentService.java         # 预约服务接口
│   │   └── impl/
│   │       └── AppointmentServiceImpl.java # 预约服务实现
│   ├── store/                              # 存储模块
│   │   └── MongoChatMemoryStore.java       # MongoDB聊天记忆存储
│   └── tools/                              # 工具模块
│       ├── AppointmentTools.java           # 预约工具
│       └── CalculatorTools.java            # 计算器工具
├── src/main/resources/
│   ├── application.yml                     # 应用配置
│   ├── xiaozhi-prompt-template.txt         # 小智提示词模板
│   ├── system_message.txt                  # 系统消息
│   ├── system_message3.txt                 # 系统消息3
│   ├── mapper/
│   │   └── AppointmentMapper.xml           # MyBatis映射文件
│   └── static/                             # 静态资源
├── src/test/java/                          # 测试代码
│   └── com/perry/langchain4j/
│       ├── AiServiceTest.java              # AI服务测试
│       ├── ChatMemoryTest.java             # 聊天记忆测试
│       ├── EmbeddingTest.java              # 向量嵌入测试
│       ├── RAGTest.java                    # RAG检索测试
│       ├── ToolsTest.java                  # 工具测试
│       ├── MysqlTest.java                  # MySQL测试
│       ├── MongoCrudTest.java              # MongoDB测试
│       ├── PromptTest.java                 # 提示词测试
│       └── Langchain4jApplicationTests.java # 应用测试
└── doc/                                    # 文档和资源
    ├── rag/                                # RAG知识库文档
    ├── sql/                                # 数据库脚本
    └── front-end-code/                     # 前端代码
```



## 🛠️ 技术栈



- **框架**: Spring Boot 3.2.6
- **AI框架**: LangChain4j 1.0.0-beta3
- **大模型**: 阿里通义千问 (Qwen-Max/Qwen-Plus)
- **向量模型**: 通义千问文本向量 (text-embedding-v3)
- **数据库**: MySQL 8.0 + MongoDB
- **ORM**: MyBatis-Plus 3.5.11
- **API文档**: Knife4j 4.3.0
- **Java版本**: JDK 17
- **向量存储**: Redis 7.2 (todo,后续集成)

## ⚙️ 环境配置



### 1. 环境变量配置



```bash
# 阿里云通义千问API密钥
export DASH_SCOPE_API_KEY=your_dashscope_api_key_here

# 可选：DeepSeek API密钥（如需使用DeepSeek模型）
export DEEP_SEEK_API_KEY=your_deepseek_api_key_here
```



### 2. 数据库配置



#### MySQL 配置



```SQL
-- 创建数据库
CREATE DATABASE guiguxiaozhi CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 执行初始化脚本
-- 参考: doc/sql/init.sql
```



#### MongoDB 配置



```bash
# 启动MongoDB服务
mongod --dbpath /path/to/data/db

# 数据库名称: chat_memory_db
```



### 3. 应用配置 (application.yml)



```yaml
server:
  port: 8080

spring:
  application:
    name: langchain4j-demo
  data:
    mongodb:
      uri: mongodb://localhost:27017/chat_memory_db
  datasource:
    url: jdbc:mysql://localhost:3306/guiguxiaozhi?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&allowPublicKeyRetrieval=true&useSSL=false&allowMultiQueries=true&useJDBCCompliant
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver

langchain4j:
  community:
    dashscope:
      chat-model:
        api-key: ${DASH_SCOPE_API_KEY}
        model-name: qwen-max
      embedding-model:
        api-key: ${DASH_SCOPE_API_KEY}
        model-name: text-embedding-v3
      streaming-chat-model:
        api-key: ${DASH_SCOPE_API_KEY}
        model-name: qwen-plus

logging:
  level:
    root: info
    com.perry.langchain4j: debug
    dev.langchain4j: debug

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```



## 🚀 快速开始



### 1. 克隆项目



```bash
git clone <repository-url>
cd langchain4j
```



### 2. 配置环境变量



```bash
export DASH_SCOPE_API_KEY=your_api_key_here
```



### 3. 启动数据库服务



```bash
# 启动MySQL
sudo service mysql start

# 启动MongoDB
mongod --dbpath /path/to/data/db
```



### 4. 初始化数据库



```bash
# 执行MySQL初始化脚本
mysql -u root -p guiguxiaozhi < doc/sql/init.sql
```



### 5. 启动应用



```bash
# 编译项目
mvn clean compile

# 启动应用
mvn spring-boot:run
```



### 6. 验证启动



```bash
# 检查应用状态
curl http://localhost:8080/actuator/health

# 访问API文档
open http://localhost:8080/doc.html
```



## 📡 API 接口



### 小智对话接口



```json
# 流式对话
POST /xiaozhi/chat
Content-Type: application/json

{
  "memoryId": 123,
  "message": "你好，我想预约挂号"
}
```



### 响应格式



- **流式响应**: `text/stream; charset=utf-8`
- **实时输出**: 支持流式文本输出

## 🔧 核心功能



### 1. 智能对话 (XiaozhiAgent)



- **系统角色**: 北京协和医院智能客服"硅谷小智"

- 功能范围

	:

	- 医疗咨询和建议
	- AI分导诊
	- 智能挂号助手

- **特色**: 流式输出、聊天记忆、工具调用

### 2. RAG 知识检索



- **知识库**: 医院信息、科室信息、神经内科等医疗文档
- **检索方式**: 基于向量相似度的语义检索
- **存储**: 内存向量存储 (InMemoryEmbeddingStore)

### 3. 工具调用 (Tools)



- **预约挂号**: 智能查询号源、预约挂号
- **取消预约**: 查询并取消已有预约
- **号源查询**: 根据科室、日期、时间、医生查询可用号源

### 4. 聊天记忆



- **存储**: MongoDB 持久化存储
- **策略**: 消息窗口记忆 (MessageWindowChatMemory)
- **限制**: 最大10条消息

## 🧪 测试



### 运行所有测试



```bash
mvn test
```



### 运行特定测试



```bash
# RAG检索测试
mvn test -Dtest=RAGTest

# 聊天记忆测试
mvn test -Dtest=ChatMemoryTest

# 工具测试
mvn test -Dtest=ToolsTest
```



### 测试覆盖范围



- AI服务集成测试
- 聊天记忆功能测试
- 向量嵌入测试
- RAG检索测试
- 工具调用测试
- 数据库操作测试

## 📚 知识库文档



项目包含以下医疗知识文档：

- `doc/rag/医院信息.md` - 医院基本信息
- `doc/rag/科室信息.md` - 科室详细信息
- `doc/rag/神经内科.md` - 神经内科专业信息

## 🔍 监控和调试



### 应用监控

```bash
# 健康检查
GET /actuator/health

# 应用信息
GET /actuator/info
```



### 日志配置



- **开发环境**: 开启详细日志
- **生产环境**: 建议调整日志级别

### API文档



- **访问地址**: http://localhost:8080/doc.html
- **功能**: 接口文档、在线调试

## 🚨 常见问题



### 1. API密钥配置问题



**问题**: 无法连接到大模型服务 **解决**:

- 检查环境变量是否正确设置
- 验证API密钥有效性
- 确认网络连接正常

### 2. 数据库连接问题



**问题**: 应用启动失败，数据库连接错误 **解决**:

- 检查数据库服务是否启动
- 验证数据库连接配置
- 确认数据库用户权限

### 3. 向量检索问题



**问题**: RAG检索结果不准确 **解决**:

- 检查知识库文档是否正确加载
- 验证向量模型配置
- 调整检索参数

Copyright (c) 2025 硅谷小智
