package com.mdni.scm.common;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.SqlSessionFactory;

public class SqlFactory extends org.mybatis.spring.SqlSessionFactoryBean {
	@Override
	public SqlSessionFactory buildSqlSessionFactory() {
		try {
			return super.buildSqlSessionFactory();
		} catch (Exception e) {
			// XML 有错误时打印异常。  
			e.printStackTrace();
			return null;
		} finally {
			ErrorContext.instance().reset();
		}
	}

}
