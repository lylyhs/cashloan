package com.rongdu.cashloan.cl.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rongdu.cashloan.core.common.mapper.BaseMapper;
import com.rongdu.cashloan.core.common.service.impl.BaseServiceImpl;
import com.rongdu.cashloan.cl.mapper.MagicIntimateContactMapper;
import com.rongdu.cashloan.cl.domain.MagicIntimateContact;
import com.rongdu.cashloan.cl.service.MagicIntimateContactService;


/**
 * 魔杖2.0报告-基础信息表ServiceImpl
 * 
 * @author szb
 * @version 1.0.0
 * @date 2018-12-01 10:56:05
 */
 
@Service("magicIntimateContactService")
public class MagicIntimateContactServiceImpl extends BaseServiceImpl<MagicIntimateContact, Long> implements MagicIntimateContactService {
	
    private static final Logger logger = LoggerFactory.getLogger(MagicIntimateContactServiceImpl.class);
   
    @Resource
    private MagicIntimateContactMapper magicIntimateContactMapper;

	@Override
	public BaseMapper<MagicIntimateContact, Long> getMapper() {
		return magicIntimateContactMapper;
	}
	
}