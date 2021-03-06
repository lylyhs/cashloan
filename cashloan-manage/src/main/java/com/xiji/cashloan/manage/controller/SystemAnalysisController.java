package com.xiji.cashloan.manage.controller;

import java.util.HashMap;
import java.util.List;
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
import com.xiji.cashloan.cl.model.OverdueAnalisisModel;
import com.xiji.cashloan.cl.model.RepayAnalisisModel;
import com.xiji.cashloan.cl.service.SystemAnalysisService;

/**
 * 运营分析
 * @author wnb
 * @version 1.0.0
 * @date 2018/11/27
 *
 *
 * 
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@Controller
@Scope("prototype")
public class SystemAnalysisController extends ManageBaseController {

	@Resource
	private SystemAnalysisService systemAnalysisService;
	
	
	/**
	 * 每日还款统计
	 * @param response
	 * @param search
	 * @param current
	 * @param pageSize
	 * @throws Exception
	 */
	@RequestMapping(value = "/modules/manage/sysanalysis/dayRepay.htm")
	public void dayRepay(HttpServletResponse response,
			@RequestParam("search")String search) throws Exception {
		Map<String, Object> params = JSONObject.parseObject(search);
		List<RepayAnalisisModel> list = systemAnalysisService.dayRepayAnalisis(params);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put(Constant.RESPONSE_DATA, list);
		result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
		result.put(Constant.RESPONSE_CODE_MSG, "查询成功");
		ServletUtils.writeToResponse(response,result);
	}
	
	
	/**
	 * 每月还款统计
	 * @param response
	 * @param search
	 * @param current
	 * @param pageSize
	 * @throws Exception
	 */
	@RequestMapping(value = "/modules/manage/sysanalysis/monthRepay.htm")
	public void monthRepay(HttpServletResponse response,
			@RequestParam("search")String search) throws Exception {
		Map<String, Object> params = JSONObject.parseObject(search);
		List<RepayAnalisisModel> list = systemAnalysisService.monthRepayAnalisis(params);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put(Constant.RESPONSE_DATA, list);
		result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
		result.put(Constant.RESPONSE_CODE_MSG, "查询成功");
		ServletUtils.writeToResponse(response,result);
	}
	
	/**
	 * 每月逾期分析
	 * @param response
	 * @param search
	 * @param current
	 * @param pageSize
	 * @throws Exception
	 */
	@RequestMapping(value = "/modules/manage/sysanalysis/overdue.htm")
	public void overdue(HttpServletResponse response,
			@RequestParam("search")String search,
			@RequestParam("current")Integer current,
			@RequestParam("pageSize")Integer pageSize) throws Exception {
		Map<String, Object> params = JSONObject.parseObject(search);
		Page<OverdueAnalisisModel> page = systemAnalysisService.overdueAnalisis(params, current, pageSize);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put(Constant.RESPONSE_DATA, page);
		result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
		result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
		result.put(Constant.RESPONSE_CODE_MSG, "查询成功");
		ServletUtils.writeToResponse(response,result);
	}
	
}
