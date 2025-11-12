package com.hzj.ai.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Patient
 * </p>
 *
 * @author qy
 */
@Data
public class Patient{

	private String name;

	//身份证号
	private String certificatesNo;

	//性别
	private Integer sex;

	//手机
	private String phone;


}

