package com.skillstorm.taxprepsystemapi.services;

import com.github.javafaker.App;
import com.skillstorm.taxprepsystemapi.dtos.in.RegisterDto;
import com.skillstorm.taxprepsystemapi.dtos.in.SignInDTO;
import com.skillstorm.taxprepsystemapi.dtos.out.AppUserDto;
import com.skillstorm.taxprepsystemapi.exceptions.*;
import com.skillstorm.taxprepsystemapi.helpers.UserHelpers;
import com.skillstorm.taxprepsystemapi.models.AppUser;
import com.skillstorm.taxprepsystemapi.models.AppUserInformation;
import com.skillstorm.taxprepsystemapi.models.Location;
import com.skillstorm.taxprepsystemapi.models.TaxDocument;
import com.skillstorm.taxprepsystemapi.repositories.AppUserInformationRepository;
import com.skillstorm.taxprepsystemapi.repositories.AppUserRepository;
import com.skillstorm.taxprepsystemapi.repositories.LocationRepository;
import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class UserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserInformationRepository appUserInformationRepository;

    @Autowired
    private LocationRepository locationRepository;

    public AppUser getAuth(SignInDTO signInDTO) throws UserNotFoundException, WrongPasswordException {
        Optional<AppUser> userCheck = appUserRepository.findByEmail(signInDTO.getEmail());
        if (userCheck.isPresent()) {
            if (new BCryptPasswordEncoder().matches(signInDTO.getPassword(), userCheck.get().getPassword())) {
                AppUser user = userCheck.get();
                user.setPassword(null);
                return user;
            } else {
                throw new WrongPasswordException();
            }
        } else {
            throw new UserNotFoundException();
        }
    }

    public AppUser getUserById(Long id) throws UserNotFoundException {
        return isUserExistsAndReturn(AppUser.builder().id(id).build());
    }

    public AppUserDto registerUser(RegisterDto registerDto) throws UserExistsException, ParseException {
        Optional<AppUser> emailCheck = appUserRepository.findByEmail(registerDto.getEmail());

        if (emailCheck.isPresent()) {
            throw new UserExistsException();
        }

        AppUser newAppUser = AppUser.builder()
                .email(registerDto.getEmail())
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .password(new BCryptPasswordEncoder().encode(registerDto.getPassword()))
                .build();

        return new AppUserDto(appUserRepository.save(newAppUser));
    }

    public AppUser getUserByEmail(String email) throws UserNotFoundException {
        Optional<AppUser> usernameCheck = appUserRepository.findByEmail(email);

        if (!usernameCheck.isPresent()) {
            throw new UserNotFoundException();
        }

        return usernameCheck.get();
    }

    // The only validation will be whether State is not 2 chars and zipcode is 5 chars
    public AppUserDto editUserInformation(AppUser appUser) throws StateNotValidException, ZipcodeNotValidException, UserNotFoundException, LocationNotFoundException {

        // will throw an exception if a user is not found
        AppUser oldUser = isUserExistsAndReturn(appUser);

        // checking state length
        if(appUser.getLocation().getState().length() != 2) {
            throw new StateNotValidException();
        }

        // checking zipcode length
        if(appUser.getLocation().getZipcode().length() != 5) {
            throw new ZipcodeNotValidException();
        }

        // checking if this the first time a user has provided a location
        if(isNull(appUser.getLocation().getId())) {
            Location newLocation = Location.builder()
                    .address(appUser.getLocation().getAddress())
                    .address2(appUser.getLocation().getAddress2())
                    .city(appUser.getLocation().getCity())
                    .state(appUser.getLocation().getState())
                    .zipcode(appUser.getLocation().getZipcode())
                    .build();
            Location updatedNewLocation = locationRepository.save(newLocation);
            appUser.setLocation(updatedNewLocation);
        } else {

            // location has an id attached meaning it isn't new
            Location location = isLocationExistsAndReturn(appUser.getLocation());
            Location updatedLocation = locationRepository.save(appUser.getLocation());
            appUser.setLocation(updatedLocation);
        }


        // check if password was sent if it was then make sure to encode and add back to appUser
        if(!isNull(appUser.getPassword())) {
            // passwords don't match, must be changing it
            if(!new BCryptPasswordEncoder().matches(appUser.getPassword(), oldUser.getPassword())) {
                appUser.setPassword(new BCryptPasswordEncoder().encode(appUser.getPassword()));
            };
        } else {
            // password is sent over as plaintext, if they aren't updating it then rather than encoding just use old hash
            appUser.setPassword(oldUser.getPassword());
        }

        // is this the first time a SSN is being put in a user
        // TODO

        // if user changed their ssn, gather any documents before deleting AppUserInformation
        // because AppUserInformation has a PK of ssn
        if(!oldUser.getAppUserInformation().getSsn().equals(appUser.getAppUserInformation().getSsn())) {
            List<TaxDocument> taxDocuments = oldUser.getAppUserInformation().getTaxDocuments();
            appUserInformationRepository.delete(oldUser.getAppUserInformation());
            AppUserInformation newAppUserInformation = AppUserInformation.builder()
                    .ssn(appUser.getAppUserInformation().getSsn())
                    .taxDocuments(taxDocuments)
                    .build();

            appUser.setAppUserInformation(newAppUserInformation);

            return new AppUserDto(appUserRepository.save(appUser));
        } else {
            // ssn is not changing so we can update appUser
            return new AppUserDto(appUserRepository.save(appUser));
        }

    }


    public Boolean deleteUser(Long id) throws UserNotFoundException {
        AppUser appUser = isUserExistsAndReturn(AppUser.builder().id(id).build());
        // removing location first
        locationRepository.delete(appUser.getLocation());
        appUserRepository.delete(appUser);
        return true;
    }


    /*Helper functions*/
    public AppUser isUserExistsAndReturn(AppUser appUser) throws UserNotFoundException {
        System.out.println(appUser);
        Optional<AppUser> userCheck = appUserRepository.findById(appUser.getId());

        if(!userCheck.isPresent()) {
            throw new UserNotFoundException();
        }

        return userCheck.get();
    }

    public Location isLocationExistsAndReturn(Location location) throws LocationNotFoundException {
        Optional<Location> locationCheck = locationRepository.findById(location.getId());

        if(!locationCheck.isPresent()) {
            throw new LocationNotFoundException();
        }

        return location;
    }


}
