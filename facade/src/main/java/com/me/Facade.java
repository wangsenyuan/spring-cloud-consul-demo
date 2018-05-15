package com.me;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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


    @Value("${message2:Hello default}") private String message2;

    @Value("${backend.svc.adder.endpoint}") private String adderSvcEndpoint;

    @RequestMapping("/facade/add/{a}/{b}")
    @HystrixCommand(fallbackMethod = "addFail", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "2000")})
    public int add(@PathVariable("a") int a, @PathVariable("b") int b) {
        System.out.println("in norma add (" + a + ", " + b + ")");
        try {
            int c = restTemplate.getForObject(adderSvcEndpoint + "/add/{a}/{b}", Integer.class, a, b);
            return c;
        } catch (RuntimeException ex) {
            System.out.println("error happened " + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
    }

    public int addFail(int a, int b, Throwable ex) {
        System.out.println("in addFail(" + a + ", " + b + ")");
        if (ex != null) {
            ex.printStackTrace();
        } else {
            System.out.println("null ex");
        }
        return -1;
    }

    @RequestMapping("/facade/message")
    public String message() {
        return message;
    }

    @RequestMapping("/facade/message2")
    public String message2() {
        return message2;
    }
}
