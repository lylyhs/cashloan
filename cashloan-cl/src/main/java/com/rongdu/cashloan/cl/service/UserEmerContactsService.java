package com.rongdu.cashloan.cl.service;

import java.util.List;
import java.util.Map;

import com.rongdu.cashloan.cl.domain.UserEmerContacts;
import com.rongdu.cashloan.core.common.service.BaseService;


/**
 * 紧急联系人表Service
 * 
 * @author lyang
 * @version 1.0.0
 * @date 2017-02-14 11:24:05
 * Copyright 杭州融都科技股份有限公司  arc All Rights Reserved
 * 官方网站：www.erongdu.com
 * 
 * 未经授权不得进行修改、复制、出售及商业使用
 */
public interface UserEmerContactsService extends BaseService<UserEmerContacts, Long>{

	public List<UserEmerContacts> getUserEmerContactsByUserId(Map<String,Object> paramMap);
}
