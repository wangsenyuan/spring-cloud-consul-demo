package com.me;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by wangsenyuan on 9/27/16.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Autowired RestrictProfileInterceptor restrictProfileInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addWebRequestInterceptor(restrictProfileInterceptor);
    }
}
