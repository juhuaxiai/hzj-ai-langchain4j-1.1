package com.hzj.ai.tools;

import com.alibaba.fastjson2.JSONObject;
import com.hzj.ai.service.AppointmentService;
import com.hzj.ai.utills.HttpUtil;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Hzj
 * @CreateTime: 2025-11-07  16:34
 * @Description: AI所执行的具体工具类
 * @Version: 1.0
 */

@Slf4j
@Component
public class AppointmentTools {

    @Autowired
    private AppointmentService appointmentService;

    private Long orderId;

    @Tool(
            name = "预约挂号",
            value = """
    执行挂号流程：
    1. 先调用工具方法 queryDepartment(name, date, time, doctorName)，确认是否有号源。
       - 若 queryDepartment 返回 scheduleId，则说明可以预约；
       - 若返回的信息为空或提示无号源，则直接告知用户无法预约。
    2. 再调用 findPatient(name, certificatesNo, phone, sex) 查询用户是否存在。
       - 如果能查到，则使用返回的 patientId；
       - 如果查不到，则调用 createPatient(name, certificatesNo, phone, sex) 创建新用户，并获取新的 patientId。
    3. 最后，调用当前方法 bookAppointmentGh(scheduleId, patientId) 执行预约。
       - 不要重复查询；
       - 不要反复确认；
       - 不要生成不存在的医生或排班。
    """
    )
    public String bookAppointmentGh(
            @P(value = "排班ID") String scheduleId,
            @P(value = "病人ID") Long patientId
    ) throws Exception {
        Assert.notNull(scheduleId, "scheduleId 不能为空");
        Assert.notNull(patientId, "patientId 不能为空");

        String host = "http://localhost:8207";
        String path = "/api/order/orderInfo/auth/submitOrder/"+scheduleId+"/"+patientId;
        String method = "POST";

        Map<String,String> head = new HashMap<>();
        Map<String,String> querys = new HashMap<>();
        Map<String,Object> bodys = new HashMap<>();

        bodys.put("scheduleId",scheduleId);
        bodys.put("patientId",patientId);

        HttpResponse response = HttpUtil.doPost(host,path,method,head,querys,bodys);

        JSONObject data = getResponseData(response);

        Long orderId = data.getLong("orderId");

        Assert.notNull(orderId,"创建预约订单失败");

        return "您已经预约成功了！！！";
    }

    @Tool(name = "取消预约",value = "先根据findAppointment工具方法查询用户的预约是否存在，如果存在则根据其查询出来的orderId删除预约记录并返回取消预约成功，否则返回取消预约失败")
    public String cancelAppointment(
            @P(value = "要取消的预约单ID") Long orderId) throws Exception {
        String host = "http://localhost:8207";
        String path = "/api/order/orderInfo/auth/cancelOrder/" + orderId;
        String method = "POST";

        Map<String,String> head = new HashMap<>();
        Map<String,String> querys = new HashMap<>();
        Map<String,Object> bodys = new HashMap<>();

        bodys.put("orderId",orderId);

        HttpResponse response = HttpUtil.doPost(host,path,method,head,querys,bodys);

        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");

        JSONObject json = JSONObject.parseObject(result);
        String message = json.getString("message");

        return message;
    }

    @Tool(name = "查询预约",value = "根据用户输入的名称，身份证号和日期时间参数，查询预约是否存在，如果存在则返回预约的id")
    public Long findAppointment(
            @P(value = "用户名称") String name,
            @P(value = "用户身份证号") String certificatesNo,
            @P(value = "日期") String date,
            @P(value = "时间，可选值：上午是0、下午是1") Integer time
    ) throws Exception {
        String host = "http://localhost:8160";
        String path = "/api/user/patient/outer/get";
        String method = "POST";

        Map<String,String> head = new HashMap<>();
        Map<String,String> querys = new HashMap<>();
        Map<String,Object> bodys = new HashMap<>();

        bodys.put("name",name);
        bodys.put("certificatesNo",certificatesNo);
        bodys.put("date",date);
        bodys.put("time",time);

        HttpResponse response = HttpUtil.doPost(host,path,method,head,querys,bodys);

        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        log.info("响应的原始内容{}",result);

        // 转为 JSON
        JSONObject json = JSONObject.parseObject(result);

        JSONObject data = json.getJSONObject("data");
        Long orderId1 = data.getLong("orderId");
        orderId = orderId1;
        return orderId;
    }

