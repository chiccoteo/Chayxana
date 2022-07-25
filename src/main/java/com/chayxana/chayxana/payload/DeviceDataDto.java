package com.chayxana.chayxana.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDataDto {

    private String deviceId;

    private String deviceType;

    private String deviceToken;

    private UserDto userDto;
}
