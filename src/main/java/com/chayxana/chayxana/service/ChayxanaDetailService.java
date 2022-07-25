package com.chayxana.chayxana.service;

import com.chayxana.chayxana.entity.Chayxana;
import com.chayxana.chayxana.entity.ChayxanaDetail;
import com.chayxana.chayxana.entity.ChayxanaImage;
import com.chayxana.chayxana.payload.*;
import com.chayxana.chayxana.repo.ChayxanaDetailRepo;
import com.chayxana.chayxana.repo.ChayxanaImageRepo;
import com.chayxana.chayxana.repo.ChayxanaRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChayxanaDetailService {

    private final ChayxanaDetailRepo chayxanaDetailRepo;

    private final MapperDTO mapperDTO;

    private final ChayxanaImageRepo chayxanaImageRepo;

    private final ChayxanaRepo chayxanaRepo;


    /**
     * Add a new ChayxanaDetail
     *
     * @param chayxanaDetailDto ChayxanaDetailDto
     * @return ApiResponse
     * If such icon exists, return true at ApiResponse else return false
     */
    public ApiResponse addChayxanaDetail(ChayxanaDetailDto chayxanaDetailDto) {
        // Generate ChayxanaDetail from ChayxanaDetailDto
        ChayxanaDetail chayxanaDetail = mapperDTO.generateChayxanaDetailFromChayxanaDetailDto(chayxanaDetailDto);

        // Checking if there is such an icon by id
        if (chayxanaDetail == null)
            return new ApiResponse(false, "Such an icon does not exist");

        // Saving ChayxanaDetail
        chayxanaDetail = chayxanaDetailRepo.save(chayxanaDetail);
        return new ApiResponse(true, "Successfully saved", chayxanaDetail.getId());
    }

    /**
     * Get ChayxanaDetails (Actives or NoActives)
     *
     * @param areActive boolean
     * @return ApiResponse
     */
    public ApiResponse getChayxanaDetails(boolean areActive) {
        List<ChayxanaDetail> chayxanaDetailList;
        if (areActive)
            chayxanaDetailList = chayxanaDetailRepo.findAll();
        else
            chayxanaDetailList = chayxanaDetailRepo.findAllByActiveIsTrue();
        List<ChayxanaDetailDto> chayxanaDetailDtoList = new ArrayList<>();
        for (ChayxanaDetail chayxanaDetail : chayxanaDetailList) {
            ChayxanaDetailDto chayxanaDetailDto = mapperDTO.generateChayxanaDetailDtoFromChayxanaDetail(chayxanaDetail);
            chayxanaDetailDtoList.add(chayxanaDetailDto);
        }
        return new ApiResponse(true, "All chayxanaDetails", chayxanaDetailDtoList);
    }

    /**
     * Edit chayxana detail by id
     *
     * @param id                Long
     * @param chayxanaDetailDto ChayxanaDetailDto
     * @return ApiResponse
     * If there is not such a chayxanaDetail by id or there is not such an icon by id, we return false at ApiResponse else true at ApiResponse
     */
    public ApiResponse editChayxanaDetailById(Long id, ChayxanaDetailDto chayxanaDetailDto) {

        // Checking if there is such a chayxanaDetail by id
        Optional<ChayxanaDetail> optionalChayxanaDetail = chayxanaDetailRepo.findById(id);
        if (optionalChayxanaDetail.isEmpty())
            return new ApiResponse(false, "Such a chayxana detail does not exist");
        ChayxanaDetail oldChayxanaDetail = optionalChayxanaDetail.get();

        // Generate ChayxanaDetail from ChayxanaDetailDto
        ChayxanaDetail newChayxanaDetail = mapperDTO.generateChayxanaDetailFromChayxanaDetailDto(chayxanaDetailDto);

        // Checking if there is such an icon by id
        if (newChayxanaDetail == null)
            return new ApiResponse(false, "Such an icon does not exist");
        newChayxanaDetail.setId(oldChayxanaDetail.getId());

        // Editing chayxanaDetail
        chayxanaDetailRepo.save(newChayxanaDetail);
        return new ApiResponse(true, "Successfully edited");
    }

    /**
     * Delete chayxanaDetail by id
     *
     * @param id Long
     * @return ApiResponse
     * If there is not such a chayxanaDetail by id, we return false at ApiResponse else true at ApiResponse
     */
    public ApiResponse deleteChayxanaDetailById(Long id) {

        // Checking if there is such a chayxanaDetail by id
        boolean existsById = chayxanaDetailRepo.existsById(id);
        if (!existsById)
            return new ApiResponse(false, "Such a chayxana detail does not exist");

        // Deleting chayxanaDetail
        chayxanaDetailRepo.deleteById(id);
        return new ApiResponse(true, "Successfully deleted");
    }


    public ApiResponse getChayxanaDetailsAndImage(UUID id) {
        List<ChayxanaDetail> chayxanaDetailList = chayxanaDetailRepo.findAllByChayxana_Id(id);
        List<ChayxanaImage> chayxanaImagesByChayxanasId = chayxanaImageRepo.findChayxanaImagesByChayxanasId(id);
        List<ChayxanaDetailDto> chayxanaDetailDtos = new ArrayList<>();
        if (chayxanaDetailList.isEmpty()){
            return new ApiResponse(false, "There is no such a chayxana detail");
        }
        else {
            for (ChayxanaDetail chayxanaDetail : chayxanaDetailList) {
                ChayxanaDetailDto chayxanaDetailDto = mapperDTO.generateChayxanaDetailDtoFromChayxanaDetail(chayxanaDetail);
                chayxanaDetailDtos.add(chayxanaDetailDto);
            }
        }

        if (chayxanaImagesByChayxanasId.isEmpty()) {
            return new ApiResponse(false, "There is no such a chayxana image");
        }else
        {

            List<ImageDTO>  imageDTOList = new ArrayList<>();
            for (ChayxanaImage chayxanaImage : chayxanaImagesByChayxanasId) {
                ImageDTO imageDTO ;
                try {
                    imageDTO = mapperDTO.generateImageToImageDTO(chayxanaImage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                imageDTOList.add(imageDTO);
                GetChayxanaDetailAndImageDto detailAndImage = mapperDTO.chayxanaDetailDtosAndImage(chayxanaDetailDtos, imageDTOList);
                return new ApiResponse(true, "Successfully got", detailAndImage);
            }

        }
        return null;
    }


}
