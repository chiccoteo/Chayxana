package com.chayxana.chayxana.service;

import com.chayxana.chayxana.entity.District;
import com.chayxana.chayxana.entity.Region;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.DistrictDto;
import com.chayxana.chayxana.repo.DistrictRepo;
import com.chayxana.chayxana.repo.RegionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DistrictService {

    private final DistrictRepo districtRepo;

    private final RegionRepo regionRepo;

    private final MapperDTO mapperDTO;


    /**
     * Adding a new district to the given region
     *
     * @param districtDto DistrictDto
     * @return ApiResponse
     * If such a region does not exist or such a district already exist in this region, we return false at ApiResponse else true at ApiResponse
     */
    public ApiResponse addDistrict(DistrictDto districtDto) {
        // Checking if there is such a region by id
        Optional<Region> optionalRegion = regionRepo.findById(districtDto.getRegionDto().getId());
        if (optionalRegion.isEmpty())
            return new ApiResponse(false, "Such a region does not exist");

        // Checking if there is such a district by name in this region
        boolean existsByNameAndRegionId = districtRepo.existsByNameAndRegion_Id(districtDto.getName(), districtDto.getRegionDto().getId());
        if (existsByNameAndRegionId) {
            return new ApiResponse(false, "Such a district already exists in this region");
        }

        // Saving a district
        District district = mapperDTO.generateDistrictFromDistrictDto(districtDto);
        districtRepo.save(district);
        return new ApiResponse(true, "Successfully saved");
    }

    /**
     * Get all districts in the given region
     *
     * @param regionId Long
     * @return ApiResponse
     * If there is not such a region by id, we return false at ApiResponse else true and districtDtoList at ApiResponse
     */
    public ApiResponse getDistrictsByRegionId(Long regionId) {
        // Checking if there is such a region by id
        Optional<Region> optionalRegion = regionRepo.findById(regionId);
        if (optionalRegion.isEmpty())
            return new ApiResponse(false, "Such a region does not exist");

        List<District> districts = districtRepo.findAllByRegion_Id(regionId);

        // Generate DistrictDtoList from Districts
        List<DistrictDto> districtDtoList = new ArrayList<>();
        for (District district : districts) {
            DistrictDto districtDto = mapperDTO.generateDistrictDtoFromDistrict(district);
            districtDtoList.add(districtDto);
        }

        return new ApiResponse(true, "All districts in this region", districtDtoList);
    }

    /**
     * Editing a district by id
     *
     * @param id          Long
     * @param districtDto DistrictDto
     * @return ApiResponse
     * If there is not such a district by id or there is not such a region by id, we return false at ApiResponse else true at ApiResponse
     */
    public ApiResponse editDistrictById(Long id, DistrictDto districtDto) {
        // Checking if there is such a district by id
        Optional<District> optionalDistrict = districtRepo.findById(id);
        if (optionalDistrict.isEmpty())
            return new ApiResponse(false, "Such a district does not exist");

        // Checking if there is such a region by id
        Optional<Region> optionalRegion = regionRepo.findById(districtDto.getRegionDto().getId());
        if (optionalRegion.isEmpty())
            return new ApiResponse(false, "Such a region does not exist");

        // Editing the district
        District district = optionalDistrict.get();
        district.setName(districtDto.getName());
        district.setRegion(optionalRegion.get());
        districtRepo.save(district);

        return new ApiResponse(true, "Successfully edited");
    }

    /**
     * Deleting a district by id
     *
     * @param id Long
     * @return ApiResponse
     * If there is not such a district by id, we return false at ApiResponse else true at ApiResponse
     */
    public ApiResponse deleteDistrictById(Long id) {
        // Checking if there is such a district by id
        Optional<District> optionalDistrict = districtRepo.findById(id);
        if (optionalDistrict.isEmpty())
            return new ApiResponse(false, "Such a district does not exist");

        // Deleting the district
        districtRepo.deleteById(id);

        return new ApiResponse(true, "Successfully deleted");
    }

    /**
     * Get all districts
     * @return ApiResponse
     */
    public ApiResponse getAllDistricts() {
        List<District> districts = districtRepo.findAll();

        // Generate DistrictDtoList from Districts
        List<DistrictDto> districtDtoList = new ArrayList<>();
        for (District district : districts) {
            DistrictDto districtDto = mapperDTO.generateDistrictDtoFromDistrict(district);
            districtDtoList.add(districtDto);
        }
        return new ApiResponse(true, "All districts", districtDtoList);
    }
}
