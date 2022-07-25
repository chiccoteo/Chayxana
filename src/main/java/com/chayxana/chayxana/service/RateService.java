package com.chayxana.chayxana.service;

import com.chayxana.chayxana.entity.Chayxana;
import com.chayxana.chayxana.entity.Rate;
import com.chayxana.chayxana.entity.User;
import com.chayxana.chayxana.exceptions.PageSizeException;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.RateDto;
import com.chayxana.chayxana.repo.ChayxanaRepo;
import com.chayxana.chayxana.repo.RateRepo;
import com.chayxana.chayxana.repo.UserRepo;
import com.chayxana.chayxana.utills.CommandUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RateService {

    private final RateRepo rateRepo;
    private final MapperDTO mapperDTO;
    private final ChayxanaRepo chayxanaRepo;
    private final UserRepo userRepo;


    public ApiResponse getChoyhonaRate(UUID id) {
        try {
            double chayxanaReting = 0;
            List<Rate> allByChayxana_id = rateRepo.findAllByChayxana_Id(id);
            for (Rate rate : allByChayxana_id) {
                if (rate != null) {
                    chayxanaReting += rate.getRate();
                }
            }
            return new ApiResponse(true, "Rate found", chayxanaReting / allByChayxana_id.size());
        } catch (Exception e) {
            return new ApiResponse(false, "No rate found");
        }
    }

    public ApiResponse setChoyhonaRate(UUID chayxanaId, RateDto rateDto) {

        try {
            Rate rate = new Rate();

            Optional<Chayxana> optionalChayxana = chayxanaRepo.findById(chayxanaId);
            if (optionalChayxana.isPresent()) {
                rate.setChayxana(optionalChayxana.get());
                Optional<User> userOptional = userRepo.findById(rateDto.getUserDto().getId());
                if (userOptional.isEmpty()){
                    return new ApiResponse(false, "Not found user");
                }
                rate.setUser(userOptional.get());
                rate.setRate(rateDto.getRate());
                rate.setComment(rateDto.getComment());
                rate.setRoomNumber(rateDto.getRoomNumber());
                rate = rateRepo.save(rate);
                return new ApiResponse(true, "Rate added", rate.getId());
            }
            return new ApiResponse(false, "not found Chayxana id");

        } catch (Exception e) {
            return new ApiResponse(false, "No rate added");
        }
    }

    //    @GetMapping("/chayxana/{id}") bu yul Userlarni chayxana ga nichi ball quyganini qaytaradi
    public ApiResponse getChayxanaRate(UUID id, Integer page, Integer size)throws PageSizeException {
        Page<Rate> allByChayxana_id = rateRepo.findAllByChayxanaIdOrderByCreatedAt(
                id, CommandUtils.simplePageable(page - 1, size));
        List<RateDto> rateDtoList = new ArrayList();
        for (Rate rate : allByChayxana_id) {
            RateDto rateDto = mapperDTO.generateRateDtoFrom(rate);
            rateDtoList.add(rateDto);
        }
        return new ApiResponse(true, "Rate found", rateDtoList);
    }
}