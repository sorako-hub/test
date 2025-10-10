package com.example.atsumori.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, 
            HttpServletResponse response, 
            Authentication authentication) throws ServletException, java.io.IOException {
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof com.example.atsumori.entity.LoginUser) {
            com.example.atsumori.entity.LoginUser loginUser = (com.example.atsumori.entity.LoginUser) principal;
            Long userId = loginUser.getId();
            
            HttpSession session = request.getSession();
            session.setAttribute("userId", userId);
            System.out.println("ログイン成功: userId=" + userId);

        }
        
        // ログイン成功後の遷移先（ここはあなたの設定に合わせて）
        response.sendRedirect("/menu");
        
    }
}
