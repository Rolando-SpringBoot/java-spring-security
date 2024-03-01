package com.example.rbard.javaspringsecurity.service;

import com.example.rbard.javaspringsecurity.service.dto.UserCreateRequestDto;
import com.example.rbard.javaspringsecurity.service.dto.UserFindResponseDto;
import com.example.rbard.javaspringsecurity.service.dto.UserSaveRequestDto;
import com.example.rbard.javaspringsecurity.service.dto.UserSaveResponseDto;
import java.util.List;

public interface UserService {

  List<UserFindResponseDto> findAll();
  UserSaveResponseDto save(UserSaveRequestDto userRequest);
  UserSaveResponseDto create(UserCreateRequestDto userRequest);
  boolean existsByUsername(String username);

}
