package com.rongdu.cashloan.cl.model;

/**
 * 每日收支统计
 * @author caitt
 * @version 1.0
 * @date 2017年3月21日下午4:34:57
 * Copyright 杭州融都科技股份有限公司 现金贷  All Rights Reserved
 * 官方网站：www.erongdu.com
 * 
 * 未经授权不得进行修改、复制、出售及商业使用
 */
public class IncomeAndExpendModel {

	/**
	 * 收入
	 */
	private String income;
	
	/**
	 * 支付
	 */
	private String expend;
	
	/**
	 * 日期
	 */
	private String date;

	/** 
	 * 获取收入
	 * @return income
	 */
	public String getIncome() {
		return income;
	}

	/** 
	 * 设置收入
	 * @param income
	 */
	public void setIncome(String income) {
		this.income = income;
	}

	/** 
	 * 获取支付
	 * @return expend
	 */
	public String getExpend() {
		return expend;
	}

	/** 
	 * 设置支付
	 * @param expend
	 */
	public void setExpend(String expend) {
		this.expend = expend;
	}

	/** 
	 * 获取日期
	 * @return date
	 */
	public String getDate() {
		return date;
	}

	/** 
	 * 设置日期
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
	}
	
	
}
