package com.xiji.cashloan.core.service;

import com.github.pagehelper.Page;
import com.xiji.cashloan.core.common.service.BaseService;
import com.xiji.cashloan.core.domain.User;
import com.xiji.cashloan.core.model.CloanUserModel;

import java.util.List;
import java.util.Map;

/**
 * 用户Service
 *
 * @author wnb
 * @date 2018/11/27
 * @version 1.0.0
 *
 *
 * 
 * 未经授权不得进行修改、复制、出售及商业使用
 */
public interface CloanUserService extends BaseService<User, Long> {
	/**
	 * 查询用户详细信息列表
	 * @param params
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	Page<CloanUserModel> listUser(Map<String, Object> params, int currentPage,
			int pageSize);

	/**
	 * 查询未借用户信息列表
	 * @param params
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	Page<CloanUserModel> listUserNotBorrowagain(Map<String, Object> params, int currentPage,
								  int pageSize);


	/**
	 * 查询未借用户信息列表
	 * @param params
	 * @return
	 */
	List<CloanUserModel> listNotBorrowAgain(Map<String, Object> params);

	/**
	 * 查询用户详细信息
	 * @param id
	 * @return
	 */
	CloanUserModel getModelById(Long id);
	
	/**
	 * 查询所有相关的数据字典
	 * @return
	 */
	List<Map<String, Object>> findAllDic();


	/**
	 * 据uuid修改用户信息
	 * 
	 * @param paramMap
	 * @return
	 */
	boolean updateByUuid(Map<String, Object> paramMap);
	
	/**
	 * 据用户手机号查询用户
	 * @param phone
	 * @return
	 */
	User findByPhone(String phone);
	
	/**
	 * 今日注册用户数
	 * @return
	 */
	long todayCount();

	/**
	 * 修改登陆时间
	 * @param loginName
	 */
	void modify(String loginName);
}
