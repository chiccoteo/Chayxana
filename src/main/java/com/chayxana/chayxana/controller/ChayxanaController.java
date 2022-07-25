package com.chayxana.chayxana.controller;

import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.ChayxanaDto;
import com.chayxana.chayxana.payload.DetailListDto;
import com.chayxana.chayxana.service.ChayxanaService;
import com.chayxana.chayxana.utills.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/mobile/chayxana")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class ChayxanaController {


    private final ChayxanaService chayxanaService;

    @PostMapping
    public HttpEntity<?> createChayxana(@RequestBody ChayxanaDto chayxanaDto) {
        ApiResponse apiResponse = chayxanaService.createChayxana(chayxanaDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }


    @GetMapping
    public HttpEntity<?> getAllChayxana() {
        ApiResponse apiResponse = chayxanaService.getAllChayxana();
        return ResponseEntity.status(200).body(apiResponse);
    }


    @GetMapping("/chayxanaId")
    public HttpEntity<?> getByChayxanaId(@RequestParam UUID chayxanaId) {
        ApiResponse apiResponse = chayxanaService.getById(chayxanaId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @GetMapping("/userId")
    public HttpEntity<?> getByUserId(@RequestParam UUID userId) {
        ApiResponse apiResponse = chayxanaService.getAllByUserId(userId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping
    public HttpEntity<?> deleteChayxana(@RequestParam UUID chayxanaId) {
        ApiResponse apiResponse = chayxanaService.deleteChayxana(chayxanaId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);

    }

    @PutMapping("{chayxanaId}")
    public HttpEntity<?> editChayxana(@PathVariable UUID chayxanaId, @RequestBody ChayxanaDto chayxanaDto) {
        ApiResponse apiResponse = chayxanaService.editChayxana(chayxanaId, chayxanaDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);

    }


    @GetMapping("/getAllPageble")
    public HttpEntity<?> getAllPageble(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
        ApiResponse apiResponse = chayxanaService.getAllPageble(page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @GetMapping("/getAllForClient")
    public HttpEntity<?> getAllChayxanaForClient(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                                 @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
        ApiResponse apiResponse = chayxanaService.getAllChayxanaForClient(page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/getOrders")
    public HttpEntity<?> getOrdersChayxana(@RequestParam UUID chayxanaId,
                                           @RequestParam String name) {
        ApiResponse apiResponse = chayxanaService.getAllOrdersFromChayxana(chayxanaId,name);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);

    }

    @GetMapping("/address")
    public HttpEntity<?> getByLonLat(@RequestParam double lon, @RequestParam double lat) {
        ApiResponse apiResponse = chayxanaService.getbyLonLat(lon, lat);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/isActive/{id}")
    public HttpEntity<?> editIsActive(@PathVariable UUID id, @RequestParam boolean isActive) {
        ApiResponse apiResponse = chayxanaService.editIsActive(id, isActive);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/getByName/{name}")
    public HttpEntity<?> getAllChayxanaByName(@PathVariable String name){
        ApiResponse allChayxanaByName = chayxanaService.getAllChayxanaByName(name);
        return ResponseEntity.status(200).body(allChayxanaByName);
    }


    @GetMapping("/getMainPicOfChayxana/{id}")
    public HttpEntity<?> getMainPictureOfChayxana(@PathVariable UUID id, HttpServletResponse response) throws IOException {
        ApiResponse apiResponse = chayxanaService.getMainPictureOfChayxana(id, response);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/getAmountOfPicsOfChayxana/{id}")
    public HttpEntity<?> getAmountOfPicturesOfChayxana(@PathVariable UUID id){
        ApiResponse apiResponse = chayxanaService.getAmountOfPicturesOfChayxana(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

//    @GetMapping("/ChayxanaDetails/{id}")
//    public HttpEntity<?> getChayxanaDetails(@PathVariable List<Long> id){
//        ApiResponse apiResponse=chayxanaService.getChayxanaDetailsByID(id);
//        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse);
//    }
//    @GetMapping("getChatxanas/roomDetails/{ids}")
//    public HttpEntity<?> getChayxanasByRoomDetails(@PathVariable List<Long> ids){
//        ApiResponse apiResponse=chayxanaService.getChayxanasByRoomDetails(ids);
//        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
//    }

    @PostMapping("/get-chayxana-by-detail-ids")
    public HttpEntity<?> getChayxanaDetails(@RequestBody DetailListDto detailListDto){
        ApiResponse apiResponse=chayxanaService.getChayxanaByDetailList(detailListDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:400).body(apiResponse);
    }

}
