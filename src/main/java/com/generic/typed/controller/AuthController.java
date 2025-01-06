package com.generic.typed.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
public class AuthController {

    @GetMapping("/auth/login")
    public String login() {
        return "로그인 스크린";
    }
}
