package com.xiji.cashloan.core.common.util.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wnb
 * @date 2018/11/27
 * @version 1.0.0
 */
public class Create {
	
	public static final Logger logger = LoggerFactory.getLogger(Create.class);
	public static void main(String[] args) {
		Create ot = new Create();
		ot.test();
	}
	
	public void test(){

		// 数据库连接信息
		String url = "jdbc:mysql://172.16.90.38:3306/cashloan_master1.0.4?useUnicode=true&characterEncoding=utf8";
		String MysqlUser = "root";
		String mysqlPassword = "xiji.com";
		
		// 数据库及数据表名称
		String database = "cashloan_master1.0.4";
		String table = "cl_borrow";
		
		// 配置作者及Domain说明
		String classAuthor = "xx";
		String functionName = "借款订单";
 
		// 公共包路径 (例如 BaseDao、 BaseService、 BaseServiceImpl)
		String commonName ="com.xiji.cashloan.core.common";
		
		String packageName ="com.xiji.cashloan.rc";
		String moduleName = "borrow";

		//Mapper文件存储地址  默认在resources中
		String batisName = "config/mappers";
		String db="mysql";
		
		//类名前缀
		String classNamePrefix = "borrow";

		try {
			MybatisGenerate.generateCode(db,url, MysqlUser, mysqlPassword, database, table,commonName,packageName,batisName,moduleName,classAuthor,functionName,classNamePrefix);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

}
