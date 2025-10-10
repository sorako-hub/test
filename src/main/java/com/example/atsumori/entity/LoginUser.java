package com.example.atsumori.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class LoginUser extends User {

    private Long id; // ★ 追加
    private String displayname;

    public LoginUser(Long id, String username, String password,
                     Collection<? extends GrantedAuthority> authorities,
                     String displayname) {
        super(username, password, authorities);
        this.id = id;
        this.displayname = displayname;
    }

    public Long getId() {
        return id;
    }

    public String getDisplayname() {
        return displayname;
    }
}

