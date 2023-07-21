package com.skillstorm.taxprepsystemapi.controllers;

import com.skillstorm.taxprepsystemapi.dtos.in.AppUserDto;
import com.skillstorm.taxprepsystemapi.dtos.in.RegisterDto;
import com.skillstorm.taxprepsystemapi.services.TaxService;
import com.skillstorm.taxprepsystemapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private TaxService taxService;

    @PostMapping(value = "register")
    public ResponseEntity registerUser(@RequestBody RegisterDto registerDto) {
        try {
            return ResponseEntity.status(201).body(userService.registerUser(registerDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/id/{id}")
    public ResponseEntity getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(userService.getUserById(id));
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

    @PutMapping(value = "/id/{id}")
    public ResponseEntity editUserInformation(@PathVariable Long id, @RequestBody AppUserDto appUserDto) {
        try {
            return ResponseEntity.ok().body(userService.editUserInformation(appUserDto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity deleteUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(userService.deleteUser(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // @PostMapping(value = "/user/{id}/documents")
    @GetMapping(value = "/user/{id}/documents")
    public ResponseEntity getUserTaxDocuments(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(taxService.getTaxDocumentsByUserId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // @GetMapping(value = "/user/{id}/document/id/{id}")
    // @PutMapping(value = "/user/{id}/document/id/{id}")
    // @DeleteMapping(value = "/user/{id}/documents")
    // @DeleteMapping(value = "/user/{id}/document/{id}")


}
