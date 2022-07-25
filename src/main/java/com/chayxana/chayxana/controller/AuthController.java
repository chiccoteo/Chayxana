package com.chayxana.chayxana.controller;

import com.chayxana.chayxana.entity.User;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.LoginDto;
import com.chayxana.chayxana.secret.CurrentUser;
import com.chayxana.chayxana.service.AuthService;
import com.chayxana.chayxana.service.MapperDTO;
import com.chayxana.chayxana.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(maxAge = 36000)
public class AuthController {
    @Autowired
    MapperDTO mapperDTO;

    @Autowired
    SmsService smsService;

    @Autowired
    AuthService authService;



    @GetMapping("/mobile/free/sendCode")
    public HttpEntity<?> sendCode(@RequestParam String phoneNumber, @RequestParam(required = false) String fullName){
        User user=authService.getByPhoneNumber(phoneNumber, fullName);
//        smsService.sendMessageCode(user.getPhoneNumber(),user.getVerificationCode());
        return ResponseEntity.ok(authService.saveVerificationCode(user));
    }

    @PostMapping("/mobile/free/login")
    public HttpEntity<?> register( @RequestBody LoginDto loginDto){
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @GetMapping("/web/free/sendCode")
    public HttpEntity<?> sendCodeForWeb(@RequestParam String phoneNumber){
        User user = authService.getByPhoneNumberForWeb(phoneNumber);
        ApiResponse response = authService.sendSmsToUser(user);
        return ResponseEntity.status(response.isSuccess()? 201 : 409).body(response);
    }
}
