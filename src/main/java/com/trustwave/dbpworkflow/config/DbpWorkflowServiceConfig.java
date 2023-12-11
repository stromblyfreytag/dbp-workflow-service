/*
 * Copyright (c) 2023 Trustwave Holdings, Inc.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information
 * of Trustwave Holdings, Inc.  Use of this software is governed by
 * the terms and conditions of the license statement and limited
 * warranty furnished with the software.
 *
 * IN PARTICULAR, YOU WILL INDEMNIFY AND HOLD TRUSTWAVE HOLDINGS INC.,
 * ITS RELATED COMPANIES AND ITS SUPPLIERS, HARMLESS FROM AND AGAINST
 * ANY CLAIMS OR LIABILITIES ARISING OUT OF OR RESULTING FROM THE USE,
 * MODIFICATION, OR DISTRIBUTION OF PROGRAMS OR FILES CREATED FROM,
 * BASED ON, AND/OR DERIVED FROM THIS SOURCE CODE FILE.
 */
package com.trustwave.dbpworkflow.config;

import javax.sql.DataSource;

import java.util.Properties;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * -- TODO add description here
 *
 * <pre>
 * Copyright (c) 2023 Trustwave Holdings, Inc.
 * All rights reserved.
 * </pre>
 *
 * @author sfreytag
 */
//@Configuration
//@EnableAutoConfiguration
//@ComponentScan(basePackages = {
//        "com.trustwave.dbpworkflow.controller",
//        "com.trustwave.dbpworkflow.service.impl",
//        "com.trustwave.dbpworkflow.task"
//})
public class DbpWorkflowServiceConfig {
//    @Autowired
//    private Environment env;
//
//    @Autowired
//    private ProcessEngine processEngine;

//    @Bean
//    public SpringProcessEngineConfiguration springProcessEngineCfg() {
//        SpringProcessEngineConfiguration springProcessEngineConfiguration = new SpringProcessEngineConfiguration();
//        springProcessEngineConfiguration
//                .setJdbcUrl("jdbc:h2:mem:flowable;DB_CLOSE_DELAY=-1")
//                .setJdbcUsername("sa")
//                .setJdbcPassword("")
//                .setJdbcDriver("org.h2.Driver")
//                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
//        return springProcessEngineConfiguration;
//    }
//
//    @Bean
//    public ProcessEngine processEngine(SpringProcessEngineConfiguration cfg) {
//        return cfg.buildProcessEngine();
//    }
//@Bean
//public LocalSessionFactoryBean assetSessionFactory() throws Exception {
//    Properties properties = new Properties();
//
//    properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
//    properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
//    properties.put("current_session_context_class", //
//            env.getProperty("spring.jpa.properties.hibernate.current_session_context_class"));
//    properties.put("hibernate.jdbc.batch_size", env.getProperty("spring.jpa.properties.hibernate.batch_size"));
//    LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
//
//    // Package contain entity classes
//    factoryBean.setPackagesToScan("com.trustwave.dbpassetservice.domain");
//    factoryBean.setDataSource(dataSource());
//    factoryBean.setHibernateProperties(properties);
//    factoryBean.afterPropertiesSet();
//
//    return factoryBean;
//}
//
//@Bean(name = "AssetHibernateTransaction")
//public PlatformTransactionManager hibernateTransactionManager() {
//    HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//    try {
//        transactionManager.setSessionFactory(assetSessionFactory().getObject());
//    }
//    catch (Exception e) {
//        e.printStackTrace();
//    }
//    return transactionManager;
//}
//@Bean
//public ProcessEngineConfiguration processEngineConfiguration() {
//
//    ProcessEngineConfiguration processEngineConfiguration = new ProcessEngineConfigurationImpl();
//    processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE);
//    return processEngineConfiguration;
//}
}
