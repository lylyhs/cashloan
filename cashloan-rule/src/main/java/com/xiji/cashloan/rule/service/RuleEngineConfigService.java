package com.xiji.cashloan.rule.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.xiji.cashloan.core.common.service.BaseService;
import com.xiji.cashloan.rule.domain.RuleEngineConfig;

/**
 * 规则引擎管理Service
 *
 * @author wnb
 * @version 1.0.0
 * @date 2018/11/27
 * Copyright 杭州融都科技股份有限公司  arc All Rights Reserved
 * 官方网站：www.xiji.com
 * 
 * 未经授权不得进行修改、复制、出售及商业使用
 */
public interface RuleEngineConfigService extends BaseService<RuleEngineConfig, Long>{

	List<RuleEngineConfig> findByMap(Map<String, Object> search);

	List<Map<String, Object>> findColumnByName(Map<String, Object> map);

	List<Map<String, Object>> findTable();

	int updateSelective(Map<String, Object> map);

	 
	List<Map<String, Object>> findAllInfo(Map<String, Object> map);

	int saveOrUpate(Map<String, Object> rule, List list, String datalist,
        HttpServletRequest request);
	/**
	 * 根据表名查询数据表信息
	 * @param paramMap
	 * @return List<Map<String, Object>>
	 */
	List<Map<String, Object>> findTableByName(Map<String, Object> paramMap);
}
