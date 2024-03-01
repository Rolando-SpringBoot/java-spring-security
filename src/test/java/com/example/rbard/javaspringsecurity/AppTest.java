package com.example.rbard.javaspringsecurity;

import java.time.Instant;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class AppTest {

  @Test
  public void test() {
    System.out.println(Date.from(Instant.now()));
  }

}

