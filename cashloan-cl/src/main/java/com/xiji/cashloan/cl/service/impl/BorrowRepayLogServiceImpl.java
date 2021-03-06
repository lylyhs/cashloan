package com.xiji.cashloan.cl.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.xiji.cashloan.cl.domain.BorrowRepayLog;
import com.xiji.cashloan.cl.mapper.BorrowRepayLogMapper;
import com.xiji.cashloan.cl.model.BorrowRepayLogModel;
import com.xiji.cashloan.cl.model.ManageBRepayLogModel;
import com.xiji.cashloan.cl.service.BorrowRepayLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiji.cashloan.core.common.mapper.BaseMapper;
import com.xiji.cashloan.core.common.service.impl.BaseServiceImpl;

/**
 * 还款记录ServiceImpl
 *
 * @author wnb
 * @date 2018/11/27
 * @version 1.0.0
 *
 *
 * 
 * 未经授权不得进行修改、复制、出售及商业使用
 */
 
@Service("borrowRepayLogService")
public class BorrowRepayLogServiceImpl extends BaseServiceImpl<BorrowRepayLog, Long> implements BorrowRepayLogService {
	
	private static final Logger logger = LoggerFactory.getLogger(BorrowRepayLogServiceImpl.class);
   
    @Resource
    private BorrowRepayLogMapper borrowRepayLogMapper;


	@Override
	public BaseMapper<BorrowRepayLog, Long> getMapper() {
		return borrowRepayLogMapper;
	}

	@Override
	public int save(BorrowRepayLog brl) {
		return borrowRepayLogMapper.save(brl);
	}

	@Override
	public Page<BorrowRepayLogModel> page(Map<String, Object> searchMap,
										  int current, int pageSize) {
		PageHelper.startPage(current, pageSize);
		List<BorrowRepayLogModel> list = borrowRepayLogMapper.listSelModel(searchMap);
		return (Page<BorrowRepayLogModel>)list;
	}
	
	@Override
	public Page<ManageBRepayLogModel> listModel(Map<String, Object> params,
												int currentPage, int pageSize) {
		PageHelper.startPage(currentPage, pageSize);
		List<ManageBRepayLogModel> list = borrowRepayLogMapper.listModel(params);
		return (Page<ManageBRepayLogModel>)list;
	}
	
	@Override
	public BorrowRepayLog findSelective(Map<String, Object> paramMap) {
		BorrowRepayLog borrowRepayLog = null;
		try {
			borrowRepayLog = borrowRepayLogMapper.findSelective(paramMap);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return borrowRepayLog;
	}


	/**
	 * 根据借款id查询最近一条记录
	 * @param borrowId
	 * @return
	 */
	@Override
	public BorrowRepayLog findLastByBorrowId(long borrowId){
		BorrowRepayLog borrowRepayLog = null;
		try {
			borrowRepayLog = borrowRepayLogMapper.findLastByBorrowId(borrowId);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return borrowRepayLog;
	}


	@Override
	public boolean updateSelective(Map<String, Object> paramMap) {
		int result  =  borrowRepayLogMapper.updateSelective(paramMap);
		if(result > 0L){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean refundDeduction(Map<String, Object> paramMap){
		int result  =  borrowRepayLogMapper.refundDeduction(paramMap);
		if(result > 0L){
			return true;
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List listExport(Map<String, Object> searchParams) {
		List<ManageBRepayLogModel> list = borrowRepayLogMapper.listModel(searchParams);
		for (ManageBRepayLogModel m : list) {
			switch (m.getRepayWay()) {
			case "10":
				m.setRepayWay("代扣");
				break;
			case "20":
				m.setRepayWay("银行卡转账");
				break;
			case "30":
				m.setRepayWay("支付宝转账");
				break;
			}

			switch (m.getType()) {
				case "10":
					m.setType("还款");
					break;
				case "20":
					m.setType("展期还款");
					break;
			}
		}
		return list;
	}

}