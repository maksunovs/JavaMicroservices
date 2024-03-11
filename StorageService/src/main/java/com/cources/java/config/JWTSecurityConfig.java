//package com.cources.java.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class JWTSecurityConfig {
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        return http
////                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
////                    authorizationManagerRequestMatcherRegistry
////                            .requestMatchers(HttpMethod.GET, "/storages/**")
////                            .permitAll()
////                            .anyRequest().authenticated();
////                })
////                .oauth2Login(Customizer.withDefaults())
////                .build();
////
////
////    }
//
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.securityMatcher("/storages/**")
//                .authorizeHttpRequests(authorize -> authorize.anyRequest()
//                        .hasAuthority("storages.read"))
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
//        return http.build();
//    }
//}
