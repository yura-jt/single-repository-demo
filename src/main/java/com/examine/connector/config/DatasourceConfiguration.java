package com.examine.connector.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfiguration {

    @Bean("first-db")
    @Primary
    public JdbcTemplate firstJDBCTemplate(@Qualifier("first-datasource") DataSource firstDataSource) {
        return new JdbcTemplate(firstDataSource);
    }

    @Bean("second-db")
    public JdbcTemplate secondJDBCTemplate(@Qualifier("second-datasource") DataSource secondDataSource) {
        return new JdbcTemplate(secondDataSource);
    }

    @Bean("first-datasource")
    public DataSource firstDataSource(Environment env) {
        return buildDataSource(env, "");
    }

    @Bean("second-datasource")
    public DataSource secondDataSource(Environment env) {
        return buildDataSource(env, "second-");
    }

    private DataSource buildDataSource(Environment env, String datasourcePrefix) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring." + datasourcePrefix + "datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring." + datasourcePrefix + "datasource.jdbc-url"));
        dataSource.setUsername(env.getProperty("spring." + datasourcePrefix + "datasource.username"));
        dataSource.setPassword(env.getProperty("spring." + datasourcePrefix + "datasource.password"));

        return dataSource;
    }

    @Bean(name = "txManager1")
    @Autowired
    DataSourceTransactionManager txManager1(@Qualifier("first-datasource") DataSource datasource) {
        return new DataSourceTransactionManager(datasource);
    }

    @Bean(name = "txManager2")
    @Autowired
    DataSourceTransactionManager txManager2(@Qualifier("second-datasource") DataSource datasource) {
        return new DataSourceTransactionManager(datasource);
    }

    @Bean(name = "chainedTransactionManager")
    @Primary
    public ChainedTransactionManager transactionManager(
            @Qualifier("txManager1") PlatformTransactionManager fooTx,
            @Qualifier("txManager2") PlatformTransactionManager barTx
    ) {
        return new ChainedTransactionManager(fooTx, barTx);
    }


}
