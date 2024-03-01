package com.example.rbard.javaspringsecurity.service.impl;

import com.example.rbard.javaspringsecurity.entity.Role;
import com.example.rbard.javaspringsecurity.entity.User;
import com.example.rbard.javaspringsecurity.entity.UserRole;
import com.example.rbard.javaspringsecurity.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Transactional(readOnly = true)
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userOpt = this.userRepository.findByUsername(username);
    if(userOpt.isEmpty()) {
      throw new UsernameNotFoundException(STR."Username \{username} doesn't exist on the system");
    }

    User user = userOpt.get();
    List<GrantedAuthority> authorities = user.getUserRoles().stream()
        .map(Optional::ofNullable)
        .map(userRole -> userRole.map(UserRole::getRole).map(Role::getName))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(), user.getPassword(), user.getEnabled(),
        true, true, true,
        authorities);
  }

}
