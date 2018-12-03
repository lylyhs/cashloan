package com.rongdu.creditrank.cr.model;


import java.util.Date;
import java.util.List;

/**
 * 评分结果
 * @author ctt
 * @version 1.0.0
 * @date 2017年1月7日 下午2:01:47
 * Copyright 杭州融都科技股份有限公司 creditrank  All Rights Reserved
 * 官方网站：www.erongdu.com
 * 
 * 未经授权不得进行修改、复制、出售及商业使用
 */
public class CrResultDetailTree {

	/**
     * 用户ID
     */
     private Long resultId;

    /**
    * 评分卡ID
    */
    private Long cardId;

    /**
    * 评分项目ID
    */
    private Long itemId;

    /**
    * 评分因子ID
    */
    private Long factorId;
    
    /**
     * 因子参数ID
     */
    private Long paramId;

    /**
    * 参数名称
    */
    private String paramName;

    /**
    * 最高分值
    */
    private Integer paramScore;

    /**
    * 表达式
    */
    private String formula;

    /**
    * 参数取值范围
    */
    private String cvalue;

    /**
    * 参与评分的因子实际值
    */
    private String value;

    /**
    * 评分分数级别10 评分卡 20 项目 30 因子
    */
    private String level;

    /**
    * 得分
    */
    private Integer score;

    /**
    * 添加时间
    */
    private Date addTime;
	

	public List<CrResultDetailModel> cardList;


	/** 
	 * 获取用户ID
	 * @return resultId
	 */
	public Long getResultId() {
		return resultId;
	}


	/** 
	 * 设置用户ID
	 * @param resultId
	 */
	public void setResultId(Long resultId) {
		this.resultId = resultId;
	}


	/** 
	 * 获取评分卡ID
	 * @return cardId
	 */
	public Long getCardId() {
		return cardId;
	}


	/** 
	 * 设置评分卡ID
	 * @param cardId
	 */
	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}


	/** 
	 * 获取评分项目ID
	 * @return itemId
	 */
	public Long getItemId() {
		return itemId;
	}


	/** 
	 * 设置评分项目ID
	 * @param itemId
	 */
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}


	/** 
	 * 获取评分因子ID
	 * @return factorId
	 */
	public Long getFactorId() {
		return factorId;
	}


	/** 
	 * 设置评分因子ID
	 * @param factorId
	 */
	public void setFactorId(Long factorId) {
		this.factorId = factorId;
	}


	/** 
	 * 获取因子参数ID
	 * @return paramId
	 */
	public Long getParamId() {
		return paramId;
	}


	/** 
	 * 设置因子参数ID
	 * @param paramId
	 */
	public void setParamId(Long paramId) {
		this.paramId = paramId;
	}


	/** 
	 * 获取参数名称
	 * @return paramName
	 */
	public String getParamName() {
		return paramName;
	}


	/** 
	 * 设置参数名称
	 * @param paramName
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}


	/** 
	 * 获取最高分值
	 * @return paramScore
	 */
	public Integer getParamScore() {
		return paramScore;
	}


	/** 
	 * 设置最高分值
	 * @param paramScore
	 */
	public void setParamScore(Integer paramScore) {
		this.paramScore = paramScore;
	}


	/** 
	 * 获取表达式
	 * @return formula
	 */
	public String getFormula() {
		return formula;
	}


	/** 
	 * 设置表达式
	 * @param formula
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}


	/** 
	 * 获取参数取值范围
	 * @return cvalue
	 */
	public String getCvalue() {
		return cvalue;
	}


	/** 
	 * 设置参数取值范围
	 * @param cvalue
	 */
	public void setCvalue(String cvalue) {
		this.cvalue = cvalue;
	}


	/** 
	 * 获取参与评分的因子实际值
	 * @return value
	 */
	public String getValue() {
		return value;
	}


	/** 
	 * 设置参与评分的因子实际值
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}


	/** 
	 * 获取评分分数级别10评分卡20项目30因子
	 * @return level
	 */
	public String getLevel() {
		return level;
	}


	/** 
	 * 设置评分分数级别10评分卡20项目30因子
	 * @param level
	 */
	public void setLevel(String level) {
		this.level = level;
	}


	/** 
	 * 获取得分
	 * @return score
	 */
	public Integer getScore() {
		return score;
	}


	/** 
	 * 设置得分
	 * @param score
	 */
	public void setScore(Integer score) {
		this.score = score;
	}


	/** 
	 * 获取添加时间
	 * @return addTime
	 */
	public Date getAddTime() {
		return addTime;
	}


	/** 
	 * 设置添加时间
	 * @param addTime
	 */
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}


	/** 
	 * 获取cardList
	 * @return cardList
	 */
	public List<CrResultDetailModel> getCardList() {
		return cardList;
	}


	/** 
	 * 设置cardList
	 * @param cardList
	 */
	public void setCardList(List<CrResultDetailModel> cardList) {
		this.cardList = cardList;
	}

	
	
}
