package com.xiji.creditrank.cr.mapper;

import java.util.List;
import java.util.Map;

import com.xiji.cashloan.core.common.mapper.BaseMapper;
import com.xiji.cashloan.core.common.mapper.RDBatisDao;
import com.xiji.creditrank.cr.domain.Factor;
import com.xiji.creditrank.cr.model.FactorModel;

/**
 * 评分因子Dao
 *
 * @author wnb
 * @version 1.0.0
 * @date 2018/11/27
 * Copyright  All Rights Reserved
 *
 * 
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@RDBatisDao
public interface FactorMapper extends BaseMapper<Factor,Long> {

	/**
	 * 根据factorName查询
	 * @param factorName
	 * @return
	 */
    Factor findByFactorName(String factorName);

    /**
     * 列表查询数据
     * @param param
     * @return
     */
	List<FactorModel> listSelect(Map<String, Object> param);

	/**
	 * 根据ItemId查询列表
	 * @param id
	 * @return
	 */
	List<Factor> findByItemId(long id);

	/**
	 * 根据cardId查询
	 * @param cardId
	 * @return
	 */
	Factor findByCardId(long cardId);

	/**
	 * 删除因子
	 * @param id
	 * @return 
	 */
	int deleteSelective(long id);

	/**
	 * 查询所属项目因子总分
	 * @param itemId
	 * @return
	 */
	int findSumScore(long itemId);
	
	/**
	 * 查询所有
	 * @param factor
	 * @return
	 */
	List<FactorModel> listFactorModel(Map<String, Object> factor);

}
