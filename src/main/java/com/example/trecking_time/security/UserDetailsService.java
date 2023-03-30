package com.example.trecking_time.security;


import com.example.trecking_time.entity.User;
import com.example.trecking_time.enums.Role;
import com.example.trecking_time.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private UserRepository userRepository;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role: user.getRole()) {
            authorities.add(new SimpleGrantedAuthority(role.name()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),true,true,true,true,authorities);
    }
}
