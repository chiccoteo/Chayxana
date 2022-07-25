package com.chayxana.chayxana.controller;

import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.RegionDto;
import com.chayxana.chayxana.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/mobile/region")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class RegionController {

    private final RegionService regionService;


    /**
     * Adding a new region
     * @param regionDto RegionDto
     * @return ResponseEntity
     * If ApiResponse return true, we return 201 (CREATED) at HttpStatus else 409 (CONFLICTED) at HttpStatus
     */
    @PostMapping
    public HttpEntity<?> addRegion(@Valid @RequestBody RegionDto regionDto) {
        ApiResponse apiResponse = regionService.addRegion(regionDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    /**
     * Get all Regions
     * @return ResponseEntity
     */
    @GetMapping
    public HttpEntity<?> getRegions() {
        ApiResponse apiResponse = regionService.getRegions();
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Editing a region by id
     * @param id Long
     * @param regionDto RegionDto
     * @return ResponseEntity
     * If ApiResponse return true, we return 202 (ACCEPTED) at HttpStatus else 404 (NOT FOUND) at HttpStatus
     */
    @PutMapping
    public HttpEntity<?> editRegionById(@RequestParam(name = "id") Long id, @RequestBody RegionDto regionDto) {
        ApiResponse apiResponse = regionService.editRegionById(id, regionDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 404).body(apiResponse);
    }

    /**
     * Deleting a region by id
     * @param id Long
     * @return ResponseEntity
     * If ApiResponse return true, we return 200 (OK) at HttpStatus else 404 (NOT FOUND) at HttpStatus
     */
    @DeleteMapping
    public HttpEntity<?> deleteRegionById(@RequestParam(name = "id") Long id) {
        ApiResponse apiResponse = regionService.deleteRegionById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 404).body(apiResponse);
    }

}
