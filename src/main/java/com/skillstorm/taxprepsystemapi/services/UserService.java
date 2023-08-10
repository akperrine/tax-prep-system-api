package com.skillstorm.taxprepsystemapi.services;

import com.skillstorm.taxprepsystemapi.dtos.in.AppUserInDto;
import com.skillstorm.taxprepsystemapi.dtos.in.RegisterDto;
import com.skillstorm.taxprepsystemapi.dtos.in.SignInDTO;
import com.skillstorm.taxprepsystemapi.dtos.out.AppUserOutDto;
import com.skillstorm.taxprepsystemapi.exceptions.*;
import com.skillstorm.taxprepsystemapi.models.AppUser;
import com.skillstorm.taxprepsystemapi.models.Location;
import com.skillstorm.taxprepsystemapi.repositories.AppUserRepository;
import com.skillstorm.taxprepsystemapi.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;



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

    public AppUserOutDto getUserById(String id) throws UserNotFoundException {
        return new AppUserOutDto(isUserExistsAndReturn(id));
    }

    @Transactional
    public AppUserOutDto registerUser(RegisterDto registerDto) throws UserExistsException, ParseException {
        Optional<AppUser> emailCheck = appUserRepository.findByEmail(registerDto.getEmail());

        if (emailCheck.isPresent()) {
            throw new UserExistsException();
        }

        AppUser newAppUser = AppUser.builder()
                .email(registerDto.getEmail())
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .password(new BCryptPasswordEncoder().encode(registerDto.getPassword()))
                .taxDocuments(new ArrayList<>())
                //.appUserInformation(new AppUserInformation())
                .build();

        return new AppUserOutDto(appUserRepository.save(newAppUser));
    }

    public AppUser getUserByEmail(String email) throws UserNotFoundException {
        Optional<AppUser> usernameCheck = appUserRepository.findByEmail(email);

        if (!usernameCheck.isPresent()) {
            throw new UserNotFoundException();
        }

        return usernameCheck.get();
    }

    // The only validation will be whether State is not 2 chars and zipcode is 5 chars
    @Transactional
    public AppUserOutDto editUserInformation(AppUserInDto appUserInDto) throws StateNotValidException, ZipcodeNotValidException, UserNotFoundException, LocationNotFoundException {

        // will throw an exception if a user is not found
        AppUser oldUser = isUserExistsAndReturn(appUserInDto.getId());


        if (!isNull(appUserInDto.getLocation())) {

            // checking state length
            if (appUserInDto.getLocation().getState().length() != 2) {
                throw new StateNotValidException();
            }

            // checking zipcode length
            if (appUserInDto.getLocation().getZipcode().length() != 5) {
                throw new ZipcodeNotValidException();
            }

            Location newLocation = Location.builder()
                    .address(appUserInDto.getLocation().getAddress())
                    .address2(appUserInDto.getLocation().getAddress2())
                    .city(appUserInDto.getLocation().getCity())
                    .state(appUserInDto.getLocation().getState())
                    .zipcode(appUserInDto.getLocation().getZipcode())
                    .build();
            oldUser.setLocation(newLocation);
        }


        // check if password was sent if it was then make sure to encode and add back to appUser
        if (!isNull(appUserInDto.getPassword())) {
            // passwords don't match, must be changing it
            if (!new BCryptPasswordEncoder().matches(appUserInDto.getPassword(), oldUser.getPassword())) {
                oldUser.setPassword(new BCryptPasswordEncoder().encode(appUserInDto.getPassword()));
            }
        }


        // set dob
        oldUser.setDob(appUserInDto.getDob());

        // setting first and last name
        oldUser.setFirstName(appUserInDto.getFirstName());
        oldUser.setLastName(appUserInDto.getLastName());


        // if the user hasn't added their ssn yet
        if (isNull(oldUser.getSsn())) {
            oldUser.setSsn(appUserInDto.getSsn());
        } else {
            // if user changed their ssn
            if (!oldUser.getSsn().equals(appUserInDto.getSsn())) {
                oldUser.setSsn(appUserInDto.getSsn());
            }
        }

        return new AppUserOutDto(appUserRepository.save(oldUser));
    }


    @Transactional
    public Boolean deleteUser(String id) throws UserNotFoundException {
        AppUser appUser = isUserExistsAndReturn(id);
        // removing location first
        locationRepository.delete(appUser.getLocation());
        appUserRepository.delete(appUser);
        return true;
    }





    /*Helper functions*/
    public AppUser isUserExistsAndReturn(String appUserId) throws UserNotFoundException {
        Optional<AppUser> userCheck = appUserRepository.findById(appUserId);

        if(!userCheck.isPresent()) {
            throw new UserNotFoundException();
        }

        return userCheck.get();
    }



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email + " not found."));
        return user;
    }
}
