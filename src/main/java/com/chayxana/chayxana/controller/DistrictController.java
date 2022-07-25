package com.chayxana.chayxana.controller;

import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.DistrictDto;
import com.chayxana.chayxana.service.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mobile/district")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class DistrictController {

    private final DistrictService districtService;


    /**
     * Adding a new district to the given region
     *
     * @param districtDto DistrictDto
     * @return ApiResponse
     * If ApiResponse return true, we return 201 (CREATED) else 409 (CONFLICTED)
     */
    @PostMapping
    public HttpEntity<?> addDistrict(@RequestBody DistrictDto districtDto) {
        ApiResponse apiResponse = districtService.addDistrict(districtDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    /**
     * Get all districts by the given region
     *
     * @param regionId Long
     * @return ResponseEntity
     * If ApiResponse return true, we return 200 (OK) at HttpStatus else 404 (NOT FOUND) at HttpStatus
     */
    @GetMapping("/byRegion")
    public HttpEntity<?> getDistrictsByRegionId(@RequestParam(name = "regionId") Long regionId) {
        ApiResponse apiResponse = districtService.getDistrictsByRegionId(regionId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 404).body(apiResponse);
    }

    /**
     * Get all districts
     * @return ResponseEntity
     */
    @GetMapping
    public HttpEntity<?> getAllDistricts() {
        ApiResponse apiResponse = districtService.getAllDistricts();
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Editing a district by id
     *
     * @param id          Long
     * @param districtDto DistrictDto
     * @return ResponseEntity
     * If ApiResponse return true, we return 202 (ACCEPTED) at HttpStatus else 404 (NOT FOUND) at HttpStatus
     */
    @PutMapping
    public HttpEntity<?> editDistrictById(@RequestParam(name = "id") Long id, @RequestBody DistrictDto districtDto) {
        ApiResponse apiResponse = districtService.editDistrictById(id, districtDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 404).body(apiResponse);
    }

    /**
     * Deleting a district by id
     * @param id Long
     * @return ResponseEntity
     * If ApiResponse return true, we return 200 (OK) at HttpStatus else 404 (NOT FOUND) at HttpStatus
     */
    @DeleteMapping
    public HttpEntity<?> deleteDistrictById(@RequestParam(name = "id") Long id) {
        ApiResponse apiResponse = districtService.deleteDistrictById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 404).body(apiResponse);
    }
}
