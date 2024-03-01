package com.example.rbard.javaspringsecurity.service.impl;

import com.example.rbard.javaspringsecurity.entity.Role;
import com.example.rbard.javaspringsecurity.entity.User;
import com.example.rbard.javaspringsecurity.repository.RoleRepository;
import com.example.rbard.javaspringsecurity.repository.UserRepository;
import com.example.rbard.javaspringsecurity.service.UserService;
import com.example.rbard.javaspringsecurity.service.dto.UserCreateRequestDto;
import com.example.rbard.javaspringsecurity.service.dto.UserFindResponseDto;
import com.example.rbard.javaspringsecurity.service.dto.UserSaveRequestDto;
import com.example.rbard.javaspringsecurity.service.dto.UserSaveResponseDto;
import com.example.rbard.javaspringsecurity.service.mapper.UserMapper;
import com.example.rbard.javaspringsecurity.util.constant.RoleEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private UserMapper userMapper;
  @Autowired
  PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  @Override
  public List<UserFindResponseDto> findAll() {
    List<User> users = (List<User>) this.userRepository.findAll();
    return this.userMapper.mapToUserFindResponseDto(users);
  }

  @Transactional
  @Override
  public UserSaveResponseDto save(UserSaveRequestDto userRequest) {

    Optional<Role> optionalRole = this.roleRepository.findByName(RoleEnum.ROLE_USER.name());
    List<Role> roles = new ArrayList<>();
    optionalRole.ifPresent(roles::add);

    Boolean admin = Optional.ofNullable(userRequest.getAdmin()).orElse(Boolean.FALSE);
    if (admin) {
      Optional<Role> optionalRoleAdmin = this.roleRepository.findByName(RoleEnum.ROLE_ADMIN.name());
      optionalRoleAdmin.ifPresent(roles::add);
    }

    User userToSave = this.userMapper.mapToUser(userRequest, passwordEncoder);
    userToSave.setRoles(roles);
    User userCreated = this.userRepository.saveAndFlush(userToSave);
    return this.userMapper.mapToUserSaveResponseDto(userCreated);
  }

  @Override
  public UserSaveResponseDto create(UserCreateRequestDto userRequest) {
    UserSaveRequestDto userSaveRequestDto = this.userMapper.mapToUserSaveRequestDto(userRequest);
    userSaveRequestDto.setAdmin(Boolean.FALSE);
    return this.save(userSaveRequestDto);
  }

  @Override
  public boolean existsByUsername(String username) {
    return this.userRepository.existsByUsername(username);
  }

}
