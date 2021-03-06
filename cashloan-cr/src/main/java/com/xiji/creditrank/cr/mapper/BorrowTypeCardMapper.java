package com.xiji.creditrank.cr.mapper;

import com.xiji.cashloan.core.common.mapper.BaseMapper;
import com.xiji.cashloan.core.common.mapper.RDBatisDao;
import com.xiji.creditrank.cr.domain.BorrowTypeCard;

/**
 * 评分卡类型绑定表Dao
 *
 * @author wnb
 * @version 1.0.0
 * @date 2018/11/27
 * Copyright  All Rights Reserved
 *
 * 
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@RDBatisDao
public interface BorrowTypeCardMapper extends BaseMapper<BorrowTypeCard,Long> {

    

}
