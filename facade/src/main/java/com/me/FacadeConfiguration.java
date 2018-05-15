package com.me;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by wangsenyuan on 7/26/16.
 */
@ComponentScan
@Configuration
@EnableSwagger2
public class FacadeConfiguration {

    @Bean
    @Primary
    public RestTemplate createResetTemplate(RestTemplateBuilder builder) {
        return builder.setConnectTimeout(1000).setReadTimeout(1000).build();
    }

    @Bean
    public Docket petApi() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any()).build();
    }
}
