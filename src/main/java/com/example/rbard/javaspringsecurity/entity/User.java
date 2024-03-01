package com.example.rbard.javaspringsecurity.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "username", length = 18, nullable = false, unique = true)
  private String username;
  @Column(name = "password", length = 60, nullable = false)
  private String password;
  @Column(name = "enabled")
  private Boolean enabled;
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
  private Set<UserRole> userRoles;

  @PrePersist
  public void prePersist() {
    if(Objects.isNull(enabled)) {
      this.enabled = true;
    }
  }

  public void setRoles(List<Role> roles) {
    Set<UserRole> userRoleSet = new HashSet<>();
    roles.forEach(role ->
        userRoleSet.add(UserRole.builder()
                .role(role)
                .user(this)
            .build()));
    this.userRoles = userRoleSet;
  }

}
