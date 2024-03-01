package com.example.rbard.javaspringsecurity.configuration;

import com.example.rbard.javaspringsecurity.service.UserService;
import com.example.rbard.javaspringsecurity.util.annotation.ExistsByUsername;
import java.lang.reflect.Field;
import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserAspect {

  @Autowired
  private UserService userService;

  @Pointcut("within(com.example.rbard.javaspringsecurity.controller..*)")
  private void anyMethodInControllerPackage() {
  }

  @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
  private void onlyRestControllerClasses() {
  }

  @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
  private void anyMethodWithPostMappingAnnotation() {
  }

  @Before("anyMethodInControllerPackage() && onlyRestControllerClasses() && "
      + "anyMethodWithPostMappingAnnotation()")
  public void adviceMethodExecutionAndValidUsernameWithExistsByUsernameAnnotation(
      JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    Arrays.stream(args).forEach(propInstance -> {
      Field[] fields = propInstance.getClass().getDeclaredFields();
      this.validFieldWithExistsByUsernameAnnotation(fields, propInstance);
    });
  }

  private void validFieldWithExistsByUsernameAnnotation(Field[] fields, Object propInstance) {
    Arrays.stream(fields).forEach(field -> {
      if (field.isAnnotationPresent(ExistsByUsername.class)) {
        try {
          field.setAccessible(true);
          Object value = field.get(propInstance);
          if (value instanceof String username) {
            boolean result = this.userService.existsByUsername(username);
            if (result) {
              String errorMessage = field.getDeclaredAnnotation(ExistsByUsername.class).message();
              throw new RuntimeException(errorMessage);
            }
          }
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      } else {
        Class<?> fieldType = field.getType();
        if(!fieldType.isPrimitive() && !fieldType.isArray()) {
          this.validFieldWithExistsByUsernameAnnotation(field.getClass().getDeclaredFields(), field);
        }
      }
    });
  }

}
