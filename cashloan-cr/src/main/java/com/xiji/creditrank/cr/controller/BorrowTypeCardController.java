package com.xiji.creditrank.cr.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.xiji.creditrank.cr.domain.BorrowTypeCard;
import com.xiji.creditrank.cr.service.BorrowTypeCardService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.Page;
import com.xiji.cashloan.core.common.context.Constant;
import com.xiji.cashloan.core.common.util.JsonUtil;
import com.xiji.cashloan.core.common.util.RdPage;
import com.xiji.cashloan.core.common.util.ServletUtils;
import com.xiji.cashloan.core.common.web.controller.BaseController;

/**
 * 评分卡类型绑定表Controller
 *
 * @author wnb
 * @date 2018/11/27
 * @version 1.0.0
 * Copyright  All Rights Reserved
 *
 * 
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@Controller
public class BorrowTypeCardController extends BaseController {

	@Resource
	private BorrowTypeCardService borrowTypeCardService;

	/**
	 * 借款类型关联分页列表
	 * @param secreditrankh
	 * @param current
	 * @param pageSize
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/modules/manage/cr/borrowTypeCard/page.htm", method = RequestMethod.POST)
	public void page(
			@RequestParam(value="search",required=false) String secreditrankh,
			@RequestParam(value = "current") int current,
			@RequestParam(value = "pageSize") int pageSize) throws Exception {
		Map<String,Object> secreditrankhMap = JsonUtil.parse(secreditrankh, Map.class);
		Page<BorrowTypeCard> page = borrowTypeCardService.page(secreditrankhMap,current, pageSize);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put(Constant.RESPONSE_DATA, page);
		result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
		result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
		result.put(Constant.RESPONSE_CODE_MSG, "查询成功");
		ServletUtils.writeToResponse(response,result);
	}
	
	/**
	 * 借款类型关联新增
	 * @param borrowTypeId
	 * @param borrowTypeName
	 * @param cardId
	 * @param cardName
	 * @throws Exception
	 */
	@RequestMapping(value = "/modules/manage/cr/borrowTypeCard/save.htm", method = RequestMethod.POST)
	public void save(
			@RequestParam(value = "borrowTypeId") long borrowTypeId,
			@RequestParam(value = "borrowTypeName") String borrowTypeName,
			@RequestParam(value = "cardId") long cardId,
			@RequestParam(value = "cardName") String cardName
			) throws Exception {
		int msg = borrowTypeCardService.save(borrowTypeId,borrowTypeName,cardId,cardName);
		Map<String,Object> result = new HashMap<String,Object>();
		if (msg>0) {
			result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
			result.put(Constant.RESPONSE_CODE_MSG, "成功");
		}else {
			result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
			result.put(Constant.RESPONSE_CODE_MSG, "成功");
		}
		ServletUtils.writeToResponse(response,result);
	}
	
	/**
	 * 借款类型关联修改
	 * @param id
	 * @param borrowTypeId
	 * @param borrowTypeName
	 * @param cardId
	 * @param cardName
	 * @throws Exception
	 */
	@RequestMapping(value = "/modules/manage/cr/borrowTypeCard/update.htm", method = RequestMethod.POST)
	public void update(
			@RequestParam(value = "id") long id,
			@RequestParam(value = "borrowTypeId") long borrowTypeId,
			@RequestParam(value = "borrowTypeName") String borrowTypeName,
			@RequestParam(value = "cardId") long cardId,
			@RequestParam(value = "cardName") String cardName
			) throws Exception {
		int msg = borrowTypeCardService.update(id,borrowTypeId,borrowTypeName,cardId,cardName);
		Map<String,Object> result = new HashMap<String,Object>();
		if (msg>0) {
			result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
			result.put(Constant.RESPONSE_CODE_MSG, "成功");
		}else {
			result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
			result.put(Constant.RESPONSE_CODE_MSG, "成功");
		}
		ServletUtils.writeToResponse(response,result);
	}
}
