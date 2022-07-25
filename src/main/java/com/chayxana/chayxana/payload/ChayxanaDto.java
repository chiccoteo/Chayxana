package com.chayxana.chayxana.payload;

import com.chayxana.chayxana.entity.District;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.sql.Time;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChayxanaDto {

    private UUID chayxanaId;

    private  AddressDto addressDto;

    private UUID userId;


    private String chayxanaName;

    private String descriptionUz;

    private String descriptionRu;

    private String descriptionEn;

    private String startTime;

    private String endTime;

    private String phoneNumber;

    private int roomNumber;

    private double price;

    private boolean isActive;

    private List<ChayxanaDetailDto> chayxanaDetailDtos;


    public ChayxanaDto(UUID chayxanaId, AddressDto addressDto, String chayxanaName, String phoneNumber) {
        this.chayxanaId = chayxanaId;
        this.addressDto = addressDto;
        this.chayxanaName = chayxanaName;
        this.phoneNumber = phoneNumber;
    }

    public ChayxanaDto(UUID chayxanaId) {
        this.chayxanaId = chayxanaId;
    }

    public ChayxanaDto(String chayxanaName) {
        this.chayxanaName = chayxanaName;
    }

    @Override
    public boolean equals(Object obj) {
        return ((ChayxanaDto) obj).chayxanaId.equals(this.chayxanaId);
//        return super.equals(obj);
    }
}
