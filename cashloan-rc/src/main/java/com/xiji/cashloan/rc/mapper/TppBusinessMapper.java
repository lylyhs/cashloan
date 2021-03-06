package com.xiji.cashloan.rc.mapper;

import java.util.List;
import java.util.Map;

import com.xiji.cashloan.rc.domain.TppBusiness;
import com.xiji.cashloan.rc.model.ManageTppBusinessModel;
import org.apache.ibatis.annotations.Param;

import com.xiji.cashloan.core.common.mapper.BaseMapper;
import com.xiji.cashloan.core.common.mapper.RDBatisDao;
import com.xiji.cashloan.rc.model.TppBusinessModel;

/**
 * 第三方征信接口信息Dao
 *
 * @author wnb
 * @version 1.0.0
 * @date 2018/11/27
 *
 *
 *
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@RDBatisDao
public interface TppBusinessMapper extends BaseMapper<TppBusiness,Long> {

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	List<TppBusinessModel> listAll();

	/**
	 * 条件查询第三方征信接口信息
	 * 
	 * @param paramMap
	 * @return
	 */
	List<ManageTppBusinessModel> list(Map<String, Object> paramMap);

	/**
	 * 据tppId查询第三方征信接口信息
	 * 
	 * @param tppId
	 * @return
	 */
	ManageTppBusinessModel findByTppId(long tppId);
	
	/**
	 * 根据Nid查找
	 * @param nid
	 * @return
	 */
	TppBusiness findByNid(@Param("nid") String nid, @Param("tppId") Long tppId);
}
