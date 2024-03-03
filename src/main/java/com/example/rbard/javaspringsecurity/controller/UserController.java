package com.example.rbard.javaspringsecurity.controller;

import com.example.rbard.javaspringsecurity.service.UserService;
import com.example.rbard.javaspringsecurity.service.dto.UserCreateRequestDto;
import com.example.rbard.javaspringsecurity.service.dto.UserFindResponseDto;
import com.example.rbard.javaspringsecurity.service.dto.UserSaveRequestDto;
import com.example.rbard.javaspringsecurity.service.dto.UserSaveResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin(originPatterns = "*")
//@CrossOrigin(origins = {"http://domain:4200"})
@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public List<UserFindResponseDto> list() {
    return this.userService.findAll();
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public UserSaveResponseDto create(@Valid @RequestBody UserSaveRequestDto request) {
    return this.userService.save(request);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/register")
  public UserSaveResponseDto register(@Valid @RequestBody UserCreateRequestDto request) {
    return this.userService.create(request);
  }

}
