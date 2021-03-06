package com.xiji.cashloan.api.user.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.xiji.cashloan.cl.domain.HelipayUser;
import com.xiji.cashloan.cl.model.pay.helipay.HelipayHelper;
import com.xiji.cashloan.cl.model.pay.helipay.constant.HelipayConstant;
import com.xiji.cashloan.cl.model.pay.helipay.util.HelipayUtil;
import com.xiji.cashloan.cl.model.pay.helipay.vo.delegation.MerchantUserQueryResVo;
import com.xiji.cashloan.cl.model.pay.helipay.vo.delegation.MerchantUserQueryVo;
import com.xiji.cashloan.cl.service.*;
import com.xiji.cashloan.core.common.exception.SimpleMessageException;
import com.xiji.cashloan.core.domain.User;
import com.xiji.cashloan.core.domain.UserBaseInfo;
import com.xiji.cashloan.core.mapper.UserMapper;
import com.xiji.cashloan.core.service.UserBaseInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tool.util.BeanUtil;
import tool.util.DateUtil;

import com.xiji.cashloan.api.user.bean.AppDbSession;
import com.xiji.cashloan.api.user.bean.AppSessionBean;
import com.xiji.cashloan.cl.domain.BankCard;
import com.xiji.cashloan.cl.domain.Channel;
import com.xiji.cashloan.cl.domain.OperatorReqLog;
import com.xiji.cashloan.cl.model.SmsModel;
import com.xiji.cashloan.core.common.context.Global;
import com.xiji.cashloan.core.common.util.MapUtil;
import com.xiji.cashloan.core.common.util.OrderNoUtil;
import com.xiji.cashloan.core.common.util.SqlUtil;
import com.xiji.cashloan.core.common.util.StringUtil;
import com.xiji.cashloan.core.service.CloanUserService;

/**
 * @author wnb
 * @date 2018/12/03
 * @version 1.0.0
 */
