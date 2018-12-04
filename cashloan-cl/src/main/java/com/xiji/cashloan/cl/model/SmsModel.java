package com.xiji.cashloan.cl.model;

/**
 * 短信model
 *
 * @author wnb
 * @date 2018/11/30
 * @version 1.0.0
 *
 * Copyright 杭州融都科技股份有限公司 金融创新事业部 此处填写项目英文简称  All Rights Reserved
 * 官方网站：www.xiji.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
public class SmsModel {
	
	/**
	 * 注册验证码-register
	 */
	public static final String SMS_TYPE_REGISTER = "register";
	
	/**
	 * 绑卡-bindCard
	 */
	public static final String SMS_TYPE_BINDCARD = "bindCard";
	
	/**
	 * 找回登录密码-findReg
	 */
	public static final String SMS_TYPE_FINDREG = "findReg";
	
	/**
	 * 找回交易密码-findPay
	 */
	public static final String SMS_TYPE_FINDPAY = "findPay";

}
