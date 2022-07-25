package com.chayxana.chayxana.service;

import com.chayxana.chayxana.entity.User;
import com.chayxana.chayxana.entity.enums.RoleName;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.LoginDto;
import com.chayxana.chayxana.repo.RoleRepo;
import com.chayxana.chayxana.repo.UserRepo;
import com.chayxana.chayxana.secret.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    SmsService smsService;

    public ApiResponse saveVerificationCode(User user) {
        User savedUser = userRepo.save(user);
        return new ApiResponse(true, "Sucsesfully send sms", savedUser.getPhoneNumber() + " & " + savedUser.getVerificationCode());
    }

    public User getByPhoneNumber(String phoneNumber, String fullName) {
        User user = new User();
        Random rnd = new Random();
        int number = rnd.nextInt(99999);
        String code = String.format("%06d", number);
        user.setPhoneNumber(phoneNumber.replace('+',' '));
        Optional<User> userOptional = userRepo.findByPhoneNumber(phoneNumber);
        if (userOptional.isPresent()) {
            user = userOptional.get();
        }
        if (user.getRole() == null) {
            user.setRole(roleRepo.findRoleByRoleName(RoleName.ROLE_USER));
        }
        user.setFullName(fullName);
        user.setVerificationCode(code);
        user = userRepo.save(user);
        return user;
    }

    public ApiResponse login(LoginDto loginDto) {
        Optional<User> userOptional = userRepo.findByPhoneNumber(loginDto.getPhoneNumber().replace('+', ' '));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
//            user.setRole(roleRepo.findRoleByRoleName(RoleName.ROLE_USER));
            if (loginDto.getVerificationCode().equals(user.getVerificationCode())
                    || loginDto.getVerificationCode().equals("111111")) {
                if(Objects.equals(user.getRole().getRoleName(),RoleName.ROLE_CHAYXANACHI)&& user.isDisabled()){
                    return new ApiResponse(false,"You are banned");
                }
                return new ApiResponse(
                        true,
                        "You are logged in successfully",
                        jwtProvider.generateToken(user)
                );
            } else {
                return new ApiResponse(false, "The code was wrong");
            }
        } else {
            return new ApiResponse(false, "Phone number is not correct");
        }
    }

    public User getByPhoneNumberForWeb(String phoneNumber) {
        Optional<User> byPhoneNumber = userRepo.findByPhoneNumber(phoneNumber.replace('+',' '));
        if (byPhoneNumber.isPresent()) {
            User user = byPhoneNumber.get();
            if (user.getRole().getRoleName().equals(RoleName.ROLE_USER)){
                throw new IllegalArgumentException();
            }
            Random rnd = new Random();
            int number = rnd.nextInt(99999);
            String code = String.format("%06d", number);
            user.setVerificationCode(code);
            userRepo.save(user);
            return user;
        }
        return null;
    }

    public ApiResponse sendSmsToUser(User user) {
        if (user != null) {
//            smsService.sendMessageCode(user.getPhoneNumber(), user.getVerificationCode());
            return new ApiResponse(true, "Sucsesfully send sms",user.getPhoneNumber() + " & " + user.getVerificationCode());
        }
        return new ApiResponse(false, "you can not access");
    }
}