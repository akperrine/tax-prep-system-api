package com.skillstorm.taxprepsystemapi.controllers;

import com.skillstorm.taxprepsystemapi.dtos.in.RegisterDto;
import com.skillstorm.taxprepsystemapi.dtos.in.SignInDTO;
import com.skillstorm.taxprepsystemapi.exceptions.TaxPayerNotFoundException;
import com.skillstorm.taxprepsystemapi.exceptions.UserNotFoundException;
import com.skillstorm.taxprepsystemapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "login")
    public ResponseEntity getAuth(@RequestBody SignInDTO signInDTO) {
        try {
            return ResponseEntity.ok().body(userService.getAuth(signInDTO));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/email/{email}")
    public ResponseEntity getUserByUsername(@PathVariable String email) {
        try {
            return ResponseEntity.ok().body(userService.getUserByEmail(email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "register")
    public ResponseEntity registerUser(@RequestBody RegisterDto registerDto) {
        try {
            return ResponseEntity.status(201).body(userService.registerUser(registerDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
