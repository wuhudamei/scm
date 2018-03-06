package cn.damei.scm.common;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.SqlSessionFactory;

public class SqlFactory extends org.mybatis.spring.SqlSessionFactoryBean {
	@Override
	public SqlSessionFactory buildSqlSessionFactory() {
		try {
			return super.buildSqlSessionFactory();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ErrorContext.instance().reset();
		}
	}

}
