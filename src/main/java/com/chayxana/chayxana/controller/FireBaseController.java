package com.chayxana.chayxana.controller;

import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/mobile/firebase")
@CrossOrigin(maxAge = 3600)
public class FireBaseController {

    @Autowired
    NotificationService notificationService;

    @GetMapping
    public HttpEntity<?> getAllNotification(@RequestParam("userId") UUID userId,
                                            @RequestParam("all") boolean all){
        ApiResponse response = notificationService.getAllNotification(userId, all);
        return ResponseEntity.status(response.isSuccess()? 200: 409).body(response);
    }

    @PutMapping("/{notificationId}")
    public HttpEntity<?> checkNotification(@PathVariable Long notificationId){
        ApiResponse responses = notificationService.checkNotification(notificationId);
        return ResponseEntity.ok(responses);
    }
}