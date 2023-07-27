package com.skillstorm.taxprepsystemapi;

import com.github.javafaker.App;
import com.skillstorm.taxprepsystemapi.dtos.in.RegisterDto;
import com.skillstorm.taxprepsystemapi.dtos.out.AppUserDto;
import com.skillstorm.taxprepsystemapi.models.AppUser;
import com.skillstorm.taxprepsystemapi.models.Location;
import com.skillstorm.taxprepsystemapi.repositories.AppUserRepository;
import com.skillstorm.taxprepsystemapi.services.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TaxPrepSystemApiApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private AppUserRepository appUserRepository;

    RegisterDto newRegisterDto = RegisterDto.builder()
            .firstName("Sam")
            .lastName("Sessums")
            .email("s@s.com")
            .password("password")
            .build();

    @Test
    void AppUserTest() {
        try {
            // Post Test
            AppUserDto newUser = userService.registerUser(newRegisterDto);
            Optional<AppUser> addedUser = appUserRepository.findByEmail("s@s.com");
            AppUserDto addedUserDto = new AppUserDto(addedUser.get());
            assertEquals(addedUserDto.toString(), newUser.toString());

            com.skillstorm.taxprepsystemapi.dtos.in.AppUserDto updateUserDto = com.skillstorm.taxprepsystemapi.dtos.in.AppUserDto.makeAppUserDto(addedUser.get());
            updateUserDto.setEmail("kdsjflksjdf");
            updateUserDto.setSsn("123456789");
            updateUserDto.setDob(new Date());
            updateUserDto.setLocation(Location.builder()
                    .address("1234 Fake St")
                    .address2("apt #343")
                    .city("portland")
                    .state("ME")
                    .zipcode("04101")
                    .build()
            );

            AppUserDto updatedAppUser = userService.editUserInformation(updateUserDto);
            // getting id from updated user location to compare to updateUserDto
            Location addedLocation = updateUserDto.getLocation();
            addedLocation.setId(updatedAppUser.getLocation().getId());
            updateUserDto.setLocation(addedLocation);
            //assertEquals(updatedAppUser.toString(), updateUserDto.toString());

        } catch (Exception e) {

        }
    }

    @AfterAll
    public static void sleeping() {
        while(true) {

        }
    }




}
