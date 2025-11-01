package com.maheshbhatt.financialnuggets.controller;

import com.maheshbhatt.financialnuggets.model.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HomeController {

    @GetMapping("/")
    public String welcome() {
        return "Welcome to Financial Nuggets API!";
    }

    @GetMapping("/user")
    public User user() {
        User user = new User();
        user.setId("1");
        user.setName("Mahesh Bhatt");
        user.setEmailId("maheshbhatt.dev@gmail.com");
        return user;
    }

    @GetMapping("/path-variable/{name}")
    public String pathVariable(@PathVariable String name) {
        return "Hello, " + name + "!";
    }

    @GetMapping("/request-param")
    public String requestParam(@RequestParam String name, @RequestParam(name = "age", required = false, defaultValue = "") String age) {
        return "Hello, " + name + " of age " + age + "!";
    }
}
