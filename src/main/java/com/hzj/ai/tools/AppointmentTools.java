package com.hzj.ai.tools;

import com.alibaba.fastjson2.JSONObject;
import com.hzj.ai.entity.Appointment;
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


import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Hzj
 * @CreateTime: 2025-11-07  16:34
 * @Description: ToDo
 * @Version: 1.0
 */

@Slf4j
@Component
public class AppointmentTools {

    @Autowired
    private AppointmentService appointmentService;

    @Tool(name="预约挂号", value = "根据参数，先执行工具方法queryDepartment查询是否可预约，" +
            "并直接给用户回答是否可预约，并让用户确认所有预约信息，用户确认后再进行预约。" +
            "如果用户没有提供具体的医生姓名，请从向量存储中找到一位医生。")
    public String bookAppointment(Appointment appointment){
        Appointment appointmentDb = appointmentService.getOne(appointment);
        if (appointmentDb == null){
            appointment.setId(null);//防止大模型幻觉设置了id
            if(appointmentService.save(appointment)){
                return "预约成功，并返回预约详情";
            }else{
                return "预约失败";
            }
        }
        return "您在相同的时间和科室已经有了预约";
    }

    @Tool(name = "取消预约",value = "根据参数，查询预约是否存在，如果存在则删除预约记录并返回取消预约成功，否则返回取消预约失败")
    public String cancelAppointment(Appointment appointment){
        Appointment appointmentDb = appointmentService.getOne(appointment);
        if (appointmentDb != null){
            if (appointmentService.removeById(appointmentDb.getId())){
                //删除预约挂号
                return "取消预约挂号成功";
            }else {
                return "取消预约失败";
            }
        }
        return "您没有预约记录，请核对预约科室和时间";
    }


    @Tool(name = "查询是否有号源", value = "根据科室名称，日期，时间和医生查询是否有号源，并返回给用户")
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
        Map<String,String> bodys = new HashMap<>();

        bodys.put("name",name);
        bodys.put("doctorName",doctorName);
        bodys.put("date",date);
        bodys.put("time",time.toString());

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
            String msg = json.getString("message");
            log.info("响应代码{}",code);
            log.info("响应信息{}",msg);
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            return "服务器链接失败，稍后再试";
        }
    }
}
