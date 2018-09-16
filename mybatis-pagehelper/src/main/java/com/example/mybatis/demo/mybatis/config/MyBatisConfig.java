//package com.example.mybatis.demo.mybatis.config;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.DefaultResourceLoader;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class MyBatisConfig {
//
//    //  加载全局的配置文件
//    @Value("${mybatis.config-locations}")
//    private String configLocation;
//
//
//    @Bean
//    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
//
//        //configuration
//        sessionFactoryBean.setConfigLocation(new DefaultResourceLoader().getResource(configLocation));
//        //datasource
//		dataSource.getConnection();
//        sessionFactoryBean.setDataSource(dataSource);
//        return sessionFactoryBean.getObject();
//    }
//
//
//}
