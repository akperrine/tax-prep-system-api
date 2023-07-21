package com.skillstorm.taxprepsystemapi.helpers;

import com.skillstorm.taxprepsystemapi.exceptions.LocationNotFoundException;
import com.skillstorm.taxprepsystemapi.exceptions.UserNotFoundException;
import com.skillstorm.taxprepsystemapi.models.AppUser;
import com.skillstorm.taxprepsystemapi.models.Location;
import com.skillstorm.taxprepsystemapi.repositories.AppUserRepository;
import com.skillstorm.taxprepsystemapi.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

public class UserHelpers {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    LocationRepository locationRepository;

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

        return locationCheck.get();
    }
}
