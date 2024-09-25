package com.ecommerce.project.service;

import com.ecommerce.project.model.Users;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
  Optional<Users> findByEmail(String email);

  Users registerUser(Users user);
}
