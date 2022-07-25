package com.chayxana.chayxana.controller;

import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.DeviceDataDto;
import com.chayxana.chayxana.service.DeviceDataService;
import com.chayxana.chayxana.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/device-data")
@CrossOrigin(maxAge = 3600)
public class DeviceDataController {

    @Autowired
    DeviceDataService deviceDataService;

    @Autowired
    NotificationService service;

    @PostMapping()
    public HttpEntity<?> saveNewDeviceOrSetUser(@RequestBody DeviceDataDto deviceDataDto){
        ApiResponse response = deviceDataService.saveOrSetUser(deviceDataDto);
        return ResponseEntity.status(response.isSuccess()? 201 : 409).body(response);
    }

//    @GetMapping()
//    public HttpEntity<?> send(@RequestParam("title") String title,
//                              @RequestParam("body") String body,
//                              @RequestParam("token") String token){
//        ResponseEntity<?> response = service.sendNotification(title, body, token);
//        return ResponseEntity.ok(response);
//    }
}