@Service("clUserService_")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UserService {
	
    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private AppDbSession appDbSession;

    @Resource
    protected DBService dbService;

    @Resource
    protected MybatisService mybatisService;

    @Resource
    protected SmsService smsService;
    
    @Resource
	private UserEquipmentInfoService userEquipmentInfoService;
    
	@Resource
	private UserAuthService userAuthService;

	@Resource
	private BankCardService bankCardService;
	
	@Resource
	private OperatorReqLogService operatorReqLogService;
	
	@Resource
	private CloanUserService cloanUserService;

    @Resource
    private ClSmsService clSmsService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserBaseInfoService userBaseInfoService;
    @Resource
    private HelipayUserService helipayUserService;

    public UserService() {
        super();
    }

	@Transactional
    public Map userRegister(HttpServletRequest request, String phone, String vcode, String invitationCode,
                            String registerCoordinate,String registerAddr,String regClient, String signMsg, String channelCode) {
        try {
            if (StringUtil.isEmpty(phone) || !StringUtil.isPhone(phone) || StringUtil.isEmpty(vcode)) {
                Map ret = new LinkedHashMap();
                ret.put("success", false);
                ret.put("msg", "参数有误");
                return ret;
            }
            
            CloanUserService cloanUserService = (CloanUserService) BeanUtil.getBean("cloanUserService");
            long todayCount = cloanUserService.todayCount();
            String dayRegisterMax_ = Global.getValue("day_register_max");
            if(StringUtil.isNotBlank(dayRegisterMax_)){
            	int dayRegisterMax = Integer.parseInt(dayRegisterMax_);
            	if(dayRegisterMax > 0 && todayCount >= dayRegisterMax){
            		 Map ret = new LinkedHashMap();
                     ret.put("success", false);
                     ret.put("msg", "今日注册用户数已达上限，请明日再来");
                     return ret;
            	}
            }
            
            ClSmsService clSmsService = (ClSmsService)BeanUtil.getBean("clSmsService");
            int results = clSmsService.verifySms(phone, SmsModel.SMS_TYPE_REGISTER, vcode);
            String vmsg;
            if (results == 1) {
            	vmsg = null;
    		}else if(results == -1){
    			vmsg="验证码已过期";
    		}else {
    			vmsg="手机号码或验证码错误";
    		}
            if (vmsg != null) {
                Map ret = new LinkedHashMap();
                ret.put("success", false);
                ret.put("msg", vmsg);
                return ret;
            }
            Map invitor = null;
            if (!StringUtil.isEmpty(invitationCode)) {
                invitor = mybatisService.queryRec("usr.queryUserByInvitation", invitationCode);
                if (invitor == null) {
                    Map ret = new LinkedHashMap();
                    ret.put("success", false);
                    ret.put("msg", "邀请人不存在");
                    return ret;
                }
            }
//
            Map old = mybatisService.queryRec("usr.queryUserByLoginName", phone);
            if (old != null) {
                Map ret = new LinkedHashMap();
                ret.put("success", false);
                ret.put("msg", "该手机号码已被注册");
                return ret;
            }
            
            // 渠道
            long channelId = 0;
            if(StringUtil.isNotBlank(channelCode)){
            	ChannelService channelService = (ChannelService) BeanUtil.getBean("channelService");
            	Channel channel = channelService.findByCode(channelCode);
            	 if (channel != null) {
            		 channelId=channel.getId();
                 } else {
                     Map ret = new LinkedHashMap();
                     ret.put("success", false);
                     ret.put("msg", "您的资质未达到我们平台的标准，请关注日用钱包获取更多贷款资讯");
                     return ret;
                 }
            } else {
                Map ret = new LinkedHashMap();
                ret.put("success", false);
                ret.put("msg", "您的资质未达到我们平台的标准，请关注日用钱包获取更多贷款资讯");
                return ret;
            }
            
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            long userId = dbService.insert(SqlUtil.buildInsertSqlMap("cl_user", new Object[][]{
                {"login_name", phone},
                {"invitation_code", randomInvitationCode(6)},
                {"regist_time", new Date()},
                {"uuid", uuid},
                {"level", 3},
                {"register_client", regClient},
                {"channel_id", channelId}
            }));

            dbService.insert(SqlUtil.buildInsertSqlMap("cl_user_base_info", new Object[][]{
                {"user_id", userId},
                {"phone", phone},
                {"register_coordinate", registerCoordinate},
                {"register_addr", registerAddr}
            }));

            dbService.insert(SqlUtil.buildInsertSqlMap("arc_credit", new Object[][]{
                {"consumer_no", userId},
                {"total", Global.getValue("init_credit")},
                {"unuse", Global.getValue("init_credit")},
                {"state", 10}
            }));

            dbService.insert(SqlUtil.buildInsertSqlMap("cl_profit_amount", new Object[][]{
                {"user_id", userId},
                {"state", "10"}
            }));

            dbService.insert(SqlUtil.buildInsertSqlMap("cl_user_auth", new Object[][]{
                {"user_id", userId},
                {"id_state", 10},
                {"zhima_state", 10},
                {"phone_state", 10},
                {"contact_state", 10},
                {"bank_card_state", 10},
                {"work_info_state", 10},
                {"other_info_state", 10},
            }));

            if (invitor != null) {
                dbService.insert(SqlUtil.buildInsertSqlMap("cl_user_invite", new Object[][]{
                    {"invite_time", new Date()},
                    {"invite_id", userId},
                    {"invite_name", phone},
                    {"user_id", invitor.get("id")},
                    {"user_name", invitor.get("login_name")},
                }));
            }
            
            //2017.5.6 仅用于demo演示环境
            demoUser(userId);
            
            Map result = new LinkedHashMap();
            result.put("success", true);
            result.put("msg", "注册成功");
            return result;
        } catch (Exception e) {
            logger.error("注册失败", e);
            Map ret = new LinkedHashMap();
            ret.put("success", false);
            ret.put("msg", "注册失败");
            return ret;
        }
    }

    @Transactional
    public Map apiRegister(final String cid,
                           final int request_time,
                           final String phone,final String sign) {
        try {
            Map ret = new LinkedHashMap();

            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update((cid + request_time + phone).getBytes());
            String cipher = new BigInteger(1, md5.digest()).toString(16);
            if (StringUtil.isEmpty(sign) || !sign.equals(cipher) ) {
                ret.put("error", true);
                ret.put("info", "签名错误!");
                ret.put("code", 11);
                ret.put("data", new JSONObject());
                return ret;
            }

            // 渠道
            long channelId ;
            Channel channel;
            if(StringUtil.isNotBlank(cid)){
                ChannelService channelService = (ChannelService) BeanUtil.getBean("channelService");
                channel = channelService.findByCode(cid);
                if (channel != null && channel.getName().trim().contains("效率花呗")) {
                    channelId=channel.getId();
                } else {
                    ret.put("error", true);
                    ret.put("info", "渠道标识错误");
                    ret.put("code", 10);
                    ret.put("data", new JSONObject());
                    return ret;
                }

                if(!channel.getState().equals("10")) {
                    ret.put("error", true);
                    ret.put("info", "渠道未启用");
                    ret.put("code", 10);
                    ret.put("data", new JSONObject());
                    return ret;
                }

            } else {
                ret.put("error", true);
                ret.put("info", "渠道标识错误");
                ret.put("code", 10);
                ret.put("data", new JSONObject());
                return ret;
            }

            if (StringUtil.isEmpty(phone) || !StringUtil.isPhone(phone) ) {
                ret.put("error", true);
                ret.put("info", "手机号格式有误!");
                ret.put("code", 200);
                ret.put("data", new JSONObject());
                return ret;
            }

            CloanUserService cloanUserService = (CloanUserService) BeanUtil.getBean("cloanUserService");
            long todayCount = cloanUserService.todayCount();
            String dayRegisterMax_ = Global.getValue("day_register_max");
            if(StringUtil.isNotBlank(dayRegisterMax_)){
                int dayRegisterMax = Integer.parseInt(dayRegisterMax_);
                if(dayRegisterMax > 0 && todayCount >= dayRegisterMax){
                    ret.put("error", true);
                    ret.put("info", "今日注册用户数已达上限，请明日再来");
                    ret.put("code", 202);
                    ret.put("data", new JSONObject());
                    return ret;
                }
            }

            Map old = mybatisService.queryRec("usr.queryUserByLoginName", phone);
            if (old != null) {
                ret.put("error", true);
                ret.put("info", "用户已存在");
                ret.put("code", 201);
                ret.put("data", new JSONObject());
                return ret;
            }

            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String pwd_ming = uuid.substring(0, 8);
            md5.update(pwd_ming.getBytes());
            String pwd_mi = new BigInteger(1, md5.digest()).toString(16).toUpperCase();
            String regClient = "api";
            long userId = dbService.insert(SqlUtil.buildInsertSqlMap("cl_user", new Object[][]{
                    {"login_name", phone},
                    {"login_pwd", pwd_mi},
                    {"invitation_code", randomInvitationCode(6)},
                    {"regist_time", new Date()},
                    {"uuid", uuid},
                    {"level", 3},
                    {"register_client", regClient},
                    {"channel_id", channelId}
            }));

            dbService.insert(SqlUtil.buildInsertSqlMap("cl_user_base_info", new Object[][]{
                    {"user_id", userId},
                    {"phone", phone},
                    {"register_coordinate", null},
                    {"register_addr", null}
            }));

            dbService.insert(SqlUtil.buildInsertSqlMap("arc_credit", new Object[][]{
                    {"consumer_no", userId},
                    {"total", channel.getInitCredit()},
                    {"unuse", channel.getInitCredit()},
                    {"state", 10}
            }));

            dbService.insert(SqlUtil.buildInsertSqlMap("cl_profit_amount", new Object[][]{
                    {"user_id", userId},
                    {"state", "10"}
            }));

            dbService.insert(SqlUtil.buildInsertSqlMap("cl_user_auth", new Object[][]{
                    {"user_id", userId},
                    {"id_state", 10},
                    {"zhima_state", 10},
                    {"phone_state", 10},
                    {"contact_state", 10},
                    {"bank_card_state", 10},
                    {"work_info_state", 10},
                    {"other_info_state", 10},
            }));

            ret.put("error", false);
            ret.put("info", "注册成功");
            ret.put("code", 0);
            ret.put("data", new JSONObject());

            String type= SmsModel.SMS_TYPE_INITREG;
            logger.info("发送短信，phone：" + phone + "， type：" + type + "，准备发送");
            String orderNo = clSmsService.sendSms(phone, type, pwd_ming);
            if (StringUtil.isNotBlank(orderNo)) {
                clSmsService.getReportByOrderNo(orderNo, phone, type);
            }
            return ret;

        } catch (Exception e) {
            logger.error("注册失败", e);
            Map ret = new LinkedHashMap();
            ret.put("error", true);
            ret.put("info", "系统异常，请稍后再试!");
            ret.put("code", 400);
            ret.put("data", null);
            return ret;
        }
    }

    @Transactional
    public Map registerUser(HttpServletRequest request, String phone, String pwd, String vcode, String invitationCode,
                            String registerCoordinate,String registerAddr,String regClient, String signMsg, String channelCode) {
        try {
            if (StringUtil.isEmpty(phone) || !StringUtil.isPhone(phone) || StringUtil.isEmpty(vcode) ) {
                Map ret = new LinkedHashMap();
                ret.put("success", false);
                ret.put("msg", "参数有误");
                return ret;
            }

            CloanUserService cloanUserService = (CloanUserService) BeanUtil.getBean("cloanUserService");
            long todayCount = cloanUserService.todayCount();
            String dayRegisterMax_ = Global.getValue("day_register_max");
            if(StringUtil.isNotBlank(dayRegisterMax_)){
                int dayRegisterMax = Integer.parseInt(dayRegisterMax_);
                if(dayRegisterMax > 0 && todayCount >= dayRegisterMax){
                    Map ret = new LinkedHashMap();
                    ret.put("success", false);
                    ret.put("msg", "今日注册用户数已达上限，请明日再来");
                    return ret;
                }
            }

            ClSmsService clSmsService = (ClSmsService)BeanUtil.getBean("clSmsService");
            int results = clSmsService.verifySms(phone, SmsModel.SMS_TYPE_REGISTER, vcode);
            String vmsg;
            if (results == 1) {
                vmsg = null;
            }else if(results == -1){
                vmsg="验证码已过期";
            }else {
                vmsg="手机号码或验证码错误";
            }
            if (vmsg != null) {
                Map ret = new LinkedHashMap();
                ret.put("success", false);
                ret.put("msg", vmsg);
                return ret;
            }
            Map invitor = null;
            if (!StringUtil.isEmpty(invitationCode)) {
                invitor = mybatisService.queryRec("usr.queryUserByInvitation", invitationCode);
                if (invitor == null) {
                    Map ret = new LinkedHashMap();
                    ret.put("success", false);
                    ret.put("msg", "邀请人不存在");
                    return ret;
                }
            }
//
            Map old = mybatisService.queryRec("usr.queryUserByLoginName", phone);
            if (old != null) {
                Map ret = new LinkedHashMap();
                ret.put("success", false);
                ret.put("msg", "该手机号码已被注册");
                return ret;
            }

            // 渠道
            long channelId = 0;
            Channel channel=null;
            if(StringUtil.isNotBlank(channelCode)){
                ChannelService channelService = (ChannelService) BeanUtil.getBean("channelService");
                channel = channelService.findByCode(channelCode);
                if (channel != null) {
                    channelId=channel.getId();
                } else {
                    Map ret = new LinkedHashMap();
                    ret.put("success", false);
                    ret.put("msg", "您的资质未达到我们平台的标准，请关注日用钱包获取更多贷款资讯");
                    return ret;
                }
            } else {
                Map ret = new LinkedHashMap();
                ret.put("success", false);
                ret.put("msg", "您的资质未达到我们平台的标准，请关注日用钱包获取更多贷款资讯");
                return ret;
            }
            String pwd_ming = null;
            if(StringUtils.isBlank(pwd)) {
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                pwd_ming = uuid.substring(0, 8);
                md5.update(pwd_ming.getBytes());
                pwd = new BigInteger(1, md5.digest()).toString(16).toUpperCase();
            }
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            long userId = dbService.insert(SqlUtil.buildInsertSqlMap("cl_user", new Object[][]{
                    {"login_name", phone},
                    {"login_pwd", pwd},
                    {"invitation_code", randomInvitationCode(6)},
                    {"regist_time", new Date()},
                    {"uuid", uuid},
                    {"level", 3},
                    {"register_client", regClient},
                    {"channel_id", channelId}
            }));

            dbService.insert(SqlUtil.buildInsertSqlMap("cl_user_base_info", new Object[][]{
                    {"user_id", userId},
                    {"phone", phone},
                    {"register_coordinate", registerCoordinate},
                    {"register_addr", registerAddr}
            }));

            dbService.insert(SqlUtil.buildInsertSqlMap("arc_credit", new Object[][]{
                    {"consumer_no", userId},
                    {"total", channel.getInitCredit()},
                    {"unuse", channel.getInitCredit()},
                    {"state", 10}
            }));

            dbService.insert(SqlUtil.buildInsertSqlMap("cl_profit_amount", new Object[][]{
                    {"user_id", userId},
                    {"state", "10"}
            }));

            dbService.insert(SqlUtil.buildInsertSqlMap("cl_user_auth", new Object[][]{
                    {"user_id", userId},
                    {"id_state", 10},
                    {"zhima_state", 10},
                    {"phone_state", 10},
                    {"contact_state", 10},
                    {"bank_card_state", 10},
                    {"work_info_state", 10},
                    {"other_info_state", 10},
            }));

            if (invitor != null) {
                dbService.insert(SqlUtil.buildInsertSqlMap("cl_user_invite", new Object[][]{
                        {"invite_time", new Date()},
                        {"invite_id", userId},
                        {"invite_name", phone},
                        {"user_id", invitor.get("id")},
                        {"user_name", invitor.get("login_name")},
                }));
            }

            if(StringUtils.isNotBlank(pwd_ming)) {
                String type= SmsModel.SMS_TYPE_INITREG;
                logger.info("发送短信，phone：" + phone + "， type：" + type + "，准备发送");
                String orderNo = clSmsService.sendSms(phone, type, pwd_ming);
                if (StringUtil.isNotBlank(orderNo)) {
                    clSmsService.getReportByOrderNo(orderNo, phone, type);
                }
            }

            Map result = new LinkedHashMap();
            result.put("success", true);
            result.put("msg", "注册成功");
            return result;
        } catch (Exception e) {
            logger.error("注册失败", e);
            Map ret = new LinkedHashMap();
            ret.put("success", false);
            ret.put("msg", "注册失败");
            return ret;
        }
    }

	/**
	 * 给注册用户添加芝麻和银行卡演示数据
	 * @param userId
	 */
	private void demoUser(long userId){
        if(StringUtil.isNotBlank(Global.getValue("app_register_demo"))&&Global.getValue("app_register_demo").equals("1")){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("bankCardState", "30");
			map.put("zhimaState", "30");
			map.put("phoneState", "30");
			map.put("userId", userId);
			userAuthService.updateByUserId(map);
			BankCard card = new BankCard();
			card.setCardNo("6212261202001978888");
			card.setBindTime(DateUtil.getNow());
			card.setUserId(userId);
			card.setBank("中国农业银行");
			card.setAgreeNo(OrderNoUtil.getSerialNumber());
		    bankCardService.save(card);
		    
		    OperatorReqLog  record=new OperatorReqLog();
		    record.setUserId(userId);
		    record.setCreateTime(new Date());
		    record.setTaskState("1");
		    record.setTaskId(OrderNoUtil.getSerialNumber());
//		    record.setRespOrderNo(OrderNoUtil.getSerialNumber());
		    record.setRespTime(new Date());
//		    record.setRespParams("");
		    operatorReqLogService.insert(record);
		    
		}
        
	}
	
	
    private String randomInvitationCode(int len) {
        while (true) {
            String str = randomNumAlph(len);
            if (mybatisService.queryRec("usr.queryUserByInvitation", str) == null) {
                return str;
            }
        }
    }

    private static String randomNumAlph(int len) {
        Random random = new Random();

        StringBuilder sb = new StringBuilder();
        byte[][] list = {
            {48, 57},
            {97, 122},
            {65, 90}
        };
        for (int i = 0; i < len; i++) {
            byte[] o = list[random.nextInt(list.length)];
            byte value = (byte) (random.nextInt(o[1] - o[0] + 1) + o[0]);
            sb.append((char) value);
        }
        return sb.toString();
    }


    //设置密码
    public Object setPwd(String phone, String Pwd,String signMsg) {

        if (dbService.update(SqlUtil.buildUpdateSql("cl_user", MapUtil.array2Map(new Object[][]{
                {"login_name", phone},
                {"login_pwd", Pwd},
                {"loginpwd_modify_time", new Date()}
        }), "login_name")) == 1) {
            Map ret = new LinkedHashMap();
            ret.put("success", true);
            ret.put("msg", "设置密码成功");
            return ret;
        } else {
            Map ret = new LinkedHashMap();
            ret.put("success", false);
            ret.put("msg", "设置密码失败");
            return ret;
        }

    }

    public Object forgetPwd(String phone, String newPwd, String vcode,String signMsg) {
             
            if (!StringUtil.isEmpty(vcode)) {
                String msg = smsService.validateSmsCode(phone, SmsModel.SMS_TYPE_FINDREG , vcode);
                if (msg != null) {
                    Map ret = new LinkedHashMap();
                    ret.put("success", false);
                    ret.put("msg", msg);
                    return ret;
                }
            }


            if (dbService.update(SqlUtil.buildUpdateSql("cl_user", MapUtil.array2Map(new Object[][]{
                {"login_name", phone},
                {"login_pwd", newPwd},
                {"loginpwd_modify_time", new Date()}
            }), "login_name")) == 1) {
                Map ret = new LinkedHashMap();
                ret.put("success", true);
                ret.put("msg", "密码已修改");
                return ret;
            } else {
                Map ret = new LinkedHashMap();
                ret.put("success", false);
                ret.put("msg", "密码修改失败");
                return ret;
            }

    }

    public Map login(final HttpServletRequest request, final String loginName, final String loginPwd,String signMsg, String blackBox) {
        try {
        	/*String _signMsg = MD5.encode(Global.getValue("app_key") + loginName + loginPwd);
            if (!_signMsg.equalsIgnoreCase(signMsg)) {
                Map ret = new LinkedHashMap();
                ret.put("success", false);
                ret.put("msg", "签名验签不通过");
                return ret;
            }*/
        	
            Map user = mybatisService.queryRec("usr.queryUserByLoginName", loginName);
            if (user == null) {
                Map ret = new LinkedHashMap();
                ret.put("success", false);
                ret.put("msg", "账户不存在");
                return ret;
            }

            String dbPwd = (String) user.get("login_pwd");
            if (dbPwd.equalsIgnoreCase(loginPwd)) {
                AppSessionBean session = appDbSession.create(request, loginName);
                cloanUserService.modify(loginName);//修改登陆时间
                userEquipmentInfoService.save(loginName,blackBox);

                String payModelSelect = Global.getValue("pay_model_select");
                // 如果是合利宝支付 需走下面 合利宝用户信息验证逻辑
                if ("helipay".equals(payModelSelect)){
                    Map<String,Object> map = new HashMap<>();
                    map.put("loginName", loginName);
                    User user1 = userMapper.findSelective(map);
                    UserBaseInfo userBaseInfo = userBaseInfoService.findByUserId(user1.getId());
                    if (userBaseInfo != null && StringUtil.isNotBlank(userBaseInfo.getIdNo())){
                        Map<String,Object> helipayMap = new HashMap<>();
                        helipayMap.put("userId",user1.getId());
                        HelipayUser helipayUser = helipayUserService.getHelipayUser(helipayMap);
                        if (helipayUser == null){
                            helipayUserService.helipayRegister(userBaseInfo);
                        } else if (!"AVAILABLE".equals(helipayUser.getUserStatus())){
                            HelipayHelper helipayHelper = new HelipayHelper();
                            MerchantUserQueryVo userVo = new MerchantUserQueryVo();
                            userVo.setP1_bizType(HelipayConstant.BTYPE_MerchantUserQuery);
                            userVo.setP2_customerNumber(HelipayUtil.customerNumber());
                            userVo.setP3_orderId(HelipayUtil.getOrderId());
                            userVo.setP4_userId(helipayUser.getHelipayUserId());
                            userVo.setP5_timestamp(HelipayUtil.getP8TimeStamp());
                            userVo.setP6_legalPersonID(userBaseInfo.getIdNo());
                            MerchantUserQueryResVo resVo = helipayHelper.userQuery(userVo);
                            Map<String,Object> param = new HashMap<>();
                            if ("AVAILABLE".equals(resVo.getRt7_userStatus())){
                                param.put("id",helipayUser.getId());
                                param.put("backCredentialStatus","UPLOADED");
                                param.put("frontCredentialStatus","UPLOADED");
                                param.put("userStatus","AVAILABLE");
                                helipayUserService.updateSelective(param);
                            } else {
                                param.put("userId",userBaseInfo.getUserId());
                                param.put("idState","10");
                                userAuthService.updateByUserId(param);
                            }
                        }
                    }
                }
                Map ret = new LinkedHashMap();
                ret.put("success", true);
                ret.put("msg", "登录成功");
                ret.put("data", session.getFront());
                return ret;
            }
            Map ret = new LinkedHashMap();
            ret.put("success", false);
            ret.put("msg", "密码错误");
            return ret;
        } catch (Exception e) {
            logger.error("登录异常", e);
            Map ret = new LinkedHashMap();
            ret.put("code", 500);
            ret.put("msg", "系统异常");
            return ret;
        }
    }

    //根据手机号和验证码完成登录
    public Map loginPhone(final HttpServletRequest request, final String loginName,final String vcode) {
        try {
            Map user = mybatisService.queryRec("usr.queryUserByLoginName", loginName);
            if (user == null) {
                Map ret = new LinkedHashMap();
                ret.put("success", false);
                ret.put("msg", "账户不存在");
                return ret;
            }
            ClSmsService clSmsService = (ClSmsService)BeanUtil.getBean("clSmsService");
            int results = clSmsService.verifySms(loginName, SmsModel.SMS_TYPE_USER_LOGIN, vcode);
            String vmsg;
            if (results == 1) {
                vmsg = null;
            }else if(results == -1){
                vmsg="验证码已过期";
            }else {
                vmsg="手机号码或验证码错误";
            }
            if (vmsg != null) {
                Map ret = new LinkedHashMap();
                ret.put("success", false);
                ret.put("msg", vmsg);
                return ret;
            }

            AppSessionBean session = appDbSession.create(request, loginName);
            cloanUserService.modify(loginName);//修改登陆时间
            Map ret = new LinkedHashMap();
            ret.put("success", true);
            ret.put("msg", "登录成功");
            ret.put("data", session.getFront());
            return ret;

        } catch (Exception e) {
            logger.error("登录异常", e);
            Map ret = new LinkedHashMap();
            ret.put("code", 500);
            ret.put("msg", "系统异常");
            return ret;
        }
    }

}