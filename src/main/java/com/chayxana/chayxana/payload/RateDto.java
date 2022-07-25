package com.chayxana.chayxana.payload;

import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateDto {

    private UserDto userDto;// UserDto ni quyamiz


    private UUID chayxanaId;

    private Timestamp createdAt;

    private int roomNumber;

    private int rate;

    private String comment;

}