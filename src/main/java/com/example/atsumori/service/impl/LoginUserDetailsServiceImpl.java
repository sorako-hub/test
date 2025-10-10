package com.example.atsumori.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.atsumori.entity.Authentication;
import com.example.atsumori.entity.LoginUser;
import com.example.atsumori.entity.Role;
import com.example.atsumori.repository.AuthenticationMapper;

import lombok.RequiredArgsConstructor;

/**
 * カスタム認証サービス
 */
@Service
@RequiredArgsConstructor
public class LoginUserDetailsServiceImpl implements UserDetailsService {

	private final AuthenticationMapper authenticationMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 「認証テーブル」からデータを取得
		Authentication authentication = authenticationMapper.selectByUsername(username);

		if (authentication != null) {
			// 権限の文字列を列挙型に変換（例: "ADMIN" → Role.ADMIN）
			Role role;
			try {
				role = Role.valueOf(authentication.getAuthority());
			} catch (IllegalArgumentException | NullPointerException e) {
				throw new UsernameNotFoundException("無効な権限が指定されています: " + authentication.getAuthority());
			}

			// UserDetailsの実装クラスを返す
			return new LoginUser(
				authentication.getId(),
				authentication.getUsername(),
				authentication.getPassword(),
				getAuthorityList(role),
				authentication.getDisplayname()
			);
		} else {
			throw new UsernameNotFoundException(username + " => 指定しているユーザー名は存在しません");
		}
	}

	/**
	 * 権限情報をリストで取得する
	 */
	private List<GrantedAuthority> getAuthorityList(Role role) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role.name()));

		// ADMIN ロールの場合は USER 権限も付与
		if (role == Role.ADMIN) {
			authorities.add(new SimpleGrantedAuthority(Role.USER.name()));
		}
		return authorities;
	}
}
