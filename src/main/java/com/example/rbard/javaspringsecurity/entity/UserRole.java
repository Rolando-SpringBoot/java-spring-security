package com.example.rbard.javaspringsecurity.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users_roles")
@Entity
@IdClass(UserRoleId.class)
public class UserRole {

  @Id
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  @ManyToOne(optional = false)
  private User user;
  @Id
  @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
  @ManyToOne(optional = false)
  private Role role;

}
