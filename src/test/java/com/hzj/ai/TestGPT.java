package com.hzj.ai;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: Hzj
 * @CreateTime: 2025-10-31  16:47
 * @Description: ToDo
 * @Version: 1.0
 */

@SpringBootTest
public class TestGPT {

    @Test
    public void test01(){
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .build();

        String answer = model.chat("你好");
        System.out.println("answer = " + answer);

        String answer2 = model.chat("你是谁");
        System.out.println("answer = " + answer2);
    }

    @Autowired
    OpenAiChatModel openAiChatModel;


    @Test
    public void test02(){
        String chat = openAiChatModel.chat("你是谁");
        System.out.println("chat = " + chat);
    }

    @Autowired
    private OllamaChatModel ollamaChatModel;

    @Test
    public void test03(){
        String chat = ollamaChatModel.chat("你是谁?");
        System.out.println("chat = " + chat);
    }

    @Autowired
    private QwenChatModel qwenChatModel;

    @Test
    public void test04(){
        String chat = qwenChatModel.chat("你是谁？");

        System.out.println("chat = " + chat);
    }

    @Test
    public void testDashScopeMax(){
        WanxImageModel wanxImageModel = WanxImageModel.builder()
                .modelName("wanx2.1-t2i-plus")
                .apiKey(System.getenv("DASH_SCOPE_API_KEY"))
                .build();
        Response<Image> response = wanxImageModel.generate("古风庭院雅集：一座宁静优美的中式庭院，亭台楼阁错落有致。池塘中，荷花盛开，荷叶田田，红色的锦鲤在水中悠然游动。庭院中央的石桌上摆放着笔墨纸砚和茶具，几位身着古装的文人雅士围坐在一起，或挥毫泼墨，或品茗吟诗。一位身着淡蓝色长袍的书生正低头沉思，旁边的一位女子手持团扇，微笑着看向他。" +
                "庭院四周，翠竹环绕，石径通幽，空气中弥漫着淡淡的花香，营造出一种闲适雅致的氛围。");

        System.out.println("response.content().url() = " + response.content().url());
    }

}
