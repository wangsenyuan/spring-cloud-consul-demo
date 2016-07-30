package com.me;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

/**
 * Created by wangsenyuan on 7/26/16.
 */
@ComponentScan
@Configuration
public class FacadeConfiguration {

    @Bean
    @Primary
    public RestTemplate createResetTemplate() {
        return new RestTemplate();
    }


}
