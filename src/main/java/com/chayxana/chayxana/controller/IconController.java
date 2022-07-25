package com.chayxana.chayxana.controller;

import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.IconUpdateDto;
import com.chayxana.chayxana.service.IconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/mobile/icon")
@CrossOrigin(maxAge = 3600)
public class IconController {


    @Autowired
    IconService iconService;


    /**
     * The method which uploads icon (s) from user FileSystem.
     *
     * @param request MultipartHttpServletRequest
     * @return ResponseEntity
     */
    @PostMapping("/add-icon")
    public HttpEntity<?> uploadIcon(MultipartHttpServletRequest request) {
        ApiResponse apiResponse = iconService.uploadIcon(request);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }



    /**
     * The method which gets all Icons' data and main content in bytes.
     *
     * @return ResponseEntity
     */
    @GetMapping
    public HttpEntity<?> getAllIcons() {
        ApiResponse apiResponse = iconService.getAllIcons();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }



    /**
     * The method which downloads (sends) icon (s) to user.
     *
     * @param id       UUID
     * @param response HttpServletResponse
     * @return ResponseEntity
     * @throws IOException IOException
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getIconById(@PathVariable UUID id, HttpServletResponse response) throws IOException {
        ApiResponse apiResponse = iconService.getIconById(id, response);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).build();
    }



    /**
     * The method which updates the name of icon.
     *
     * @param response HttpServletResponse
     * @return ResponseEntity
     * @throws IOException IOException
     */
    @PostMapping("/update-icon")
    public ResponseEntity<?> updateIconById(@RequestBody IconUpdateDto dto, HttpServletResponse response) throws IOException {
        ApiResponse apiResponse = iconService.updateIconById(dto, response);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).build();
    }



    /**
     * The method which deletes icon.
     *
     * @param id UUID
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteIconById(@PathVariable UUID id) {
        ApiResponse apiResponse = iconService.deleteIcon(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }


}
