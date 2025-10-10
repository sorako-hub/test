package com.example.atsumori.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import com.example.atsumori.entity.Authentication;

@Mapper
public interface AuthenticationMapper {
	
    /**
     * ユーザー名でログイン情報を取得します
     */
    Authentication selectByUsername(String username);

    /**
     * 新規ユーザーを登録します（文字列として authority を保存）
     */
    @Insert("INSERT INTO authentications (username, password, authority, displayname) " +
            "VALUES (#{username}, #{password}, #{authority}, #{displayname})")
    void insertUser(@Param("username") String username,
                    @Param("password") String password,
                    @Param("authority") String authority,
                    @Param("displayname") String displayname);

    /**
     * エンティティを使用してユーザーを登録します（自動採番ID付き）
     */
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(Authentication user);
}
