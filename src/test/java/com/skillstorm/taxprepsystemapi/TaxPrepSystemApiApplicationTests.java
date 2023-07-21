package com.skillstorm.taxprepsystemapi;

import com.skillstorm.taxprepsystemapi.dtos.in.RegisterDto;
import com.skillstorm.taxprepsystemapi.dtos.out.AppUserDto;
import com.skillstorm.taxprepsystemapi.models.AppUser;
import com.skillstorm.taxprepsystemapi.models.Location;
import com.skillstorm.taxprepsystemapi.repositories.AppUserRepository;
import com.skillstorm.taxprepsystemapi.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TaxPrepSystemApiApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    void contextLoads() {
    }


    @Test
    void registerAppUserTest() {
        try {
            Location newLocation = Location.builder()
                    .address("1234 Fake Street")
                    .state("ME")
                    .city("Portland")
                    .zipcode("04101")
                    .build();

            RegisterDto newRegisterDto = RegisterDto.builder()
                    .firstName("Sam")
                    .lastName("Sessums")
                    .email("s@s.com")
                    .dob("03-17-1993")
                    .location(newLocation)
                    .ssn("4728364872634")
                    .password("password")
                    .build();

            AppUserDto newUser = userService.registerUser(newRegisterDto);
            assertTrue(appUserRepository.findByEmail("s@s.com").isPresent());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
