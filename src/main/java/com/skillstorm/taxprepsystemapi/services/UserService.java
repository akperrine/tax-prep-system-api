package com.skillstorm.taxprepsystemapi.services;

import com.skillstorm.taxprepsystemapi.dtos.in.AppUserDto;
import com.skillstorm.taxprepsystemapi.dtos.in.RegisterDto;
import com.skillstorm.taxprepsystemapi.dtos.in.SignInDTO;
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
import java.math.BigInteger;
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

    public com.skillstorm.taxprepsystemapi.dtos.out.AppUserDto getUserById(BigInteger id) throws UserNotFoundException {
        return new com.skillstorm.taxprepsystemapi.dtos.out.AppUserDto(isUserExistsAndReturn(id));
    }

    @Transactional
    public com.skillstorm.taxprepsystemapi.dtos.out.AppUserDto registerUser(RegisterDto registerDto) throws UserExistsException, ParseException {
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

        return new com.skillstorm.taxprepsystemapi.dtos.out.AppUserDto(appUserRepository.save(newAppUser));
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
    public com.skillstorm.taxprepsystemapi.dtos.out.AppUserDto editUserInformation(AppUserDto appUserDto) throws StateNotValidException, ZipcodeNotValidException, UserNotFoundException, LocationNotFoundException {

        // will throw an exception if a user is not found
        AppUser oldUser = isUserExistsAndReturn(new BigInteger(appUserDto.getId()));

        // checking state length
        if(appUserDto.getLocation().getState().length() != 2) {
            throw new StateNotValidException();
        }

        // checking zipcode length
        if(appUserDto.getLocation().getZipcode().length() != 5) {
            throw new ZipcodeNotValidException();
        }

        // checking if this the first time a user has provided a location
        if(isNull(appUserDto.getLocation().getId())) {

            Location newLocation = Location.builder()
                    .address(appUserDto.getLocation().getAddress())
                    .address2(appUserDto.getLocation().getAddress2())
                    .city(appUserDto.getLocation().getCity())
                    .state(appUserDto.getLocation().getState())
                    .zipcode(appUserDto.getLocation().getZipcode())
                    .build();
            Location updatedNewLocation = locationRepository.save(newLocation);
            oldUser.setLocation(updatedNewLocation);
        } else {

            // location has an id attached meaning it isn't new
            Location location = isLocationExistsAndReturn(appUserDto.getLocation());
            Location updatedLocation = locationRepository.save(appUserDto.getLocation());
            oldUser.setLocation(updatedLocation);
        }


        // check if password was sent if it was then make sure to encode and add back to appUser
        if(!isNull(appUserDto.getPassword())) {
            // passwords don't match, must be changing it
            if(!new BCryptPasswordEncoder().matches(appUserDto.getPassword(), oldUser.getPassword())) {
                oldUser.setPassword(new BCryptPasswordEncoder().encode(appUserDto.getPassword()));
            };
        }


        // set dob
        oldUser.setDob(appUserDto.getDob());

        // setting first and last name
        oldUser.setFirstName(appUserDto.getFirstName());
        oldUser.setLastName(appUserDto.getLastName());


        // if the user hasn't added their ssn yet
        if(isNull(oldUser.getSsn())) {
            oldUser.setSsn(appUserDto.getSsn());
        } else {
            // if user changed their ssn
            if(!oldUser.getSsn().equals(appUserDto.getSsn())) {
                oldUser.setSsn(appUserDto.getSsn());
            }
        }

        return new com.skillstorm.taxprepsystemapi.dtos.out.AppUserDto(appUserRepository.save(oldUser));
    }


    @Transactional
    public Boolean deleteUser(BigInteger id) throws UserNotFoundException {
        AppUser appUser = isUserExistsAndReturn(id);
        // removing location first
        locationRepository.delete(appUser.getLocation());
        appUserRepository.delete(appUser);
        return true;
    }





    /*Helper functions*/
    public AppUser isUserExistsAndReturn(BigInteger appUserId) throws UserNotFoundException {
        Optional<AppUser> userCheck = appUserRepository.findById(appUserId);

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


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email + " not found."));
        return user;
    }
}
