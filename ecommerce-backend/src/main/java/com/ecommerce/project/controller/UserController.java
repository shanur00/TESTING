package com.ecommerce.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {
  @RequestMapping("/profile")
  public String welcome(){
    return "Welcome To Profile (^_^)";
  }

  @RequestMapping("/user")
  public Principal user(Principal user){
    System.out.println(user.toString());
    return user;
  }
}
