package com.rongdu.creditrank.cr.mapper;

import java.util.List;
import java.util.Map;

import com.rongdu.cashloan.core.common.mapper.BaseMapper;
import com.rongdu.cashloan.core.common.mapper.RDBatisDao;
import com.rongdu.creditrank.cr.domain.Credit;
import com.rongdu.creditrank.cr.model.CreditModel;

/**
 * 授信额度管理Dao
 * 
 * @author lyang
 * @version 1.0.0
 * @date 2016-12-15 15:47:24
 * Copyright 杭州融都科技股份有限公司  arc All Rights Reserved
 * 官方网站：www.erongdu.com
 * 
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@RDBatisDao
public interface CreditMapper extends BaseMapper<Credit,Long> {
	
	/**
	 * 更新额度
	 * @param map
	 * @return
	 */
	int updateAmount(Map<String, Object> map);

	/**
	 * 列表查询返回CreditModel
	 * @param searchMap
	 * @return
	 */
	List<CreditModel> page(Map<String,Object> searchMap);

	/**
	 * 根据consumerNo查询
	 * @param consumerNo
	 * @return
	 */
	Credit findByConsumerNo(String consumerNo);
	
	/**
	 * 查询用户所有额度类型
	 * @param searchMap
	 * @return
	 */
	List<CreditModel> listAll(Map<String, Object> searchMap);
	
	
	/**
	 * 用户信用额度查询
	 * @param searchMap
	 * @return
	 */
	List<CreditModel> creditList(Map<String, Object> searchMap);
	
	/**
	 * 根据userId修改额度，提额/逾期恢复到原额度
	 * @param map
	 * @return
	 */
	int updateByUserId(Map<String, Object> map);

	/**
	 * 减少总额度与可用额度
	 * @param change
	 * @param d 
	 * @return
	 */
	void reduceUpdate(Map<String, Double> map);
	
	/**
	 * 增加总额度和可用额度
	 * @param change
	 */
	void addUpdate(Map<String, Double> map);
	
	/**
	 * 平衡额度
	 */
	void balance();
}
