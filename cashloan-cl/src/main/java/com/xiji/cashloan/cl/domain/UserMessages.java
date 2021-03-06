package com.xiji.cashloan.cl.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户短信
 * 
 * @author wnb
 * @version 1.0.0
 * @date 2018/11/23
 *
 *
 * 
 * 未经授权不得进行修改、复制、出售及商业使用
 */
 public class UserMessages implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键Id
    */
    private Long id;
    
	/**
     * 用户标识
     */
	private Long userId;

    /**
    * 短信收发人
    */
    private String name;

    /**
    * 手机号码
    */
    private String phone;

    /**
    * 收发时间
    */
    private Date time;

    /**
    * 收发标识，10发20收
    */
    private String type;

	/**
	 * 短信内容
	 */
	private String content;

	/**
	 * 获取主键Id
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置主键Id
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取用户标识
	 * @return userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * 设置用户标识
	 * @param userId
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * 获取短信收发人
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置短信收发人
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取手机号码
	 * @return phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置手机号码
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取收发时间
	 * @return time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * 设置收发时间
	 * @param time
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * 获取收发标识，10发20收
	 * @return type
	 */
	public String getType() {
		return type;
	}


	/**
	 * 设置收发标识，10发20收
	 * @param type
	 */

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取短信内容
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置短信
	 * @param content
	 */
	public void setContent(String content){
		this.content = content;
	}

}
