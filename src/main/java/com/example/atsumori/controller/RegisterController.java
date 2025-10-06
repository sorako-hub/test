package com.example.atsumori.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.atsumori.repository.AuthenticationMapper;

@Controller
public class RegisterController {

    private final AuthenticationMapper authenticationMapper;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(AuthenticationMapper authenticationMapper,
                              PasswordEncoder passwordEncoder) {
        this.authenticationMapper = authenticationMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // 新規登録フォームを表示
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    // 登録処理
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam(required = false) String displayname) {
        String encodedPassword = passwordEncoder.encode(password);

        authenticationMapper.insertUser(username, encodedPassword, "USER", displayname);

        return "redirect:/login?registered";
    }
}
