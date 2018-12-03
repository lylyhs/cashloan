package com.rongdu.cashloan.cl.service;

import java.util.Map;

import com.github.pagehelper.Page;
import com.rongdu.cashloan.cl.domain.UserInvite;
import com.rongdu.cashloan.cl.model.InviteBorrowModel;
import com.rongdu.cashloan.cl.model.ManageAgentModel;
import com.rongdu.cashloan.cl.model.ManageProfitModel;
import com.rongdu.cashloan.core.common.exception.ServiceException;
import com.rongdu.cashloan.core.common.service.BaseService;

/**
 * 邀请记录Service
 * 
 * @author lyang
 * @version 1.0.0
 * @date 2017-02-18 15:54:41
 * Copyright 杭州融都科技股份有限公司  arc All Rights Reserved
 * 官方网站：www.erongdu.com
 * 
 * 未经授权不得进行修改、复制、出售及商业使用
 */
public interface UserInviteService extends BaseService<UserInvite, Long>{

	/**
	 * 查询联系人电话 
	 * @param userId
	 * @return
	 */
	Map<String, Object> findPhone(long userId);

	/**
	 * 查询邀请记录
	 * @param phone
	 * @param userName 
	 * @return
	 * @throws ServiceException 
	 */
	Page<ManageProfitModel> findAgent(String phone, String userName, int current, int pageSize) throws ServiceException;

	/**
	 * 系统查询代理商
	 * @param userName
	 * @param current
	 * @param pageSize
	 * @return
	 */
	Page<ManageAgentModel> findSysAgent(String userName, int current,
			int pageSize);

	/**
	 * 查询用户邀请记录
	 * @param userId
	 * @param current
	 * @param pageSize
	 * @return
	 */
	Page<InviteBorrowModel> listInviteBorrow(long userId, int current,
			int pageSize);


}
