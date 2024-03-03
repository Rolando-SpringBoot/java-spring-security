package com.example.rbard.javaspringsecurity.util.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleEnum {
  ROLE_USER("USER"),ROLE_ADMIN("ADMIN");

  private final String desc;

}
