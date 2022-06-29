package com.sixzerofour.parkingsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.Resource;

@Configuration
@PropertySource("classpath:alipay.properties")
public class AlipayClientConfig {


}