    @Tool(name = "创建挂号人用户信息",
            value = "根据用户输入的名称，身份证号和日期时间参数创建用户，并返回创建成功后的用户Id")
    public String createPatient(
            @P(value = "用户名称") String name,
            @P(value = "身份证号") String certificatesNo,
            @P(value = "手机号") String phone,
            @P(value = "性别，男性为1，女性为0") Integer sex
    ) throws Exception {

        String host = "http://localhost:8160";
        String path = "/api/user/patient/auth/ai/save";
        String method = "POST";

        Map<String,String> head = new HashMap<>();
        Map<String,String> querys = new HashMap<>();
        Map<String,Object> bodys = new HashMap<>();

        bodys.put("name",name);
        bodys.put("certificatesNo",certificatesNo);
        bodys.put("phone",phone);
        bodys.put("sex",sex);

        HttpResponse response = HttpUtil.doPost(host, path, method, head, querys, bodys);

        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        log.info("响应的原始内容{}",result);


        // 转为 JSON
        JSONObject json = JSONObject.parseObject(result);

        JSONObject data = json.getJSONObject("data");

        Long patientId = data.getLong("patientId");

        String msg = json.getString("message");

        if (patientId == null){
            throw new RuntimeException(msg);
        }

        //返回结构化 JSON，方便 AI 读取字段
        JSONObject resp = new JSONObject();
        resp.put("patientId", patientId);
        resp.put("message", json.getString("message"));

        return resp.toJSONString();
    }

    @Tool(name = "查询挂号人用户信息",
            value = "根据用户输入的名称，身份证号和日期时间参数查询用户，并返回查询成功后的用户Id")
    public String findPatient(
            @P(value = "用户名称") String name,
            @P(value = "身份证号") String certificatesNo,
            @P(value = "手机号") String phone,
            @P(value = "性别，男性为1，女性为0") Integer sex
    ) throws Exception {

        String host = "http://localhost:8160";
        String path = "/api/user/patient/auth/ai/find";
        String method = "POST";

        Map<String,String> head = new HashMap<>();
        Map<String,String> querys = new HashMap<>();
        Map<String,Object> bodys = new HashMap<>();

        bodys.put("name",name);
        bodys.put("certificatesNo",certificatesNo);
        bodys.put("phone",phone);
        bodys.put("sex",sex);

        HttpResponse response = HttpUtil.doPost(host, path, method, head, querys, bodys);

        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        log.info("响应的原始内容{}",result);

        // 转为 JSON
        JSONObject json = JSONObject.parseObject(result);

        JSONObject data = json.getJSONObject("data");

        Long patientId = data.getLong("patientId");

        //返回结构化 JSON，方便 AI 读取字段
        JSONObject resp = new JSONObject();
        resp.put("patientId", patientId);
        resp.put("message", json.getString("message"));

        return resp.toJSONString();
    }


    @Tool(name = "查询是否有号源", value = "根据科室名称，日期，时间和医生姓名查询是否有号源，并返回给用户")
    public String queryDepartment(
            @P(value = "科室名称") String name,
            @P(value = "日期") String date,
            @P(value = "时间，可选值：上午是0、下午是1") Integer time,
            @P(value = "医生名称", required = false) String doctorName
    ){
        System.out.println("查询是否有号源");
        System.out.println("科室名称" + name);
        System.out.println("日期" + date);
        System.out.println("时间" + time);
        System.out.println("医生名称" + doctorName);

        //TODO 维护医生的排班信息：

        //如果没有指定医生名字，则根据其他条件查询是否有可以预约的医生（有返回true，否则返回false）；

        //如果指定了医生名字，则判断医生是否有排班（没有排班返回false）

        //如果有排班，则判断医生排班时间段是否已约满（约满返回false，有空闲时间返回true）

        String host = "http://localhost:8201";
        String path = "/api/hosp/select/selectSchedule";
        String method = "POST";

        Map<String,String> head = new HashMap<>();
        Map<String,String> querys = new HashMap<>();
        Map<String,Object> bodys = new HashMap<>();

        bodys.put("name",name);
        bodys.put("doctorName",doctorName);
        bodys.put("date",date);
        bodys.put("time",time);

        try {
            HttpResponse response = HttpUtil.doPost(host, path, method, head, querys, bodys);
            // 获取响应体
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            log.info("响应的原始内容{}",result);

            // 转为 JSON
            JSONObject json = JSONObject.parseObject(result);

            // 取出 code 和 message
            Integer code = json.getInteger("code");
            JSONObject data = json.getJSONObject("data");
            String msg = json.getString("message");
            log.info("响应代码{}",code);
            log.info("响应信息{}",msg);

            // 取排班ID → 医生姓名 Map
            if (data != null) {
                String scheduleId = data.getString("scheduleId");
                return scheduleId;
            }
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            return "服务器链接失败，稍后再试";
        }
    }

    public JSONObject getResponseData(HttpResponse response) throws IOException {
        // 获取响应体
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        log.info("响应的原始内容 {}", result);

        JSONObject json = JSONObject.parseObject(result);

        Integer code = json.getInteger("code");
        String msg = json.getString("message");

        JSONObject data = json.getJSONObject("data");

        log.info("响应代码 {}", code);
        log.info("响应信息 {}", msg);
        log.info("data 内容 {}", data);

        return data;
    }
}
