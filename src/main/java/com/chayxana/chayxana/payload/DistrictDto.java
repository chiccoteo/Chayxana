package com.chayxana.chayxana.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DistrictDto {

    private Long id;

    private String name;

    private RegionDto regionDto;

    public DistrictDto(String name) {
        this.name = name;
    }
}
