package com.microservices.demo.elastic.query.service.config;

import com.microservices.demo.elastic.query.service.security.FinanceQueryUserDetailService;
import com.microservices.demo.elastic.query.service.security.FinanceQueryUserJwtConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;


@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final FinanceQueryUserDetailService financeQueryUserDetailService;

    private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    @Value("${security.paths-to-ignore}")
    private String[] pathsToIgnore;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .anyRequest()
                .fullyAuthenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(financeQueryUserJwtConverter());
    }

    @Bean
    JwtDecoder jwtDecoder(@Qualifier("elastic-query-service-audience-validator")
                          OAuth2TokenValidator<Jwt> audienceValidator) {
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(
                oAuth2ResourceServerProperties.getJwt().getIssuerUri());
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer
                (oAuth2ResourceServerProperties.getJwt().getIssuerUri());
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
        jwtDecoder.setJwtValidator(withAudience);
        return jwtDecoder;
    }


    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity
                .ignoring()
                .antMatchers(pathsToIgnore);
    }

    @Bean
    Converter<Jwt, ? extends AbstractAuthenticationToken> financeQueryUserJwtConverter() {
        return new FinanceQueryUserJwtConverter(financeQueryUserDetailService);
    }

}
