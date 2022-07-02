package com.sixzerofour.parkingsystem.config;

import com.alipay.api.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@Configuration
@PropertySource("classpath:alipay.properties")
public class AlipayClientConfig {

    @Resource
    private Environment config;

    @Bean
    public AlipayClient alipayClient() throws AlipayApiException {
        AlipayConfig alipayConfig = new AlipayConfig();

        alipayConfig.setServerUrl(config.getProperty("alipay.gateway-url"));
        alipayConfig.setAppId(config.getProperty("alipay.app-id"));
        alipayConfig.setPrivateKey(config.getProperty("alipay.merchant-private-key"));
        alipayConfig.setFormat(AlipayConstants.FORMAT_JSON);
        alipayConfig.setCharset(AlipayConstants.CHARSET_UTF8);
        alipayConfig.setAlipayPublicKey(config.getProperty("alipay.alipay-public-key"));
        alipayConfig.setSignType(AlipayConstants.SIGN_TYPE_RSA2);
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);

        return alipayClient;
    }
}
