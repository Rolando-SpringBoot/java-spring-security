package com.example.rbard.javaspringsecurity.util.constant;

import io.jsonwebtoken.Jwts.SIG;
import javax.crypto.SecretKey;

public class JwtUtil {

  private JwtUtil() {
  }

  public static final SecretKey SECRET_KEY = SIG.HS256.key().build();
  public static final String BEARER_TOKEN_PREFIX = "Bearer ";

}
