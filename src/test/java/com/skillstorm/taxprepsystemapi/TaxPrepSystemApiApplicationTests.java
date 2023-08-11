package com.skillstorm.taxprepsystemapi;

import com.github.javafaker.App;
import com.skillstorm.taxprepsystemapi.controllers.TaxController;
import com.skillstorm.taxprepsystemapi.dtos.in.AppUserInDto;
import com.skillstorm.taxprepsystemapi.dtos.in.RegisterDto;
import com.skillstorm.taxprepsystemapi.dtos.in.TaxDocumentDto;
import com.skillstorm.taxprepsystemapi.dtos.out.AppUserOutDto;
import com.skillstorm.taxprepsystemapi.enums.FilingStatus;
import com.skillstorm.taxprepsystemapi.enums.MaritalStatus;
import com.skillstorm.taxprepsystemapi.exceptions.*;
import com.skillstorm.taxprepsystemapi.models.*;
import com.skillstorm.taxprepsystemapi.repositories.AppUserRepository;
import com.skillstorm.taxprepsystemapi.services.TaxService;
import com.skillstorm.taxprepsystemapi.services.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// It's important to note that tests use a test database
@SpringBootTest
class TaxPrepSystemApiApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private AppUserRepository appUserRepository;


    @Autowired
    private TaxService taxService;

    @Autowired
    private TaxController taxController;

    Location jobLocation = Location.builder()
            .address("1234 Job Ave")
            .state("ME")
            .city("Portland")
            .zipcode("04102")
            .build();

    TaxDocument newTaxDocument = TaxDocument.builder()
            .form1099s(Arrays.asList(
                    Form1099.builder()
                        .income(10000.00d)
                        .location(jobLocation)
                        .build()
                )
            )
            .formW2s(Arrays.asList(
                    FormW2.builder()
                            .income(10000.00d)
                            .location(jobLocation)
                            .build()
                )
            )
            .build();

    @Test
    public void createUserTest() throws UserExistsException, ParseException {
        RegisterDto newRegisterDto = RegisterDto.builder()
                .firstName("Sam")
                .lastName("Sessums")
                .email("s@s.com")
                .password("password")
                .build();

        AppUserOutDto appUserOutDto = userService.registerUser(newRegisterDto);
        assertEquals(newRegisterDto.getFirstName(), appUserOutDto.getFirstName());
        assertEquals(newRegisterDto.getLastName(), appUserOutDto.getLastName());
        assertEquals(newRegisterDto.getEmail(), appUserOutDto.getEmail());
    }

    @Test
    public void getUserTest() throws UserExistsException, ParseException, UserNotFoundException {
        RegisterDto newRegisterDto = RegisterDto.builder()
                .firstName("Sam")
                .lastName("Sessums")
                .email("s@1.com")
                .password("password")
                .build();

        AppUserOutDto appUserOutDto = userService.registerUser(newRegisterDto);
        assertEquals(newRegisterDto.getFirstName(), appUserOutDto.getFirstName());
        assertEquals(newRegisterDto.getLastName(), appUserOutDto.getLastName());
        assertEquals(newRegisterDto.getEmail(), appUserOutDto.getEmail());

        AppUserOutDto userById = userService.getUserById(appUserOutDto.getId());

        assertEquals(userById, appUserOutDto);

    }


    @Test
    public void updateUserTest() throws UserExistsException, ParseException, UserNotFoundException, StateNotValidException, ZipcodeNotValidException, LocationNotFoundException {
        RegisterDto newRegisterDto = RegisterDto.builder()
                .firstName("Sam")
                .lastName("Sessums")
                .email("s@2.com")
                .password("password")
                .build();

        AppUserOutDto appUserOutDto = userService.registerUser(newRegisterDto);
        assertEquals(newRegisterDto.getFirstName(), appUserOutDto.getFirstName());
        assertEquals(newRegisterDto.getLastName(), appUserOutDto.getLastName());
        assertEquals(newRegisterDto.getEmail(), appUserOutDto.getEmail());

        Location newLocation = Location.builder()
                .address("1234 Fake Street")
                .state("ME")
                .city("Portland")
                .zipcode("04101")
                .build();

        Date dob = new Date();
        AppUserInDto appUserInDto = AppUserInDto.builder()
                .id(appUserOutDto.getId())
                .firstName("Larry")
                .lastName("Lobster")
                .email("l@l.com")
                .ssn("111111111")
                .dob(dob)
                .location(newLocation)
                .password("password1")
                .build();

        AppUserOutDto appUserOutDto1 = userService.editUserInformation(appUserInDto);

        assertEquals(appUserInDto.getId(), appUserOutDto1.getId());
        assertEquals(appUserInDto.getFirstName(), appUserOutDto1.getFirstName());
        assertEquals(appUserInDto.getLastName(), appUserOutDto1.getLastName());
        assertEquals(appUserInDto.getEmail(), appUserOutDto1.getEmail());
        assertEquals(appUserInDto.getSsn(), appUserOutDto1.getSsn());
        assertEquals(appUserInDto.getSsn(), appUserOutDto1.getDob());
        assertEquals(appUserInDto.getLocation(), appUserOutDto1.getLocation());
    }

    @Test
    public void deleteUserTest() throws UserExistsException, ParseException, UserNotFoundException {
        RegisterDto newRegisterDto = RegisterDto.builder()
                .firstName("Sam")
                .lastName("Sessums")
                .email("s@3.com")
                .password("password")
                .build();

        AppUserOutDto appUserOutDto = userService.registerUser(newRegisterDto);
        assertEquals(newRegisterDto.getFirstName(), appUserOutDto.getFirstName());
        assertEquals(newRegisterDto.getLastName(), appUserOutDto.getLastName());
        assertEquals(newRegisterDto.getEmail(), appUserOutDto.getEmail());

        userService.deleteUser(appUserOutDto.getId());

        assertThrows(UserNotFoundException.class, () -> {
           userService.getUserByEmail(newRegisterDto.getEmail());
        });

    }

    @Test
    public void addTaxDocuments() throws UserExistsException, ParseException, UserNotFoundException, NegativeIncomeException {
        RegisterDto newRegisterDto = RegisterDto.builder()
                .firstName("Sam")
                .lastName("Sessums")
                .email("s@4.com")
                .password("password")
                .build();

        AppUserOutDto appUserOutDto = userService.registerUser(newRegisterDto);
        assertEquals(newRegisterDto.getFirstName(), appUserOutDto.getFirstName());
        assertEquals(newRegisterDto.getLastName(), appUserOutDto.getLastName());
        assertEquals(newRegisterDto.getEmail(), appUserOutDto.getEmail());

        TaxDocumentDto taxDocumentDto = TaxDocumentDto.builder()
                .userId(appUserOutDto.getId())
                .maritalStatus(MaritalStatus.SINGLE)
                .form1099s(newTaxDocument.getForm1099s())
                .formW2s(newTaxDocument.getFormW2s())
                .build();

        TaxDocument taxDocument = taxService.addTaxDocument(taxDocumentDto);

        assertEquals(taxDocument.getForm1099s(), taxDocumentDto.getForm1099s());
        assertEquals(taxDocument.getFormW2s(), taxDocumentDto.getFormW2s());
        assertEquals(taxDocument.getMaritalStatus(), taxDocumentDto.getMaritalStatus());
    }

    @Test
    public void calculateTaxDocuments() throws UserExistsException, ParseException, UserNotFoundException, NegativeIncomeException {
        RegisterDto newRegisterDto = RegisterDto.builder()
                .firstName("Sam")
                .lastName("Sessums")
                .email("s@5.com")
                .password("password")
                .build();

        AppUserOutDto appUserOutDto = userService.registerUser(newRegisterDto);
        assertEquals(newRegisterDto.getFirstName(), appUserOutDto.getFirstName());
        assertEquals(newRegisterDto.getLastName(), appUserOutDto.getLastName());
        assertEquals(newRegisterDto.getEmail(), appUserOutDto.getEmail());

        TaxDocumentDto taxDocumentDto = TaxDocumentDto.builder()
                .userId(appUserOutDto.getId())
                .maritalStatus(MaritalStatus.SINGLE)
                .form1099s(newTaxDocument.getForm1099s())
                .formW2s(newTaxDocument.getFormW2s())
                .build();

        TaxDocument taxDocument = taxService.addTaxDocument(taxDocumentDto);

        assertEquals(taxDocument.getForm1099s(), taxDocumentDto.getForm1099s());
        assertEquals(taxDocument.getFormW2s(), taxDocumentDto.getFormW2s());
        assertEquals(taxDocument.getMaritalStatus(), taxDocumentDto.getMaritalStatus());

        Double excess = 20000 - 11000d;
        Double taxedTotalIncome = 1100d + (.12d * excess);
        Double taxedIncome = taxService.calculateUserTaxes(appUserOutDto.getId());

        assertEquals(taxedIncome, taxedTotalIncome);
    }
}