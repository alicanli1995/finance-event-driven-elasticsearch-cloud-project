package com.microservices.demo.elastic.query.service.service.impl;

import com.microservices.demo.elastic.query.service.dataaccess.entity.UserPermission;
import com.microservices.demo.elastic.query.service.dataaccess.repository.UserPermissionRepository;
import com.microservices.demo.elastic.query.service.service.QueryUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceQueryUserService implements QueryUserService {

    private final UserPermissionRepository userPermissionRepository;

    @Override
    public Optional<List<UserPermission>> findAllPermissionsByUsername(String username) {
        log.info("Finding permissions by username {}", username);
        return userPermissionRepository.findUserPermissionByUsername(username);
    }
}
