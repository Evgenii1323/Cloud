package com.example.cloud.security;

import com.example.cloud.model.Role;
import com.example.cloud.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtEntityFactory {

    public static JwtEntity create(User user) {
        List<Role> list = new LinkedList<>();
        list.add(Role.USER);
        return new JwtEntity(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                mapToGrantedAuthorities(list)
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}