package com.example.rbard.javaspringsecurity.configuration;

import static com.example.rbard.javaspringsecurity.util.constant.JwtUtil.BEARER_TOKEN_PREFIX;
import static com.example.rbard.javaspringsecurity.util.constant.JwtUtil.SECRET_KEY;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtValidationFilter extends BasicAuthenticationFilter {

  private final ObjectMapper objectMapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  public JwtValidationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    String header = request.getHeader(AUTHORIZATION);
    if(Objects.isNull(header) || !header.startsWith(BEARER_TOKEN_PREFIX)) {
      chain.doFilter(request, response);
      return;
    }
    String token = header.replace(BEARER_TOKEN_PREFIX, "");

    try {
      Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build()
          .parseSignedClaims(token).getPayload();
      String username = claims.getSubject();
      Object authorities = claims.get("authorities");

      List<String> roles = objectMapper.readValue(authorities.toString()
          .getBytes(StandardCharsets.UTF_8), new TypeReference<>() {
      });

      List<GrantedAuthority> grantedAuthorities = roles.stream()
          .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

      UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      chain.doFilter(request, response);
    } catch(JwtException ex) {
      Map<String, String> body = new HashMap<>();
      body.put("error", ex.getMessage());
      body.put("message", "El token JWT es invalido");

      response.getWriter().write(objectMapper.writeValueAsString(body));
      response.setStatus(HttpStatus.FORBIDDEN.value());
      response.setContentType(APPLICATION_JSON_VALUE);
    }
  }

}
