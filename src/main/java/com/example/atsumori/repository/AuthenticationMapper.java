package com.example.atsumori.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.atsumori.entity.Authentication;

@Mapper
public interface AuthenticationMapper {
	
	/**
	 * ユーザー名でログイン情報を取得します
	 */
	Authentication selectByUsername(String username);
	
	 @Insert("INSERT INTO authentications (username, password, authority, displayname) " +
	            "VALUES (#{username}, #{password}, CAST(#{authority} AS role), #{displayname})")
	    void insertUser(@Param("username") String username,
	                    @Param("password") String password,
	                    @Param("authority") String authority,
	                    @Param("displayname") String displayname);

}
