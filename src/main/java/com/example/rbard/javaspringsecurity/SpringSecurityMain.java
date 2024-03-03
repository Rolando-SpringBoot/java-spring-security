package com.example.rbard.javaspringsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@SpringBootApplication
public class SpringSecurityMain {

  public static void main(String[] args) {
    SpringApplication.run(SpringSecurityMain.class, args);
  }

}

