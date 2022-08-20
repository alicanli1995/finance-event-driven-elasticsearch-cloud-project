package com.microservices.demo.elastic.query.service.security;


import com.microservices.demo.elastic.model.index.model.FinanceAvroDTO;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryRequestModel;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryResponseModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceAnalyticsResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Component
public class QueryServicePermissionEvaluator implements PermissionEvaluator {

    private static final String SUPER_USER_ROLE = "APP_SUPER_USER_ROLE";

    private final HttpServletRequest httpServletRequest;

    public QueryServicePermissionEvaluator(HttpServletRequest request) {
        this.httpServletRequest = request;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean hasPermission(Authentication authentication,
                                 Object targetDomain,
                                 Object permission) {
        if (isSuperUser()) {
            return true;
        }
        if (targetDomain instanceof ElasticQueryRequestModel) {
            return preAuthorize(authentication, ((ElasticQueryRequestModel) targetDomain).getId(), permission);
        } else if (targetDomain instanceof ResponseEntity || targetDomain == null) {
            if (targetDomain == null) {
                return true;
            }
            ElasticQueryServiceAnalyticsResponseModel responseBody =
                    ((ResponseEntity<ElasticQueryServiceAnalyticsResponseModel>) targetDomain).getBody();
            Objects.requireNonNull(responseBody);
            return postAuthorize(authentication, ElasticQueryResponseModel
                            .builder()
                    .createdAt(ZonedDateTime.now())
                    .id(UUID.randomUUID().toString())
                    .shareData(
                            FinanceAvroDTO
                                .builder()
                                    .last(responseBody.getShareVolume())
                                    .c(responseBody.getQueryResponseModels())
                                .build()
                    )
                            .build()
                    , permission);
        }
        return false;
    }



    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        if (isSuperUser()) {
            return true;
        }
        if (targetId == null) {
            return false;
        }
        return preAuthorize(authentication, (String) targetId, permission);
    }

    private boolean preAuthorize(Authentication authentication, String id, Object permission) {
        FinanceQueryUser twitterQueryUser = (FinanceQueryUser) authentication.getPrincipal();
        PermissionType userPermission = twitterQueryUser.getPermissions().get(id);
        return hasPermission((String) permission, userPermission);
    }

    private boolean postAuthorize(Authentication authentication,
                                  ElasticQueryResponseModel responseBody,
                                  Object permission) {
        FinanceQueryUser financeQueryUser = (FinanceQueryUser) authentication.getPrincipal();

            PermissionType userPermission = financeQueryUser.getPermissions().get(responseBody.getId());
        return hasPermission((String) permission, userPermission);
    }

    private boolean hasPermission(String requiredPermission, PermissionType userPermission) {
        return userPermission != null && requiredPermission.equals(userPermission.getType());
    }

    private boolean isSuperUser() {
        return httpServletRequest.isUserInRole(SUPER_USER_ROLE);
    }
}
