package com.microservices.demo.elastic.query.service.converter;

import com.microservices.demo.elastic.query.service.dataaccess.entity.UserPermission;
import com.microservices.demo.elastic.query.service.security.FinanceQueryUser;
import com.microservices.demo.elastic.query.service.security.PermissionType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserPermissionToUserDetailConverter {

    public FinanceQueryUser getUserDetails(List<UserPermission> userPermissions) {
        if (userPermissions.isEmpty()) {
            return FinanceQueryUser.builder().build();
        }
        return FinanceQueryUser.builder()
                .username(userPermissions.get(0).getUsername())
                .permissions(userPermissions.stream()
                        .collect(Collectors.toMap(
                                UserPermission::getDocumentId,
                                permission -> PermissionType.valueOf(permission.getPermissionType()))))
                .build();
    }


}
