package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Roles;
import com.ecommerce.project.model.Users;
import com.ecommerce.project.repository.RoleRepository;
import com.ecommerce.project.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImplementation implements UserService{

  @Autowired
  UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private RoleRepository roleRepository;

  @Override
  public Optional<Users> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  @Transactional
  public Users registerUser(Users user) {
    if(user.getPassword() != null){
      user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    Set<Roles> roles = new HashSet<>();

    Optional<Roles> roleOpt = roleRepository.findByRoleName(AppRole.ROLE_USER);

    if (roleOpt.isPresent()) {
      roles.add(roleOpt.get());  // Ensure you're using the managed instance
    } else {
      throw new APIException("Role not found");
    }

    user.setRolesInUsers(roles);
    user.setEnabled(true);

    return userRepository.save(user);
  }
}
