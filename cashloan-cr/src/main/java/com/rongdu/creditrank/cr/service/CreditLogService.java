package com.rongdu.creditrank.cr.service;

import java.util.Map;

import com.github.pagehelper.Page;
import com.rongdu.cashloan.core.common.service.BaseService;
import com.rongdu.creditrank.cr.domain.CreditLog;
import com.rongdu.creditrank.cr.model.CreditLogModel;

/**
 * 授信额度记录Service
 * 
 * @author lyang
 * @version 1.0.0
 * @date 2016-12-16 10:31:23
 * Copyright 杭州融都科技股份有限公司  arc All Rights Reserved
 * 官方网站：www.erongdu.com
 * 
 * 未经授权不得进行修改、复制、出售及商业使用
 */
public interface CreditLogService extends BaseService<CreditLog, Long>{

	/**
	 * 保存数据
	 * @param log
	 * @return
	 */
	int save(CreditLog log);
	
	/**
	 * 分页查询
	 * @param searchMap
	 * @param current
	 * @param pageSize
	 * @return
	 */
	Page<CreditLogModel> page(Map<String,Object> searchMap,int current,int pageSize);
}
