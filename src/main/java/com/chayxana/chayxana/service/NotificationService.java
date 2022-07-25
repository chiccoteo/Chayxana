package com.chayxana.chayxana.service;

import com.chayxana.chayxana.entity.DeviceData;
import com.chayxana.chayxana.entity.Firebase;
import com.chayxana.chayxana.entity.Notification;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.repo.FirebaseRepo;
import com.chayxana.chayxana.repo.NotificationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final FirebaseRepo firebaseRepo;
    private final NotificationRepo notificationRepo;

    @Value("${url.for.send.notification}")
    private String url;

    @Value("${key.for.send.notification}")
    private String key;

    public ResponseEntity<? extends Object> sendNotification(String title, String body, DeviceData deviceData, String imageUrl){

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization",key);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        Notification notification=new Notification(title,body, imageUrl);
        Firebase firebase =new Firebase(notification);
        firebase.setRegistration_ids(Collections.singletonList(deviceData.getDeviceToken()));
        firebase.setClick_action("FLUTTER_NOTIFICATION_CLICK");
        firebase.setUser(deviceData.getUser());
        firebase.setRead(false);

        HttpEntity<?> entity = new HttpEntity<>(firebase,httpHeaders);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
        if (response.getStatusCode() == HttpStatus.OK) {
//            System.out.println(response.getStatusCode());
            notification = notificationRepo.save(notification);
            firebase.setNotification(notification);
            firebaseRepo.save(firebase);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("FAILED");
    }


    public ApiResponse getAllNotification(UUID userId, boolean all) {
        List<Firebase> firebaseList = null;
        if (all){
            firebaseList = firebaseRepo.findAllByUserId(userId);
        }
        firebaseList = firebaseRepo.findAllByUserIdAndRead(userId, false);
        if (firebaseList.isEmpty()){
            return new ApiResponse(true, "NOT YET NOTIFICATION");
        }
        return new ApiResponse(true, "Succesfully", firebaseList);
    }

    public ApiResponse checkNotification(Long notificationId) {
        Optional<Firebase> firebaseOptional = firebaseRepo.findById(notificationId);
        if (firebaseOptional.isPresent()){
            Firebase firebase = firebaseOptional.get();
            firebase.setRead(true);
            firebaseRepo.save(firebase);
            return new ApiResponse(true, "Edit");
        }
        return new ApiResponse(false, "Not found notification");
    }
}