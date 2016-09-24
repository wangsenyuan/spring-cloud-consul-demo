package com.me;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by wangsenyuan on 7/26/16.
 */
@RestController
@RefreshScope
public class Facade {

    @Autowired RestTemplate restTemplate;


    @Value("${message:Hello default}") private String message;

    @RequestMapping("/facade/add/{a}/{b}")
    public int add(@PathVariable("a") int a, @PathVariable("b") int b) {
        int c = restTemplate.getForObject("http://localhost:1111/add/{a}/{b}", Integer.class, a, b);
        return c;
    }

    @RequestMapping("/facade/message")
    public String message() {
        return message;
    }
}
