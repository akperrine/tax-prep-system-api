package com.skillstorm.taxprepsystemapi.controllers;

import com.skillstorm.taxprepsystemapi.dtos.in.SignInDTO;
import com.skillstorm.taxprepsystemapi.dtos.out.AppUserDto;
import com.skillstorm.taxprepsystemapi.exceptions.UserNotFoundException;
import com.skillstorm.taxprepsystemapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Map;

@Controller
@CrossOrigin
public class LoginController {

    @Autowired(required = true)
    private OAuth2AuthorizedClientService clientService;

    @Autowired
    private UserService userService;


    /*@PostMapping("/login")
    @ResponseBody
    public ResponseEntity login(@RequestBody SignInDTO signInDTO) {
        try {
            return ResponseEntity.ok().body(new AppUserDto(userService.getAuth(signInDTO)));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }*/

    @GetMapping("/login")
    @ResponseBody
    public ResponseEntity login(Principal principal, Authentication auth) {
        try {
            return ResponseEntity.ok().body(principal);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/userinfo")
    @ResponseBody
    public Map<String, Object> userInfo(@AuthenticationPrincipal OAuth2User user) {
        return user.getAttributes();
    }

    @GetMapping("/accessToken")
    @ResponseBody
    public String accessToken(Authentication auth, HttpServletResponse response) {

        if(auth instanceof OAuth2AuthenticationToken) {

            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) auth;

            OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(), authToken.getName());


            Cookie cookie = new Cookie("Access Token", client.getAccessToken().getTokenValue());
            cookie.isHttpOnly();
            response.addCookie(cookie);

            return client.getAccessToken().getTokenValue();
        }

        return "";
    }

    @GetMapping("/signin")
    public RedirectView redirectView() {
        return new RedirectView("http://localhost:5173");
    }
}
