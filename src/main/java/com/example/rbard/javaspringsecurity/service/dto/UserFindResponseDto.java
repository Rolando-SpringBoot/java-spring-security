package com.example.rbard.javaspringsecurity.service.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFindResponseDto implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private Long id;
  private String username;
  private Boolean enabled;
  private List<String> roles;

}
