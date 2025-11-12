package com.hzj.ai.utills;

/**
 * @Author: Hzj
 * @CreateTime: 2025-11-07  18:38
 * @Description: ToDo
 * @Version: 1.0
 */

/**
 *
 */

import com.alibaba.fastjson2.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpUtil {

    /**
     * 通用 doPost / doGet 方法
     *
     * @param host    主机地址，如：https://api.example.com
     * @param path    路径，如：/v1/query
     * @param method  请求方法：POST / GET
     * @param headers 请求头
     * @param querys  URL 查询参数
     * @param bodys   POST 请求体参数（Map，会自动转 JSON）
     * @return HttpResponse
     * @throws Exception
     */
    public static HttpResponse doPost(
            String host,
            String path,
            String method,
            Map<String, String> headers,
            Map<String, String> querys,
            Map<String, Object> bodys
    ) throws Exception {

        // 拼接 URL
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(host);
        if (!host.endsWith("/") && !path.startsWith("/")) {
            urlBuilder.append("/");
        }
        urlBuilder.append(path);

        // 拼接 query 参数
        if (querys != null && !querys.isEmpty()) {
            urlBuilder.append("?");
            for (Map.Entry<String, String> entry : querys.entrySet()) {
                urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.name()));
                urlBuilder.append("=");
                urlBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name()));
                urlBuilder.append("&");
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }

        String url = urlBuilder.toString();
        CloseableHttpClient client = HttpClients.createDefault();
        HttpRequestBase request;

        // 根据 method 创建请求对象
        if ("POST".equalsIgnoreCase(method)) {
            HttpPost post = new HttpPost(url);

            // 设置 body
            if (bodys != null && !bodys.isEmpty()) {
                JSONObject json = new JSONObject();
                bodys.forEach(json::put);
                StringEntity entity = new StringEntity(json.toJSONString(), "UTF-8");
                entity.setContentType("application/json");
                post.setEntity(entity);
            }
            request = post;

        } else if ("GET".equalsIgnoreCase(method)) {
            request = new HttpGet(url);
        } else {
            throw new IllegalArgumentException("Unsupported method: " + method);
        }

        // 设置 headers
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(request::addHeader);
        }

        // 执行请求
        return client.execute(request);
    }

    /**
     * 简单示例：解析返回 JSON 并获取 code
     */
    public static Integer getCodeFromResponse(HttpResponse response) throws Exception {
        HttpEntity entity = response.getEntity();
        String result = entity != null ? new String(entity.getContent().readAllBytes(), StandardCharsets.UTF_8) : null;
        if (result != null && !result.isEmpty()) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            return jsonObject.getInteger("code");
        }
        return null;
    }
}


