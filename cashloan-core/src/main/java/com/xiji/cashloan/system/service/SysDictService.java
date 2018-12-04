package com.xiji.cashloan.system.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.xiji.cashloan.core.common.exception.ServiceException;
import com.xiji.cashloan.system.domain.SysDict;

/**
 * 
 * 数据字典Service
 * @author wnb
 * @date 2018/11/27
 * @version 1.0.0
 */
public interface SysDictService {
	/**
	 * 查询数据字典
	 * @param typeArr 字典组名
	 * @return 数据字典
	 */
	List<SysDict> getDictByTypeArr(String... typeArr);
	
	
	Long getDictCount(Map<String, Object> arg) throws ServiceException;

	long getDictDetailCount(Map<String, Object> data)throws ServiceException;

	boolean addOrModify(SysDict sysDict, String status) throws ServiceException;

	boolean deleteDict(Long id) throws ServiceException;

	Page<SysDict> getDictPageList(int currentPage,int pageSize,Map<String, Object> searchMap) throws ServiceException;

	Page<Map<String, Object>> getDictDetailList(Map<String, Object> data) throws ServiceException;

	List<Map<String, Object>> getDictsCache(String type) throws ServiceException;
	
	/**
	 * 根据父类ID获取详细值
	 * @param parentId
	 * @return
	 */
	List<String> getItemVlueByParentId(String id) throws ServiceException;
	
	/**
	 * 根据类型查询
	 * @param typeCode
	 * @return
	 */
	SysDict findByTypeCode(String typeCode);
}
