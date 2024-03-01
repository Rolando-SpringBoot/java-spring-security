package com.example.rbard.javaspringsecurity.util;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {

  private PasswordUtil() {
  }

  public static String encryptPassword(String unencryptedPassword,
      PasswordEncoder passwordEncoder) {
    return Optional.ofNullable(unencryptedPassword)
        .map(passwordEncoder::encode)
        .orElse("");
  }

}
