package com.bdi.sb.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
@Slf4j
public class TransactionAOP {

	@Resource
	private DataSourceTransactionManager dstm;
	
	@Bean
	@ConfigurationProperties(prefix="spring.datasource.hikari")
	public DataSource getDS() {
		return DataSourceBuilder.create().build();
	}
	@Bean
	public DataSourceTransactionManager txManager() {
		return new DataSourceTransactionManager(getDS());
	}
	
	@Bean
	public TransactionInterceptor txInterceptor() {
		log.debug("transaction starts...");
		TransactionInterceptor txInterceptor = new TransactionInterceptor();
		Properties prop = new Properties();
		List<RollbackRuleAttribute> rollbackRules = new ArrayList<>();
		rollbackRules.add(new RollbackRuleAttribute(Exception.class));
		//exception 발생시 무조건 rollback
		DefaultTransactionAttribute readOnly = new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED);
		readOnly.setReadOnly(true);
		readOnly.setTimeout(30);
		RuleBasedTransactionAttribute update = new RuleBasedTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED,rollbackRules);
		update.setTimeout(30);
		prop.setProperty("select*", readOnly.toString());
		prop.setProperty("get*", readOnly.toString());
		prop.setProperty("find*", readOnly.toString());
		prop.setProperty("search*", readOnly.toString());
		prop.setProperty("count*", readOnly.toString());
		prop.setProperty("*", update.toString());
		txInterceptor.setTransactionAttributes(prop);
		txInterceptor.setTransactionManager(dstm);
		return txInterceptor;
	}
	
	@Bean
	public Advisor txAdvisor() {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("execution(* com.grabit.bdi.service..*ServiceImpl.*(..))");
		return new DefaultPointcutAdvisor(pointcut, txInterceptor());
	}
}
