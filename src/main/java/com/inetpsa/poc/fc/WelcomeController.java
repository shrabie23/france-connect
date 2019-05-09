/*
 * Creation : 9 mai 2018
 */
package com.inetpsa.poc.fc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class WelcomeController {
    @GetMapping("/welcome")
    public String sayHello() {
        return "Welcome to www.SpringBootDev.com";
    }
}
