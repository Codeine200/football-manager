package org.example.footballmanager.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return User.builder()
                .username(username)
                .password("$2a$10$LX1s28LrZU32UzJ0LkLMt.zM1m3LyKbsfI85Zx.mRQXQ0C0YbtTVC")
                .roles("USER")
                .build();
    }
}
