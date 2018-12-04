package com.xiji.cashloan.core.model;

import com.xiji.cashloan.core.domain.UserBaseInfo;

/**
 * 用户基本信息model
 * @author wnb
 * @date 2018/11/27
 * @version 1.0.0
 * Copyright 杭州融都科技股份有限公司 金融创新事业部 此处填写项目英文简称  All Rights Reserved
 * 官方网站：www.xiji.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
public class UserBaseInfoModel extends UserBaseInfo {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户状态-黑名单 10
	 */
	public static final String USER_STATE_BLACK = "10";
	
	/**
	 * 用户状态-正常用户
	 */
	public static final String USER_STATE_NOBLACK = "0";
	
	/**
	 * 用户状态-白名单 20
	 */
	public static final String USER_STATE_WHITE = "20";

}
