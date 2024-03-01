package com.example.rbard.javaspringsecurity.configuration;

import static com.example.rbard.javaspringsecurity.util.constant.JwtUtil.BEARER_TOKEN_PREFIX;
import static com.example.rbard.javaspringsecurity.util.constant.JwtUtil.SECRET_KEY;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.example.rbard.javaspringsecurity.entity.User;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;

  private final ObjectMapper objectMapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  @SneakyThrows
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    User user = objectMapper.readValue(request.getInputStream(), User.class);

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
    return authenticationManager.authenticate(authenticationToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
    String username = ((org.springframework.security.core.
        userdetails.User) authResult.getPrincipal()).getUsername();
    List<String> roles = authResult.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();

    String token = Jwts.builder()
        .subject(username)
        .expiration(Date.from(Instant.now().plusSeconds(30)))
        .issuedAt(Date.from(Instant.now()))
        .signWith(SECRET_KEY)
        .claims(Collections.singletonMap("authorities", roles))
        .compact();

    Map<String, String> body = new HashMap<>();
    body.put("token", token);
    body.put("username", username);
    body.put("message", STR. "Hola \{ username } has iniciado sesion con exito" );

    response.getWriter().write(objectMapper.writeValueAsString(body));
    response.addHeader(AUTHORIZATION, BEARER_TOKEN_PREFIX + token);
    response.setContentType(APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.OK.value());
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {
    Map<String, String> body = new HashMap<>();
    body.put("message", "Error en la autenticacion username o password incorrectos!");
    body.put("error", failed.getMessage());

    response.getWriter().write(objectMapper.writeValueAsString(body));
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(APPLICATION_JSON_VALUE);
  }

}