package com.example.atsumori.utility;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * ハッシュ化した文字列を返すクラス
 */
public class PasswordGenerator {
	public static void main(String[] args) {
		// 「BCrypt」のインスタンス化
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		// 入力値
		String rawPassword = "adminpass";
		// パスワードをハッシュ化
		String encodedPassword = encoder.encode(rawPassword);
		// 表示
		System.out.println("ハッシュ化されたパスワード：" + encodedPassword);
		// 入力値
				String rawPassword2 = "userpass";
				// パスワードをハッシュ化
				String encodedPassword2 = encoder.encode(rawPassword2);
				// 表示
				System.out.println("ハッシュ化されたパスワード：" + encodedPassword2);
	}

}
