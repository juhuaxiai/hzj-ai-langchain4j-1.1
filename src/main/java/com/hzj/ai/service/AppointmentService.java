package com.hzj.ai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzj.ai.entity.Appointment;

/**
 * @Author: Hzj
 * @CreateTime: 2025-11-07  15:28
 * @Description: 预约挂号服务端
 * @Version: 1.0
 */

public interface AppointmentService extends IService<Appointment> {
    Appointment getOne(Appointment appointment);
}
