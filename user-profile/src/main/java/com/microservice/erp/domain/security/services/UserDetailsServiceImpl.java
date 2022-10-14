package com.microservice.erp.domain.security.services;

import com.microservice.erp.domain.entities.UserInfo;
import com.microservice.erp.domain.repositories.IUserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    IUserInfoRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user;
        user = userRepository.findByUsername(username);
        if (user == null) {
            user = userRepository.findByCid(username);
        }
        if (user == null) {
            user = userRepository.findByEmail(username);
        }
        if (user == null) {
            return (UserDetails) new UsernameNotFoundException("User Not Found with username: " + username);
        }

        return UserDetailsImpl.build(user);
    }

}
