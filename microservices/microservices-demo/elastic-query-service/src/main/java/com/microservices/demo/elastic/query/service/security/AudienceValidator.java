package com.microservices.demo.elastic.query.service.security;

import com.microservices.demo.config.ElasticQueryServiceConfigData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Qualifier("elastic-query-service-audience-validator")
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final ElasticQueryServiceConfigData configData;


    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        if (token.getAudience().contains(configData.getCustomAudience())) {
            return OAuth2TokenValidatorResult.success();
        } else {
            var error = new OAuth2Error("invalid_token",
                    "The token does not have the expected audience.", null);
            return OAuth2TokenValidatorResult.failure(error);
        }
    }
}
