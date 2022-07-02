package com.sixzerofour.parkingsystem.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@MapperScan("com.sixzerofour.parkingsystem.mapper")
@EnableTransactionManagement//事务管理
public class MyBatisPlusConfig {
}
