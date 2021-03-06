package com.xiji.cashloan.rule.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.xiji.cashloan.rule.service.BorrowRuleEngineService;
import com.xiji.cashloan.rule.service.RuleEngineConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tool.util.StringUtil;

import com.github.pagehelper.Page;
import com.xiji.cashloan.core.common.context.Constant;
import com.xiji.cashloan.core.common.util.JsonUtil;
import com.xiji.cashloan.core.common.util.RdPage;
import com.xiji.cashloan.core.common.util.ServletUtils;
import com.xiji.cashloan.core.common.web.controller.BaseController;
import com.xiji.cashloan.rule.domain.BorrowRuleConfig;
import com.xiji.cashloan.rule.domain.RuleEngine;
import com.xiji.cashloan.rule.service.BorrowRuleConfigService;
import com.xiji.cashloan.rule.service.RuleEngineService;

/**
 * 规则引擎管理Controller
 *
 * @author wnb
 * @version 1.0.0
 * @date 2018/11/27
 *
 *
 * <p>
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@Controller
@Scope("prototype")
public class RuleEngineController extends BaseController {

	@Resource
	private RuleEngineService ruleEngineService;
	@Resource
	private BorrowRuleConfigService borrowRuleConfigService;
	@Resource
	private BorrowRuleEngineService borrowRuleEngineService;
	@Resource
	private RuleEngineConfigService ruleEngineConfigService;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(RuleEngineController.class);
	
	/**
	 * 查询规则列表含分页
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/modules/manage/rule/list.htm")
	public void list(@RequestParam(value="search",required=false) String search,
			@RequestParam(value = "currentPage") int currentPage,
			@RequestParam(value = "pageSize") int pageSize){
		Map<String, Object> params = JsonUtil.parse(search, Map.class);
		Page<RuleEngine> page = ruleEngineService.findListByPage(params,currentPage,pageSize);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put(Constant.RESPONSE_DATA, page);
		result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
		result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
		result.put(Constant.RESPONSE_CODE_MSG, "获取成功");
		ServletUtils.writeToResponse(response,result);
	}
	
	/**
	 * 修改状态
	 */
	@RequestMapping("/modules/manage/rule/modifyState.htm")
	public void modifyState(@RequestParam(value = "state") String state,
			@RequestParam(value = "id")  Long id){
		int resCode=0;
		if (StringUtil.isNotBlank(id)&&StringUtil.isNotBlank(state)) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id",id);
			map.put("state",state);
			resCode=ruleEngineService.updateByRule(map);
			if(state.equals("20")){
				//关联的借款规则信息修改
				Map<String,Object> paramMap=new HashMap<String, Object>();
				paramMap.put("ruleId",id);
				List<BorrowRuleConfig> borrowRuleConfig = borrowRuleConfigService.findBorrowRuleId(paramMap);
				RuleEngine  rule = ruleEngineService.findById(id);
				for(BorrowRuleConfig br:borrowRuleConfig){
					int count=br.getConfigSort()-rule.getConfigCount();//ruleCount赋值到ConfigSort 
					if(count>0){
						paramMap=new HashMap<String, Object>();
						paramMap.put("id",br.getBorrowRuleId());
						paramMap.put("ruleCount",count);
						borrowRuleEngineService.updateSelective(paramMap);
					}else{
						borrowRuleEngineService.deleteById(br.getBorrowRuleId());
					}
					
				}
				
				 
				map=new HashMap<String, Object>();
				map.put("ruleId",id);
				borrowRuleConfigService.deleteByMap(map);
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		if (resCode>0) {
			result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
			result.put(Constant.RESPONSE_CODE_MSG, "成功");
		}else{
			result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
			result.put(Constant.RESPONSE_CODE_MSG, "失败");
		}
		ServletUtils.writeToResponse(response,result);
	}
	/**
	 * 查询所有规则及其配置信息
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/modules/manage/rule/allList.htm")
	public void list(@RequestParam(value="search",required=false) String search){
		Map<String, Object> params = JsonUtil.parse(search, Map.class);
		List<Map<String, Object>> list = ruleEngineService.findAllRule(params);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put(Constant.RESPONSE_DATA, list);
		result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
		result.put(Constant.RESPONSE_CODE_MSG, "获取成功");
		ServletUtils.writeToResponse(response,result);
	}
	
}
