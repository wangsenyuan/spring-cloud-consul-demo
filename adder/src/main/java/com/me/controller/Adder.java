package com.me.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangsenyuan on 7/26/16.
 */
@RestController
public class Adder {

    @RequestMapping("/add/{a}/{b}")
    public int add(@PathVariable("a") int a, @PathVariable("b") int b) {
        return a + b;
    }
}
