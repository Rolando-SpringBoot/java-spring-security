package com.example.rbard.javaspringsecurity.service.dto;

import com.example.rbard.javaspringsecurity.util.annotation.ExistsByUsername;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSaveRequestDto implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @ExistsByUsername
  @NotBlank(message = "username is required")
  private String username;
  @NotBlank(message = "password is required")
  private String password;
  private Boolean enabled;
  private Boolean admin;

}
