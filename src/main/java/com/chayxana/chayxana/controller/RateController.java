package com.chayxana.chayxana.controller;

import com.chayxana.chayxana.entity.User;
import com.chayxana.chayxana.exceptions.PageSizeException;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.RateDto;
import com.chayxana.chayxana.secret.CurrentUser;
import com.chayxana.chayxana.service.RateService;
import com.chayxana.chayxana.utills.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/rate")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class RateController {

    private final RateService rateService;

    /*
            *  @param    id choyhona_id
     *  @return   ResponseEntity
     */
    @GetMapping("/{id}") // Bu yul Chayxana Retingini qaytaradi
    public HttpEntity<?>getChoyhonaRate(@RequestParam UUID id){
        ApiResponse apiResponse= rateService.getChoyhonaRate(id);

        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    /* @param    rateDto
     * @return   ResponseEntity
     */
    // bu yul Chayxanaga Reting qoshadi
    @PostMapping("/set-by-chayxana-id/{chayxanaId}")
    public HttpEntity<?>setChoyhonaRate(@PathVariable("chayxanaId") UUID chayxanaId,
                                        @RequestBody RateDto rateDto){
        ApiResponse apiResponse =rateService.setChoyhonaRate(chayxanaId, rateDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    /**
     *
     * @param chayxanaId
     * @return ApiResponse
     */
    // Bu yul Chayxanaga Userlar  quyga retingini qaytaradi
    @GetMapping("/get-by-chayxana-id/{chayxanaId}")
    public HttpEntity<?>getChayxanaRate(
            @PathVariable UUID chayxanaId,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    )throws PageSizeException {
        ApiResponse apiResponse =rateService.getChayxanaRate(chayxanaId,page,size);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }





}
