package com.ash.project.blogpost.service;

import com.ash.project.blogpost.model.User;
import com.ash.project.blogpost.repository.UserRepository;
import com.ash.project.blogpost.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("UsernameNotFoundException");
        }
        MyUserDetails myUserDetails = new MyUserDetails(user);
        return myUserDetails;
    }
}
