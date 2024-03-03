package com.example.rbard.javaspringsecurity.configuration;

import static com.example.rbard.javaspringsecurity.util.constant.RoleEnum.ROLE_ADMIN;
import static com.example.rbard.javaspringsecurity.util.constant.RoleEnum.ROLE_USER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SpringSecurityConfig {

  @Autowired
  private AuthenticationConfiguration authenticationConfiguration;

  @Bean
  AuthenticationManager authenticationManager() throws Exception {
    return this.authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web
        .ignoring()
        .requestMatchers("/h2-console/**");
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(auth -> auth
            .requestMatchers(GET,"/api/users").permitAll()
            .requestMatchers(POST, "/api/users/register").permitAll()
            .requestMatchers(POST, "/api/users").hasAnyRole(ROLE_USER.getDesc(), ROLE_ADMIN.getDesc())
            .requestMatchers(GET, "/api/products", "/api/products/{id}").hasAnyRole(ROLE_ADMIN.getDesc())
            .anyRequest().authenticated())
        .addFilter(new JwtAuthenticationFilter(authenticationManager()))
        .addFilter(new JwtValidationFilter(authenticationManager()))
        .csrf(AbstractHttpConfigurer::disable)
        /*
          Agregando configuración de cors a spring security
         */
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .sessionManagement(management -> management.sessionCreationPolicy(STATELESS))
        .build();
  }

  /*
    Configuracion de cors de spring
   */
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOriginPatterns(List.of("*"));
    config.setAllowedMethods(List.of(
        GET.name(), POST.name(), DELETE.name(), PUT.name()
    ));
    config.setAllowedHeaders(List.of(AUTHORIZATION, CONTENT_TYPE));
    config.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    /*
      Ruta que define desde donde se va a aplicar la configuración de cors,
      siendo en este caso desde la raíz.
     */
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  /*
    Filtro que hace uso de la configuración de cors
   */
  @Bean
  FilterRegistrationBean<CorsFilter> corsFilter() {
    FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(
        new CorsFilter(corsConfigurationSource()));
    /*
      Definiendo la prioridad
     */
    corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return corsBean;
  }

}
