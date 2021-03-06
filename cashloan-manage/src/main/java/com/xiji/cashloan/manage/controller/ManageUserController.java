package com.xiji.cashloan.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.xiji.cashloan.cl.domain.*;
import com.xiji.cashloan.cl.model.InviteBorrowModel;
import com.xiji.cashloan.cl.model.UserAuthModel;
import com.xiji.cashloan.cl.model.UserEducationInfoModel;
import com.xiji.cashloan.cl.service.*;
import com.xiji.cashloan.core.common.context.Constant;
import com.xiji.cashloan.core.common.context.Global;
import com.xiji.cashloan.core.common.util.JsonUtil;
import com.xiji.cashloan.core.common.util.RdPage;
import com.xiji.cashloan.core.common.util.ServletUtils;
import com.xiji.cashloan.core.domain.User;
import com.xiji.cashloan.core.domain.UserBaseInfo;
import com.xiji.cashloan.core.domain.UserOtherInfo;
import com.xiji.cashloan.core.model.CloanUserModel;
import com.xiji.cashloan.core.model.ManagerUserModel;
import com.xiji.cashloan.core.model.UserBaseInfoModel;
import com.xiji.cashloan.core.service.CloanUserService;
import com.xiji.cashloan.core.service.UserBaseInfoService;
import com.xiji.cashloan.core.service.UserOtherInfoService;
import com.xiji.cashloan.system.permission.annotation.RequiresPermission;
import com.xiji.creditrank.cr.model.CreditModel;
import com.xiji.creditrank.cr.service.CreditService;
import credit.CreditRequest;
import credit.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import tool.util.StringUtil;

import javax.annotation.Resource;
import java.util.*;

