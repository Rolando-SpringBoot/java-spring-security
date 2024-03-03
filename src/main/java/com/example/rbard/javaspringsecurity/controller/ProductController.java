package com.example.rbard.javaspringsecurity.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public List<Map<String, String>> list() {
    return List.of(Collections.singletonMap("1", "water machine"));
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{id}")
  public Map<Long, String> findById(@PathVariable Long id) {
    return Collections.singletonMap(id, "water machine");
  }

  @PreAuthorize("hasRole('ADMIN')")
  @ResponseStatus(HttpStatus.ACCEPTED)
  @DeleteMapping("/{id}")
  public Map<String, String> deleteById(@PathVariable Long id) {
    return Collections.singletonMap("message", "Eliminado con éxito");
  }

}
