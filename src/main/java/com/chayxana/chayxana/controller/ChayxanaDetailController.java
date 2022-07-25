package com.chayxana.chayxana.controller;

import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.ChayxanaDetailDto;
import com.chayxana.chayxana.service.ChayxanaDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/mobile/chayxanaDetail")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class ChayxanaDetailController {

    private final ChayxanaDetailService chayxanaDetailService;


    /**
     * Add a new ChayxanaDetail
     *
     * @param chayxanaDetailDto ChayxanaDetailDto
     * @return ResponseEntity
     * If ApiResponse return true, we return 201 (CREATED) at HttpStatus else 404 (NOT FOUND) at HttpStatus
     */
    @PostMapping
    public HttpEntity<?> addChayxanaDetail(@RequestBody ChayxanaDetailDto chayxanaDetailDto) {
        ApiResponse apiResponse = chayxanaDetailService.addChayxanaDetail(chayxanaDetailDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 404).body(apiResponse);
    }

    /**
     * Get ChayxanaDetails (Actives or NoActives)
     *
     * @param all boolean
     * @return ResponseEntity
     */
    @GetMapping
    public HttpEntity<?> getChayxanaDetails(@RequestParam("all") boolean all) {
        ApiResponse apiResponse = chayxanaDetailService.getChayxanaDetails(all);
        return ResponseEntity.status(201).body(apiResponse);
    }

    /**
     * Edit chayxana detail by id
     *
     * @param id                Long
     * @param chayxanaDetailDto ChayxanaDetailDto
     * @return ResponseEntity
     * If ApiResponse return true, we return 202 (ACCEPTED) at HttpStatus else 404 (NOT FOUND) at HttpStatus
     */
    @PutMapping
    public HttpEntity<?> editChayxanaDetailById(@RequestParam("id") Long id, @RequestBody ChayxanaDetailDto chayxanaDetailDto) {
        ApiResponse apiResponse = chayxanaDetailService.editChayxanaDetailById(id, chayxanaDetailDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    /**
     * Delete chayxanaDetail by id
     * @param id Long
     * @return ResponseEntity
     * If ApiResponse return true, we return 200 (OK) at HttpStatus else 404 (NOT FOUND) at HttpStatus
     */
    @DeleteMapping
    public HttpEntity<?> deleteChayxanaDetailById(@RequestParam("id") Long id) {
        ApiResponse apiResponse = chayxanaDetailService.deleteChayxanaDetailById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    //Chayxana idsi berilsa shu chayxanada
    // mavjud bo'lgan detaillarni berib yuborish kerek.
    // icon rasmi hamda detailning nomini qo'shib junatish kerek.
    // chayxana tagida ko'rinib turish uchun

    /**
     * Get ChayxanaDetail by id
     * @return ResponseEntity with ChayxanaDetailDto or ApiResponse with 404 (NOT FOUND)
     * at HttpStatus if not found or 500 (INTERNAL SERVER ERROR) at HttpStatus if error occurs while getting ChayxanaDetail by id from database or if id is null
     */

    @GetMapping("/{id}")
    public HttpEntity<?>getChayxanaDetailsAndEmage(@PathVariable UUID id){
        ApiResponse apiResponse=chayxanaDetailService.getChayxanaDetailsAndImage(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse);
    }


}
