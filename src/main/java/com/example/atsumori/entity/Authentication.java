package com.example.atsumori.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authentication {
	/** ID */
	private Long id;
	/** ユーザー名 */
	private String username;
	/** パスワード */
	private String password;
	/** 権限（例: "USER", "ADMIN"） */
	private String authority;
	/** 表示名 */
	private String displayname;
}
