package com.example.rbard.javaspringsecurity.service.dto;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveResponseDto implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private Long id;
  private String username;
  private Boolean enabled;

}
