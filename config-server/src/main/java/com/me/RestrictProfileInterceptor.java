package com.me;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

/**
 * Created by wangsenyuan on 9/27/16.
 */
@Component
public class RestrictProfileInterceptor implements WebRequestInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RestrictProfileInterceptor.class);
    @Value("${spring.active.profile:default}") private String profile;

    @Autowired Allowed allowed;

    @Override
    public void preHandle(WebRequest request) throws Exception {
        String path = ((DispatcherServletWebRequest) request).getRequest().getServletPath();

        if (allowed.allowUrl(path) || allowed.allowApplication(path, profile)) {
            return;
        }

        logger.warn("{} is not allowed", path);
        throw new Exception(path + " is not allowed");
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {

    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {

    }

}
