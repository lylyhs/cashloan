package com.xiji.cashloan.system.domain;

import java.io.Serializable;

/**
 * 
 * 系统参数实体类
 * @author wnb
 * @date 2018/11/27
 * @version 1.0.0
 */
public class SysConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	/** 参数编号 唯一 **/
	private String code;

	/** 名称 **/
	private String name;

	/** 值 **/
	private String value;

	/** 类型，1：基础参数，...... **/
	private Integer type;

	/** 状态，0：禁用，1：启用 **/
	private Integer status;
	
	/**创建者*/
	private Long creator;
	
	/**备注*/
	private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}