/**
 * 用户记录Controller
 *
 * @author wnb
 * @version 1.0.0
 * @date 2018/11/27
 *
 *
 * <p>
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@Controller
@Scope("prototype")
public class ManageUserController extends ManageBaseController{
	private static final Logger logger = LoggerFactory.getLogger(ManageUserController.class);
	@Resource
	private CloanUserService cloanUserService;
	@Resource
	private UserAuthService authService;
	@Resource
	private UserBaseInfoService userBaseInfoService;
	@Resource
	private BankCardService bankCardService;
	@Resource
	private UserEmerContactsService userEmerContactsService;
	@Resource
	private UserInviteService userInviteService;
	@Resource
	private UserOtherInfoService userOtherInfoService;
	@Resource
	private UserEducationInfoService userEducationService;
	@Resource
	private CreditService creditService;
	@Resource
	private ZhimaService zhimaService;
	@Resource
	private ChannelService channelService;
	@Resource
	private UserBlackInfoService userBlackInfoService;
	
	/**
	 *用户信息列表
	 * @param searchParams
	 * @param currentPage
	 * @param pageSize
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/modules/manage/cl/cluser/list.htm",method={RequestMethod.GET,RequestMethod.POST})
	@RequiresPermission(code = "modules:manage:cl:cluser:list",name = "用户信息列表")
	public void list(@RequestParam(value="searchParams",required=false) String searchParams,
			@RequestParam(value = "current") int currentPage,
			@RequestParam(value = "pageSize") int pageSize){
		Map<String, Object> params = JsonUtil.parse(searchParams, Map.class);
		Page<CloanUserModel> page = cloanUserService.listUser(params,currentPage,pageSize);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put(Constant.RESPONSE_DATA, page);
		result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
		result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
		result.put(Constant.RESPONSE_CODE_MSG, "获取成功");
		ServletUtils.writeToResponse(response,result);
	}
	
	/**
	 * 用户详细信息
	 * @param userId
	 */
	@RequestMapping(value="/modules/manage/cl/cluser/detail.htm",method={RequestMethod.GET,RequestMethod.POST})   
	@RequiresPermission(code = "modules:manage:cl:cluser:detail",name = "用户详细信息")
	public void detail(@RequestParam(value = "userId") Long userId){
		String serverHost = Global.getValue("manage_host");
		HashMap<String, Object> map = new HashMap<String,Object>();
		User user = cloanUserService.getById(userId);
		if (user != null && user.getId() != null) {
			//用户基本信息
			ManagerUserModel model = userBaseInfoService.getBaseModelByUserId(userId);
			model.setLivingImg(model.getLivingImg()!=null?serverHost +"/readFile.htm?path="+ model.getLivingImg():"");
			model.setFrontImg(model.getFrontImg()!=null?serverHost +"/readFile.htm?path="+ model.getFrontImg():"");
			model.setBackImg(model.getBackImg()!=null?serverHost +"/readFile.htm?path="+ model.getBackImg():"");
			model.setOcrImg(model.getOcrImg()!=null?serverHost +"/readFile.htm?path="+ model.getOcrImg():"");
			
			if (StringUtil.isNotBlank(model.getWorkingImg())) {
				String workImgStr = model.getWorkingImg();
				List<String> workImgList = Arrays.asList(workImgStr.split(";"));
				for (int i = 0; i < workImgList.size(); i++) {
					String workImg = workImgList.get(i);
					workImgList.set(i, serverHost +"/readFile.htm?path="+ workImg);
				}
				map.put("workImgArr", workImgList);
			}
			
			//银行卡信息
			BankCard bankCard=bankCardService.getBankCardByUserId(user.getId());
			if (null != bankCard) {
				model.setBank(bankCard.getBank());
				model.setCardNo(bankCard.getCardNo());
				model.setBankPhone(bankCard.getPhone());
			}
			
			Channel cl = channelService.getById(user.getChannelId());
			if (cl!=null) {
				model.setChannelName(cl.getName());
			}
			
			//芝麻分
			Zhima zm = zhimaService.findByUserId(userId);
			if (zm!=null&&zm.getScore()>0) {
				model.setScore(zm.getScore().toString());
			}
			map.put("userbase", model);
			
			// 构造查询条件Map
			HashMap<String, Object> paramMap = new HashMap<String,Object>();
			paramMap.put("userId",user.getId());
			
			// 认证信息
			UserAuth authModel = authService.getUserAuth(paramMap);
			map.put("userAuth", authModel);
			
			// 联系人信息
			List<UserEmerContacts> infoModel = userEmerContactsService.getUserEmerContactsByUserId(paramMap);
			map.put("userContactInfo", infoModel);
			
			// 用户其他账号信息
			UserOtherInfo otherInfo = userOtherInfoService.getInfoByUserId(user.getId());
			map.put("userOtherInfo", otherInfo);
			
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put(Constant.RESPONSE_DATA, map);
		result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
		result.put(Constant.RESPONSE_CODE_MSG, "获取成功");
		ServletUtils.writeToResponse(response,result);
	}
	
	/**
	 * 用户认证信息列表
	 * @param currentPage
	 * @param pageSize
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/modules/manage/cl/cluser/authlist.htm",method={RequestMethod.GET,RequestMethod.POST})
	@RequiresPermission(code = "modules:manage:cl:cluser:authlist",name = "用户认证信息列表")
	public void authlist(@RequestParam(value="searchParams",required=false) String searchParams,
			@RequestParam(value = "current") int currentPage,
			@RequestParam(value = "pageSize") int pageSize){
		Map<String, Object> params = JsonUtil.parse(searchParams, Map.class);
		Page<UserAuthModel> page = authService.listUserAuth(params, currentPage, pageSize);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put(Constant.RESPONSE_DATA, page);
		result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
		result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
		result.put(Constant.RESPONSE_CODE_MSG, "获取成功");
		ServletUtils.writeToResponse(response,result);
	}

	/**
	 * 用户未借信息列表
	 * @param currentPage
	 * @param pageSize
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/modules/manage/cl/cluser/notborrowagain.htm",method={RequestMethod.GET,RequestMethod.POST})
	@RequiresPermission(code = "modules:manage:cl:cluser:notborrowagain",name = "用户未借信息列表")
	public void borrowagain(@RequestParam(value="searchParams",required=false) String searchParams,
						 @RequestParam(value = "current") int currentPage,
						 @RequestParam(value = "pageSize") int pageSize){
		Map<String, Object> params = JsonUtil.parse(searchParams, Map.class);
		Page<CloanUserModel> page = cloanUserService.listUserNotBorrowagain(params, currentPage, pageSize);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put(Constant.RESPONSE_DATA, page);
		result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
		result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
		result.put(Constant.RESPONSE_CODE_MSG, "获取成功");
		ServletUtils.writeToResponse(response,result);
	}

	/**
	 * @description  查询黑名单用户列表   不用
	 * @param currentPage
	 * @param pageSize
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/modules/manage/cl/cluser/credit/list.htm")
	@RequiresPermission(code = "modules:manage:cl:cluser:credit:list",name = "查询用户列表")
	public void page(
			@RequestParam(value="searchParams",required=false) String searchParams,
			@RequestParam(value = "current") int currentPage,
			@RequestParam(value = "pageSize") int pageSize) throws Exception {
		Map<String,Object> searchMap = JsonUtil.parse(searchParams, Map.class);
		Page<CreditModel> page = creditService.page(searchMap,currentPage, pageSize);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put(Constant.RESPONSE_DATA, page);
		result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
		result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
		result.put(Constant.RESPONSE_CODE_MSG, "查询成功");
		ServletUtils.writeToResponse(response,result);
	}
	
	/**
	 * 查询邀请用户借款记录
	 * @param userId
	 * @param current
	 * @param pageSize
	 * @throws Exception
	 */
	@RequestMapping(value = "/modules/manage/invite/listInviteBorrow.htm")
	public void listInviteBorrow(
			@RequestParam(value="userId") long userId,
			@RequestParam(value = "current") int current,
			@RequestParam(value = "pageSize") int pageSize) throws Exception {
		Page<InviteBorrowModel> page = userInviteService.listInviteBorrow(userId,current,pageSize);
		Map<String, Object> data = new HashMap<>();
		data.put("list", page.getResult());
		Map<String,Object> result = new HashMap<String,Object>();
		result.put(Constant.RESPONSE_DATA, data);
		result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
		result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
		result.put(Constant.RESPONSE_CODE_MSG, "查询成功");
		ServletUtils.writeToResponse(response,result);
	}
	
	/**
	 * 添加和取消黑名单
	 * @param id
	 * @param state
	 * @throws Exception
	 */
	@RequestMapping(value = "/modules/manage/user/updateState.htm")
	public void updateState(
			@RequestParam(value="id") long id,
			@RequestParam(value="state") String state) throws Exception {
		int msg = userBaseInfoService.updateState(id,state);
		Map<String,Object> result = new HashMap<String,Object>();
		if (msg<0) {
			result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
			result.put(Constant.RESPONSE_CODE_MSG, "修改失败");
		}else {

			if(StringUtil.isNotBlank(state) && UserBaseInfoModel.USER_STATE_BLACK.equals(state)){
				// 当 state ="10" 添加黑名单
				userBlackInfoService.addNameBlack(id,state);
			}
			userBlackInfoService.deleteBlackOrWhite(id, state,"black");
			result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
			result.put(Constant.RESPONSE_CODE_MSG, "修改成功");
		}
		ServletUtils.writeToResponse(response,result);
	}
	
	/**
	 * 添加和取消白名单
	 * @param id
	 * @param state
	 * @throws Exception
	 */
	@RequestMapping(value = "/modules/manage/user/updateWhiteState.htm")
	public void updateWhiteState(
			@RequestParam(value="id") long id,
			@RequestParam(value="state") String state) throws Exception {
		Map<String,Object> result = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(state) && (UserBaseInfoModel.USER_STATE_NOBLACK.equals(state) || UserBaseInfoModel.USER_STATE_WHITE.equals(state+""))){
			int msg = userBaseInfoService.updateState(id,state);
			
			if (msg<0) {
				result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
				result.put(Constant.RESPONSE_CODE_MSG, "修改失败");
			}else {
				if (StringUtil.isNotBlank(state) && UserBaseInfoModel.USER_STATE_WHITE.equals(state)){
					// 当 state = "20" 时 添加白名单
					userBlackInfoService.addNameWhite(id,state);
				}
				userBlackInfoService.deleteBlackOrWhite(id, state,"white");
				result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
				result.put(Constant.RESPONSE_CODE_MSG, "修改成功");
			}
		}else{
			result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
			result.put(Constant.RESPONSE_CODE_MSG, "参数错误");
		}
		
		ServletUtils.writeToResponse(response,result);
	}
	
	 /**
     * 天行 学信查询
     * @throws Exception
     */
	@RequestMapping(value = "/modules/manage/user/educationRequest.htm")
    public void apiEducationRequest(
    		@RequestParam(value="name") String name,
    		@RequestParam(value="idCard") String idCard) throws Exception{
    	final String APIKEY = Global.getValue("apikey");
		final String SECRETKEY = Global.getValue("secretkey");
    	String url = Global.getValue("tx_apihost");
        final String channelNo = Global.getValue("tx_channelNo");
        final String interfaceName = Global.getValue("tx_interfaceName");
        
        long timestamp = new Date().getTime();
        Header header = new Header(APIKEY, channelNo, interfaceName, timestamp);

        CreditRequest creditRequest = new CreditRequest(url, header);

        Map<String, Object> payload = new HashMap<>();

        payload.put("name", name);
        payload.put("idCard", idCard);

        creditRequest.setPayload(payload);

        creditRequest.signByKey(SECRETKEY);

        String resultStr = creditRequest.request();

        JSONObject resultJson = JSONObject.parseObject(resultStr);
        
        
        Map<String,Object> map = new HashMap<>();
        map.put("loginName", name);
        User user = cloanUserService.findByPhone(name);
        
        UserBaseInfo ubi = new UserBaseInfo();
        if (user!=null) {
        	map = new HashMap<>();
        	map.put("userId", user.getId());
        	ubi = userBaseInfoService.findSelective(map);
		}
        
        int msg = 0;
        Map<String,Object> result = new HashMap<String,Object>();
        if (ubi!=null&&resultJson.getInteger("code")==200) {
        	JSONObject resJson = JSONObject.parseObject(StringUtil.isNull(resultJson.get("res")));
        	logger.info(StringUtil.isNull(resJson));
			UserEducationInfo ue = new UserEducationInfo();
			ue.setUserId(ubi.getUserId());
			ue.setEducationType(resJson.getString("educationType"));
			ue.setProfession(resJson.getString("profession"));
			ue.setMatriculationTime(resJson.getString("matriculationTime"));
			ue.setGraduateSchool(resJson.getString("graduateSchool"));
			ue.setGraduationTime(resJson.getString("graduationTime"));
			ue.setGraduationConclusion(resJson.getString("graduationConclusion"));
			ue.setEducationBackground(resJson.getString("educationBackground"));
			ue.setState("20");
			msg = userEducationService.save(ue);
		}
		logger.info(resultJson.getString("message"));
		if (msg>0) {
			result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
			result.put(Constant.RESPONSE_CODE_MSG, "操作成功");
		}else {
			result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
			result.put(Constant.RESPONSE_CODE_MSG, "操作失败");
			
		}
		ServletUtils.writeToResponse(response,result);
    }
	
	/**
	 * 修改学历信息
	 * @param uei
	 * @throws Exception
	 */
	@RequestMapping(value = "/modules/manage/user/updateEducation.htm")
	public void updateEducation(UserEducationInfo uei) throws Exception {
		int msg = userEducationService.update(uei);
		Map<String,Object> result = new HashMap<String,Object>();
		if (msg<0) {
			result.put(Constant.RESPONSE_CODE, Constant.FAIL_CODE_VALUE);
			result.put(Constant.RESPONSE_CODE_MSG, "修改失败");
		}else {
			result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
			result.put(Constant.RESPONSE_CODE_MSG, "修改成功");
			
		}
		ServletUtils.writeToResponse(response,result);
	}
	
	/**
	 * 查询学历列表
	 * @param search
	 * @param current
	 * @param pageSize
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/modules/manage/user/educationList.htm")
	public void educationList(
			@RequestParam(value="search",required=false) String search,
			@RequestParam(value = "current") int current,
			@RequestParam(value = "pageSize") int pageSize) throws Exception {
		Map<String,Object> searchMap = JsonUtil.parse(search, Map.class);
		Page<UserEducationInfoModel> page = userEducationService.list(searchMap,current,pageSize);
		Map<String,Object> result = new HashMap<String,Object>();
		Map<String, Object> data = new HashMap<>();
		data.put("list", page.getResult());
		result.put(Constant.RESPONSE_DATA, data);
		result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
		result.put(Constant.RESPONSE_CODE, Constant.SUCCEED_CODE_VALUE);
		result.put(Constant.RESPONSE_CODE_MSG, "查询成功");
		ServletUtils.writeToResponse(response,result);
	}
}
