package com.example.atsumori.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			// ★アクセス制御の設定
			.authorizeHttpRequests(auth -> auth
				// 新規登録・ログイン画面は誰でもOK
				.requestMatchers("/register", "/login", "/authentication", "/css/**", "/js/**").permitAll()
				// 管理者だけアクセス可能
				//.requestMatchers("/atsumori/**").hasAuthority("ADMIN")
				// それ以外はログイン必須
				.anyRequest().authenticated()
			)
			
			// ★フォームログインの設定
			.formLogin(form -> form
				.loginPage("/login")                  // ログイン画面
				.loginProcessingUrl("/authentication") // 認証処理のURL
				.usernameParameter("usernameInput")    // フォームのname属性
				.passwordParameter("passwordInput")
				.defaultSuccessUrl("/", true)          // 成功時にリダイレクト
				.failureUrl("/login?error")            // 失敗時
				.permitAll()
			)
			
			// ★ログアウト設定
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.permitAll()
			);
		
		return http.build();
	}
}
