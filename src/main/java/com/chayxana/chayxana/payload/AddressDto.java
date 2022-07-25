package com.chayxana.chayxana.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {
    private UUID id;
    private DistrictDto districtDto;
    private String streetName;
    private double lan;
    private double lat;

}
