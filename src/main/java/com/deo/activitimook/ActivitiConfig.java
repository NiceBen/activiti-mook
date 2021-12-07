//package com.deo.activitimook;
//
//import org.activiti.spring.SpringProcessEngineConfiguration;
//import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.annotation.Resource;
//import javax.sql.DataSource;
//
////声名为配置类，继承Activiti抽象配置类，达梦数据库数据源配置类
//@Configuration
//public class ActivitiConfig extends AbstractProcessEngineAutoConfiguration {
//
//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource activitiDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Resource
//    PlatformTransactionManager activitiTransactionManager;
//
//
//    @Bean
//    public SpringProcessEngineConfiguration springProcessEngineConfiguration(){
//        System.out.println("注入activiti的config");
//        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
//         DataSource dataSource = activitiDataSource();
//         configuration.setDataSource(dataSource);
//         configuration.setDatabaseType("oracle");
//         configuration.setTransactionManager(activitiTransactionManager);
//         configuration.setDatabaseSchemaUpdate("none");
//        return  configuration;
//    }
//}