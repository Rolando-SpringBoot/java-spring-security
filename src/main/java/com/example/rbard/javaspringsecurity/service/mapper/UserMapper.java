package com.example.rbard.javaspringsecurity.service.mapper;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;

import com.example.rbard.javaspringsecurity.entity.Role;
import com.example.rbard.javaspringsecurity.entity.User;
import com.example.rbard.javaspringsecurity.entity.UserRole;
import com.example.rbard.javaspringsecurity.service.dto.UserCreateRequestDto;
import com.example.rbard.javaspringsecurity.service.dto.UserFindResponseDto;
import com.example.rbard.javaspringsecurity.service.dto.UserSaveRequestDto;
import com.example.rbard.javaspringsecurity.service.dto.UserSaveResponseDto;
import com.example.rbard.javaspringsecurity.util.PasswordUtil;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", nullValueCheckStrategy = ALWAYS, imports = PasswordUtil.class)
public interface UserMapper {

  @Mapping(qualifiedByName = "getRolesRelatedToUser", source = "user", target = "roles")
  UserFindResponseDto mapToUserFindResponseDto(User user);

  List<UserFindResponseDto> mapToUserFindResponseDto(List<User> users);

  @Mapping(expression = "java(PasswordUtil.encryptPassword(userRequest.getPassword(), passwordEncoder))",
      target = "password")
  User mapToUser(UserSaveRequestDto userRequest, PasswordEncoder passwordEncoder);

  UserSaveResponseDto mapToUserSaveResponseDto(User user);

  UserSaveRequestDto mapToUserSaveRequestDto(UserCreateRequestDto userRequest);

  @Named("getRolesRelatedToUser")
  default List<String> getRolesRelatedToUser(User user) {
    Set<UserRole> optionalUserRolesSet = Optional.of(user).map(User::getUserRoles).orElseGet(Set::of);

    return optionalUserRolesSet.stream().map(userRole -> Optional.of(userRole)
            .map(UserRole::getRole).map(Role::getName))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .distinct()
        .toList();
  }

}
