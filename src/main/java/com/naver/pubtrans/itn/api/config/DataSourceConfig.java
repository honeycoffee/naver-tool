package com.naver.pubtrans.itn.api.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(value="com.naver.pubtrans.itn.api.dao*", sqlSessionFactoryRef = "MySQLSessionFactory")
public class DataSourceConfig {

	@Value("${mybatis.config-location}")
    private String mybatisConfigLocaton;
    @Value("${mybatis.mapper-locations}")
    private String mybatisMapperLocaton;
	@Autowired
	private DataSource dataSource;
	
	@Bean(name = "MySQLSessionFactory")
	public SqlSessionFactory getMySQLSessionFactory(ApplicationContext context) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(context.getResource(mybatisConfigLocaton));
        sqlSessionFactoryBean.setMapperLocations(context.getResources(mybatisMapperLocaton));
        
        return sqlSessionFactoryBean.getObject();
	}
	
    @Bean(name = "MySQLSessionTemplate")
    public SqlSessionTemplate getMySQLSessionTemplate(SqlSessionFactory mysqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(mysqlSessionFactory);
    }
    
}
