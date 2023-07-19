package com.skillstorm.taxprepsystemapi.services;


import com.skillstorm.taxprepsystemapi.dtos.in.RegisterDto;
import com.skillstorm.taxprepsystemapi.dtos.in.SignInDTO;
import com.skillstorm.taxprepsystemapi.exceptions.UserExistsException;
import com.skillstorm.taxprepsystemapi.exceptions.UserNotFoundException;
import com.skillstorm.taxprepsystemapi.models.AppUser;
import com.skillstorm.taxprepsystemapi.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private AppUserRepository appUserRepository;

    public Boolean getAuth(SignInDTO signInDTO) throws UserNotFoundException {
        Optional<AppUser> userCheck = appUserRepository.findByEmail(signInDTO.getEmail());
        if (userCheck.isPresent()) {
            if (userCheck.get().getPassword().equals(new BCryptPasswordEncoder().encode(signInDTO.getPassword()))) {
                return true;
            } else {
                return false;
                // an alternative to returning false
                //throw new WrongPasswordException();
            }
        } else {
            throw new UserNotFoundException();
        }
    }

    public Boolean registerUser(RegisterDto registerDto) throws UserExistsException {
        Optional<AppUser> emailCheck = appUserRepository.findByEmail(registerDto.getEmail());

        if (emailCheck.isPresent()) {
            throw new UserExistsException();
        }

        AppUser newAppUser = AppUser.builder()
                .email(registerDto.getEmail())
                .password(new BCryptPasswordEncoder().encode(registerDto.getPassword()))
                .build();

        appUserRepository.save(newAppUser);

        return true;
    }

    public AppUser getUserByEmail(String email) throws UserNotFoundException {
        Optional<AppUser> usernameCheck = appUserRepository.findByEmail(email);

        if (!usernameCheck.isPresent()) {
            throw new UserNotFoundException();
        }

        return usernameCheck.get();
    }

//    public Boolean postUserInformation() {
//
//    }
}
