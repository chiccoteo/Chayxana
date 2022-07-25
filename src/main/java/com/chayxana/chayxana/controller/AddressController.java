package com.chayxana.chayxana.controller;

import com.chayxana.chayxana.payload.AddressDto;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/web/address")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class AddressController {

    private final AddressService addressService;

    /**
     * add new address
     *
     * @param dto AddressDto
     * @return ApiResponse
     *
     * If saved return 201 (created) else 409 (Conflict)
     */
    @PostMapping("/add")
    public HttpEntity<?> addAddress(@RequestBody AddressDto dto){
        ApiResponse response = addressService.saveAddress(dto);
        return ResponseEntity.status(response.isSuccess()? 201 : 409).body(response);
    }

    /**
     * update Address which id
     *
     * @param dto AddressDto
     * @return ApiResponse
     *
     * If saved return 202 (updated) else 409 (Conflict)
     */
    @PutMapping("/edit")
    public HttpEntity<?> editAddress(@RequestParam(name = "id") UUID id, @RequestBody AddressDto dto){
        ApiResponse response = addressService.editAddress(id,dto);
        return ResponseEntity.status(response.isSuccess()? 200 : 409).body(response);
    }

    /**
     * delete address which id
     * @param id
     * @return ApiResponse
     * if deleted true return 204  else 409
     */
    @DeleteMapping("/delete/{id}")
    public HttpEntity<?> deleteAddress(@PathVariable UUID id){
        ApiResponse response = addressService.deleteAddress(id);
        return ResponseEntity.status(response.isSuccess()? 200 : 409).body(response);
    }
}
