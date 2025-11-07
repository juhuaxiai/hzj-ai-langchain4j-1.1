package com.hzj.ai.tools;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: Hzj
 * @CreateTime: 2025-11-06  16:19
 * @Description: ToDo
 * @Version: 1.0
 */

@Component
public class CalculatorTools {
    @Tool
    double sum(double a,double b){
        System.out.println("调用加法算法");
        return a + b;
    }

    @Tool
    double squareRoot(double x){
        System.out.println("调用取平方根算法");
        return Math.sqrt(x);
    }
}
