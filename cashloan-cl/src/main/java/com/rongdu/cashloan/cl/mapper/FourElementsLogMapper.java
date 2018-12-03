package com.rongdu.cashloan.cl.mapper;

import java.util.Map;

import com.rongdu.cashloan.core.common.mapper.BaseMapper;
import com.rongdu.cashloan.core.common.mapper.RDBatisDao;
import com.rongdu.cashloan.cl.domain.FourElementsLog;

/**
 * 四要素认证记录Dao
 * 
 * @author xx
 * @version 1.0.0
 * @date 2017-08-31 17:46:26
 * Copyright 杭州融都科技股份有限公司  cashloan All Rights Reserved
 * 官方网站：www.erongdu.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@RDBatisDao
public interface FourElementsLogMapper extends BaseMapper<FourElementsLog, Long> {
	/**
	 * 统计
	 * @param  paramMap
	 * @return
	 */
	int count(Map<String, Object> paramMap);

}
