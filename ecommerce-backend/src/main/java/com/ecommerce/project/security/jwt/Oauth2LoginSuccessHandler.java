package com.ecommerce.project.security.jwt;

import com.ecommerce.project.model.Users;
import com.ecommerce.project.repository.RoleRepository;
import com.ecommerce.project.security.services.UserDetailsImplementation;
import com.ecommerce.project.service.FileService;
import com.ecommerce.project.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Oauth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  @Autowired
  private final UserService userService;

  @Autowired
  private final JwtUtils jwtUtils;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  FileService fileService;

  String username;
  String idAttributeKey;

  @Value("${frontend.url}")
  private String frontendUrl;

  @Value("${project.image}")
  private String path;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws ServletException, IOException {

    OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

    if("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())){
      DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();

      Map<String, Object> attributes = principal.getAttributes();

      String email = attributes.getOrDefault("email", "").toString();
      String name = attributes.getOrDefault("name", "").toString();
      String image = attributes.getOrDefault("picture", "").toString();

      if("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())){
//        username = email.split("@")[0];
        username = name;
        idAttributeKey = "sub";
      }

      else{
        username = "";
        idAttributeKey = "id";
      }

      userService.findByEmail(email)
        .ifPresentOrElse(users -> {
          List<GrantedAuthority> authorities = users.getRolesInUsers().stream().map(
            roles -> new SimpleGrantedAuthority(roles.getRoleName().name())).collect(Collectors.toList());

          DefaultOAuth2User oAuth2User = new DefaultOAuth2User(
            authorities,
            attributes,
            idAttributeKey
          );

          Authentication securityAuth = new OAuth2AuthenticationToken(
            oAuth2User,
            authorities,
            oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()
          );

          SecurityContextHolder.getContext().setAuthentication(securityAuth);
        },

          ()-> {
            Users user = new Users();
            user.setEmail(email);
            user.setUserName(username);
            user.setSignUpMethod(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
            try {
              user.setImage(fileService.saveImage(image, path));
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
            Users savedUser = userService.registerUser(user);
            List<GrantedAuthority> authorities = savedUser.getRolesInUsers().stream().map(
              user_role -> new SimpleGrantedAuthority(user_role.getRoleName().name())
            ).collect(Collectors.toList());

            DefaultOAuth2User oAuth2User = new DefaultOAuth2User(
              authorities,
              attributes,
              idAttributeKey
            );

            Authentication securityAuth = new OAuth2AuthenticationToken(
              oAuth2User,
              authorities,
              oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()
            );
            SecurityContextHolder.getContext().setAuthentication(securityAuth);

          });

    }

    this.setAlwaysUseDefaultTargetUrl(true);

    DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
    Map<String, Object> attributes = oAuth2User.getAttributes();

    String email = (String) attributes.get("email");
    String image = (String) attributes.get("picture");
    System.out.println("OAuth2LoginSuccessHandler: " + username + " : " + email);
    System.out.println("PICTURE: ");
    System.out.println(image);
    System.out.println();

    UserDetailsImplementation userDetailsImplementation = new UserDetailsImplementation(
      null,
      username,
      email,
      null,
      oAuth2User.getAuthorities().stream()
        .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
        .collect(Collectors.toList())
    );

    String jwtToken = jwtUtils.generateTokenFromUsername(userDetailsImplementation);

    String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/profile")
      .queryParam("token", jwtToken)
      .build().toUriString();

    this.setDefaultTargetUrl(targetUrl);
    super.onAuthenticationSuccess(request, response, authentication);
  }
}
