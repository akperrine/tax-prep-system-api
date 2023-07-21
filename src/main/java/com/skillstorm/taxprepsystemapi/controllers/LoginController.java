package com.skillstorm.taxprepsystemapi.controllers;

import com.skillstorm.taxprepsystemapi.dtos.in.SignInDTO;
import com.skillstorm.taxprepsystemapi.dtos.out.AppUserDto;
import com.skillstorm.taxprepsystemapi.exceptions.UserNotFoundException;
import com.skillstorm.taxprepsystemapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity login(@RequestBody SignInDTO signInDTO) {
        try {
            return ResponseEntity.ok().body(new AppUserDto(userService.getAuth(signInDTO)));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
