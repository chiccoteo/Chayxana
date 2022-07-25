package com.chayxana.chayxana.controller;

import com.chayxana.chayxana.entity.SmsForToken;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/open/mobile/smsFor")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class SmsForTokenController {

    private final SmsService smsService;

    @PostMapping
    public HttpEntity<?> addSmsForToken(@RequestBody SmsForToken smsForToken){
        ApiResponse apiResponse = smsService.addSmsApi(smsForToken);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }


  /**  Sms junatishni tekshirish uchun*/
    @PostMapping("/sendsms")
    public HttpEntity<?> sendSms(@RequestParam String phoneNumber, String code){
        System.out.println(phoneNumber+ ": " +code);
        okhttp3.ResponseBody responseBody = smsService.sendMessageCode(phoneNumber, code);
        return ResponseEntity.ok(responseBody);

    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> xatolikniUshlabOlibOzimizniMessageniQaytarish(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
          String fieldName = ((FieldError) error).getField();
          String errorMessage = error.getDefaultMessage();
          errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
