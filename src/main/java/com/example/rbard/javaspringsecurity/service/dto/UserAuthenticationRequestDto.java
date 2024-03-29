package com.example.rbard.javaspringsecurity.service.dto;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthenticationRequestDto implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String username;
  private String password;

}
