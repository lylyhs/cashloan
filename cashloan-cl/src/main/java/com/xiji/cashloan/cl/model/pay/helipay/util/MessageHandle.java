package com.xiji.cashloan.cl.model.pay.helipay.util;

import com.xiji.cashloan.cl.model.pay.helipay.annotation.FieldEncrypt;
import com.xiji.cashloan.cl.model.pay.helipay.annotation.SignExclude;
import java.lang.reflect.Field;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by heli50 on 2018-06-25.
 */
public class MessageHandle {

	private static final Log log = LogFactory.getLog(MessageHandle.class);

	private static String PFX_PATH = "d:/merchant0002.pfx";        //商户pfx
	private static String PFX_PWD = "1234qwer";    //pfx密码
	private static final String ENCRYPTION_KEY = "encryptionKey";
	private static final String SPLIT = "&";
	private static final String SIGN = "sign";

	public static void setPfxPath(String path) {
		PFX_PATH = path;
	}

	public static void setPfxPwd(String pfxPwd) {
		PFX_PWD = pfxPwd;
	}
	/**
	 * 获取map
	 */
	public static Map getReqestMap(Object bean) throws Exception {

		Map retMap = new HashMap();

		boolean isEncrypt = false;
		String aesKey = AES.generateString(16);
		StringBuilder sb = new StringBuilder();

		Class clazz = bean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String key = field.toString().substring(field.toString().lastIndexOf(".") + 1);
			String value = (String) field.get(bean);
			if (value == null) {
				value = "";
			}
			//查看是否有需要加密字段的注解,有则加密
			//这部分是将需要加密的字段先进行加密
			if (field.isAnnotationPresent(FieldEncrypt.class) && StringUtils.isNotEmpty(value)) {
				isEncrypt = true;
				value = AES.encryptToBase64(value, aesKey);
			}

			//字段没有@SignExclude注解的拼签名串
			//这部分是把需要参与签名的字段拼成一个待签名的字符串
			if (!field.isAnnotationPresent(SignExclude.class)) {
				sb.append(SPLIT);
				sb.append(value);
			}

			retMap.put(key, value);
		}

		//如果有加密的，需要用合利宝的公钥将AES加密的KEY进行加密使用BASE64编码上送
		if (isEncrypt) {
			PublicKey publicKey = RSA.getPublicKeyByCert();
			String encrytionKey = RSA.encodeToBase64(aesKey, publicKey, ConfigureEncryptAndDecrypt.KEY_ALGORITHM);
			retMap.put(ENCRYPTION_KEY, encrytionKey);
		}

		if (HelipayUtil.isLogSign()) {
			log.info("原签名串：" + sb.toString());
		}

		//使用商户的私钥进行签名
		PrivateKey privateKey = RSA.getPrivateKey();
		String sign = RSA.sign(sb.toString(), privateKey);
		retMap.put(SIGN, sign);
		if (HelipayUtil.isLogSign()) {
			log.info("签名sign：" + sign);
		}
		return retMap;
	}


	public static boolean checkSign(Object bean) throws Exception {

		boolean flag = false;

		StringBuilder sb = new StringBuilder();

		Class clazz = bean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		String sign = "";
		for (Field field : fields) {
			field.setAccessible(true);
			String key = field.toString().substring(field.toString().lastIndexOf(".") + 1);
			String value = (String) field.get(bean);
			if (value == null) {
				value = "";
			}

			if (SIGN.equals(key)) {
				sign = value;
			}

			//字段没有@SignExclude注解的拼签名串
			//这部分是把需要参与签名的字段拼成一个待签名的字符串
			if (!field.isAnnotationPresent(SignExclude.class)) {
				sb.append(SPLIT);
				sb.append(value);
			}

		}

		if (HelipayUtil.isLogSign()) {
			log.info("response验签原签名串：" + sb.toString());
		}
		//使用合利宝的公钥进行验签
		PublicKey publicKey = RSA.getPublicKeyByCert();
		flag = RSA.verifySign(sb.toString(), sign, publicKey);
		if (HelipayUtil.isLogSign()) {
			if (flag) {
				log.info("验签成功");
			} else {
				log.info("验签失败");
			}
		}

		return flag;

	}


	public static void main(String[] args) throws Exception {
	}

}
