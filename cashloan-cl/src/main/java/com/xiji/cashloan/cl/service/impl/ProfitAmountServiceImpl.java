package com.xiji.cashloan.cl.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.xiji.cashloan.cl.domain.ProfitAmount;
import com.xiji.cashloan.cl.mapper.ProfitAmountMapper;
import com.xiji.cashloan.cl.mapper.ProfitCashLogMapper;
import com.xiji.cashloan.cl.model.ManageProfitAmountModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import tool.util.StringUtil;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiji.cashloan.cl.domain.ProfitCashLog;
import com.xiji.cashloan.cl.service.ProfitAmountService;
import com.xiji.cashloan.core.common.mapper.BaseMapper;
import com.xiji.cashloan.core.common.service.impl.BaseServiceImpl;
import com.xiji.cashloan.core.domain.User;
import com.xiji.cashloan.core.mapper.UserMapper;


/**
 * 分润资金ServiceImpl
 *
 * @author wnb
 * @date 2018/11/27
 * @version 1.0.0
 *
 *
 * 
 * 未经授权不得进行修改、复制、出售及商业使用
 */
 
@Service("profitAmountService")
public class ProfitAmountServiceImpl extends BaseServiceImpl<ProfitAmount, Long> implements ProfitAmountService {
	
    @SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ProfitAmountServiceImpl.class);
   
    @Resource
    private ProfitAmountMapper profitAmountMapper;
    @Resource
    private ProfitCashLogMapper profitCashLogMapper;
    @Resource
    private UserMapper userMapper;

	@Override
	public BaseMapper<ProfitAmount, Long> getMapper() {
		return profitAmountMapper;
	}

	@Override
	public int cash(long userId, double money) {
		int msg = 0;
		Map<String,Object> map = new HashMap<>();
		map.put("userId", userId);
		ProfitAmount pa = profitAmountMapper.findSelective(map);
		if (StringUtil.isNotBlank(pa)) {
			map.put("noCashed", pa.getNoCashed()-money);
			map.put("cashed", pa.getCashed()+money);
			map.put("id", pa.getId());
			msg = profitAmountMapper.updateCash(map);
		}
		if (msg>0) {//保存提现记录
			ProfitCashLog pcl = new ProfitCashLog();
			pcl.setUserId(userId);
			pcl.setAmount(money);
			pcl.setAddTime(new Date());
			msg = profitCashLogMapper.save(pcl);
		}
		return msg;
	}

	@Override
	public ProfitAmount find(long userId) {
		Map<String,Object> map = new HashMap<>();
		map.put("userId", userId);
		return profitAmountMapper.findSelective(map);
	}
	
	@Override
	public Page<ManageProfitAmountModel> findAmount(String phone, String userName, int current,
													int pageSize) {
		User user = userMapper.findByLoginName(phone);
		PageHelper.startPage(current, pageSize);
		Map<String,Object> map = new HashMap<>();
		map.put("id", user.getId());
		if (StringUtil.isNotBlank(userName)) {
			map.put("userName", "%"+userName+"%");
		}
		List<ManageProfitAmountModel> list = profitAmountMapper.findAmount(map);
		return (Page<ManageProfitAmountModel>)list;
	}

	@Override
	public Page<ManageProfitAmountModel> findSysAmount(String userName,
			int current, int pageSize) {
		PageHelper.startPage(current, pageSize);
		Map<String,Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(userName)) {
			map.put("userName", "%"+userName+"%");
		}
		List<ManageProfitAmountModel> list = profitAmountMapper.findSysAmount(map);
		return (Page<ManageProfitAmountModel>)list;
	}

	@Override
	public List<ProfitAmount> listNoCash() {
		return profitAmountMapper.listNoCash();
	}
	
}