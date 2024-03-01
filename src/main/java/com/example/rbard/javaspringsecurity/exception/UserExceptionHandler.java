package com.example.rbard.javaspringsecurity.exception;

import com.example.rbard.javaspringsecurity.controller.UserController;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice(basePackageClasses = UserController.class)
public class UserExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  protected ResponseEntity<Object> handleMethodRuntimeException(RuntimeException ex,
      WebRequest request) {
    String defaultDetail = ex.getMessage();
    ProblemDetail body = createProblemDetail(ex, HttpStatus.INTERNAL_SERVER_ERROR, defaultDetail,
        null, null, request);
    log.atError().setMessage("{} : {}")
        .addArgument(ex.getClass().getSimpleName()).addArgument(ex.getMessage()).log();
    return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
        request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
    BindingResult bindingResult = ex.getBindingResult();

    String defaultDetail = "";
    if (bindingResult.hasFieldErrors()) {
      defaultDetail = bindingResult.getFieldErrors().stream()
          .map(DefaultMessageSourceResolvable::getDefaultMessage)
          .distinct()
          .collect(Collectors.joining(","));
    }
    ProblemDetail body = createProblemDetail(ex, status, defaultDetail,
        null, null, request);
    log.atError().setMessage("{} : {}")
        .addArgument(ex.getClass().getSimpleName()).addArgument(ex.getMessage()).log();
    return handleExceptionInternal(ex, body, headers, status, request);
  }

}
