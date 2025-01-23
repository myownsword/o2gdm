package com.tp.o2gdm.o2gdmcore.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.tp.o2gdm.o2gdmcore.mapper.target",sqlSessionFactoryRef = "targetSqlSessionFactory")
public class TargetDataSourceConfig {

    private static final String MAPPER_LOCATION = "classpath:mapper/target/*.xml";

    @Value("${target.dataSource.url}")
    private String url;

    @Value("${target.dataSource.username}")
    private String username;

    @Value("${target.dataSource.password}")
    private String password;

    @Value("${target.dataSource.driver-class-name}")
    private String driverClassName;



    @Bean("targetDataSource")
    public DataSource getTargetDataSource(){
        //创建一个数据源对象，springboot默认为Hikari数据源，我们也用该数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(url);
        return dataSource;



    }

    @Bean("targetTransactionManager")
    public TransactionManager getTargetTransactionManager(){
        return new DataSourceTransactionManager
                ( getTargetDataSource());
    }

    @Bean("targetSqlSessionFactory")
    public SqlSessionFactory targetSqlSessionFactory(@Qualifier("targetDataSource") DataSource targetDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(targetDataSource);
        sqlSessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources(MAPPER_LOCATION)

        );
        sqlSessionFactory.setTypeAliasesPackage("com.tp.o2gdm.o2gdmcore.entity");
        return sqlSessionFactory.getObject();


    }
}