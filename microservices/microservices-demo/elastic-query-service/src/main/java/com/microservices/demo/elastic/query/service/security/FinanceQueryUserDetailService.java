package com.microservices.demo.elastic.query.service.security;

import com.microservices.demo.elastic.query.service.converter.UserPermissionToUserDetailConverter;
import com.microservices.demo.elastic.query.service.service.QueryUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceQueryUserDetailService implements UserDetailsService {

    private final QueryUserService queryUserService;

    private final UserPermissionToUserDetailConverter userPermissionsToUserDetailTransformer;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return queryUserService
                .findAllPermissionsByUsername(username)
                .map(userPermissionsToUserDetailTransformer::getUserDetails)
                .orElseThrow(
                        () -> new UsernameNotFoundException("No user found with name " + username));
    }
}
