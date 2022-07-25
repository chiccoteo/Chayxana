package com.chayxana.chayxana.controller;

import com.chayxana.chayxana.entity.RoomDetail;
import com.chayxana.chayxana.exceptions.PageSizeException;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.RoomDetailDTO;
import com.chayxana.chayxana.service.RoomDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/mobile/room-detail")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class RoomDetailController {

    // Controller
    // Written By Boburbek!!!

    private final RoomDetailService roomDetailService;

    @GetMapping
    public HttpEntity<?> getAllRoomDetails(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        ApiResponse apiResponse = roomDetailService.getAllRoomDetails(page - 1, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getRoomDetailById(@PathVariable Long id) {
        ApiResponse apiResponse = roomDetailService.getRoomDetailById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @PostMapping
    public HttpEntity<?> addRoomDetail(@Valid @RequestBody RoomDetailDTO dto) {
        ApiResponse apiResponse = roomDetailService.addRoomDetail(dto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editRoomDetailById(@PathVariable Long id, @Valid @RequestBody RoomDetailDTO dto) {
        ApiResponse apiResponse = roomDetailService.editRoomDetailById(id, dto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteRoomDetailById(@PathVariable Long id) {
        ApiResponse apiResponse = roomDetailService.deleteRoomDetailById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
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