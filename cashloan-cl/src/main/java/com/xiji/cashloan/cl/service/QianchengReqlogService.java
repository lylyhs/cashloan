package com.xiji.cashloan.cl.service;

import java.util.Map;

import com.github.pagehelper.Page;
import com.xiji.cashloan.cl.domain.QianchengReqlog;
import com.xiji.cashloan.cl.model.QianchengReqlogModel;
import com.xiji.cashloan.core.common.service.BaseService;

/**
 * 浅橙借款申请审核Service
 *
 * @author wnb
 * @date 2018/11/27
 * @version 1.0.0
 *
 *
 * 
 * 未经授权不得进行修改、复制、出售及商业使用
 */
public interface QianchengReqlogService extends BaseService<QianchengReqlog, Long>{
	
	/**
	 * 机审请求记录分页查询后台列表显示
	 * @param params
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	Page<QianchengReqlogModel> listQianchengReqlog(Map<String, Object> params,
			int currentPage, int pageSize);
	
	/**
     * 根据订单号查找日志
     * @param orderNo
     * @return
     */
	QianchengReqlog findByOrderNo(String orderNo);
	
	/**
	 * 根据借款申请查找
	 * @param borrowId
	 * @return
	 */
	QianchengReqlog findByBorrowId(Long borrowId);
	
	/**
	 * 更新信息
	 * @param reqLog
	 * @return
	 */
	int update(QianchengReqlog reqLog);
}
