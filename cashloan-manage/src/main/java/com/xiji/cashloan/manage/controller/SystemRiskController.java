package com.xiji.cashloan.manage.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.xiji.cashloan.core.common.context.Constant;
import com.xiji.cashloan.core.common.util.RdPage;
import com.xiji.cashloan.core.common.util.ServletUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.xiji.cashloan.cl.model.DayPassApr;
import com.xiji.cashloan.cl.model.SystemDayData;
import com.xiji.cashloan.cl.service.SystemRcService;

/**
 * 风控数据
 * 1、平台数据日报
 * @author wnb
 * @version 1.0.0
 * @date 2018/11/27
 * Copyright 杭州融都科技股份有限公司 现金贷  All Rights Reserved
 * 官方网站：www.xiji.com
 * 
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@Controller
@Scope("prototype")
public class SystemRiskController extends ManageBaseController {

	@Resource
	private SystemRcService systemRcService;
	
	/**
	 * 平台数据日报
	 * @param response
	 * @param current
	 * @param pageSize
	 * @param search
	 * @throws Exception
	 */
	@RequestMapping(value = "/modules/manage/rc/day.htm")
	public void dayData(HttpServletResponse response,
			@RequestParam(value="current")Integer current,
			@RequestParam(value="pageSize")Integer pageSize,
			@RequestParam("search")String search) throws Exception {
		Map<String,Object> params = JSONObject.parseObject(search);
		Page<SystemDayData> page = systemRcService.findDayData(params,current, pageSize);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put(Constant.RESPONSE_DATA, page);
		result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
		result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
		result.put(Constant.RESPONSE_CODE_MSG, "查询成功");
		ServletUtils.writeToResponse(response,result);
	}
	
	/**
	 * 每日通过率
	 * @param response
	 * @param current
	 * @param pageSize
	 * @param search
	 * @throws Exception
	 */
	@RequestMapping(value = "/modules/manage/rc/dayApr.htm")
	public void dayApr(HttpServletResponse response,
			@RequestParam(value="current")Integer current,
			@RequestParam(value="pageSize")Integer pageSize,
			@RequestParam("search")String search) throws Exception {
		Map<String,Object> params = JSONObject.parseObject(search);
		Page<DayPassApr> page = systemRcService.findDayApr(params,current, pageSize);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put(Constant.RESPONSE_DATA, page);
		result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
		result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
		result.put(Constant.RESPONSE_CODE_MSG, "查询成功");
		ServletUtils.writeToResponse(response,result);
	}
}
