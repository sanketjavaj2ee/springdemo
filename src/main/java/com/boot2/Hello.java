package com.boot2;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {

    @RequestMapping("/")
    //@ResponseBody
    String home() {
        return "Hello World!";
    }
}
