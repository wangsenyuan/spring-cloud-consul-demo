package com.me.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangsenyuan on 7/26/16.
 */
@RestController
public class Adder {

    private static final Logger logger = LoggerFactory.getLogger(Adder.class);

    @RequestMapping("/add/{a}/{b}")
    public int add(@PathVariable("a") int a, @PathVariable("b") int b) {
        logger.info("add({}, {})", a, b);
        return a + b;
    }
}
