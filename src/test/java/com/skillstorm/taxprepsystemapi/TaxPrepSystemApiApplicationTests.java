package com.skillstorm.taxprepsystemapi;

import com.github.javafaker.App;
import com.skillstorm.taxprepsystemapi.dtos.in.RegisterDto;
import com.skillstorm.taxprepsystemapi.dtos.out.AppUserDto;
import com.skillstorm.taxprepsystemapi.models.AppUser;
import com.skillstorm.taxprepsystemapi.models.Location;
import com.skillstorm.taxprepsystemapi.repositories.AppUserRepository;
import com.skillstorm.taxprepsystemapi.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TaxPrepSystemApiApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    void registerAppUserTest() {
        try {
            RegisterDto newRegisterDto = RegisterDto.builder()
                    .firstName("Sam")
                    .lastName("Sessums")
                    .email("s@s.com")
                    .password("password")
                    .build();

            AppUserDto newUser = userService.registerUser(newRegisterDto);
            Optional<AppUser> addedUser = appUserRepository.findByEmail("s@s.com");
            assertTrue(addedUser.isPresent());

            assertTrue(new AppUserDto(addedUser.get()).equals(newUser));
        } catch (Exception e) {
        }
    }



}
