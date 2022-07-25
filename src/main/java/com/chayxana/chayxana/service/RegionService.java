package com.chayxana.chayxana.service;

import com.chayxana.chayxana.entity.Region;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.RegionDto;
import com.chayxana.chayxana.repo.RegionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepo regionRepo;

    private final MapperDTO mapperDTO;


    /**
     * Adding a new region
     * @param regionDto RegionDto
     * @return ApiResponse
     * If such a region exists, we return false at ApiResponse else true at ApiResponse
     */
    public ApiResponse addRegion(RegionDto regionDto) {
        // Checking if there is such a region by name
        boolean existsByName = regionRepo.existsByName(regionDto.getName());
        if (existsByName)
            return new ApiResponse(false, "Such a region already exists");

        // Saving a region
        Region region = mapperDTO.generateRegionFromRegionDto(regionDto);
        regionRepo.save(region);

        return new ApiResponse(true, "Successfully saved");
    }

    /**
     * Get all Regions
     * @return ApiResponse(success, message, RegionDtoList)
     */
    public ApiResponse getRegions() {
        List<Region> regions = regionRepo.findAll();
        // Generate RegionDtoList from Regions
        List<RegionDto> regionDtoList = new ArrayList<>();
        for (Region region : regions) {
            RegionDto regionDto = mapperDTO.generateRegionDtoFromRegion(region);
            regionDtoList.add(regionDto);
        }

        return new ApiResponse(true, "All regions", regionDtoList);
    }

    /**
     * Editing a region by id
     * @param id Long
     * @param regionDto RegionDto
     * @return ApiResponse
     * If such a region does not exist, we return false at ApiResponse else true at ApiResponse
     */
    public ApiResponse editRegionById(Long id, RegionDto regionDto) {
        // Checking if there is such a region by id
        Optional<Region> optionalRegion = regionRepo.findById(id);
        if (optionalRegion.isEmpty())
            return new ApiResponse(false, "Such a region does not exist");

        // Editing the region
        Region region = optionalRegion.get();
        region.setName(regionDto.getName());
        regionRepo.save(region);

        return new ApiResponse(true, "Successfully edited");
    }

    /**
     * Deleting a region by id
     * @param id Long
     * @return ApiResponse
     * If such a region does not exist, we return false at ApiResponse else true at ApiResponse
     */
    public ApiResponse deleteRegionById(Long id) {
        // Checking if there is such a region by id
        Optional<Region> optionalRegion = regionRepo.findById(id);
        if (optionalRegion.isEmpty())
            return new ApiResponse(false, "Such a region does not exist");

        // Deleting the region
        regionRepo.deleteById(id);

        return new ApiResponse(true, "Successfully deleted");
    }
}
