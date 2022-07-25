package com.chayxana.chayxana.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {

    private UUID id;

    private int roomNumber;

    private boolean bron;

    private int maxPerson;

    private int minPerson;

    private ChayxanaDto chayxanaDto;

    private BigDecimal price;

    private List<RoomDetailDTO> roomDetailDTOS;


    public RoomDto(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomDto(UUID id, int roomNumber) {
        this.id=id;
        this.roomNumber = roomNumber;
    }

    public RoomDto(UUID id, int roomNumber, ChayxanaDto chayxanaDto) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.chayxanaDto = chayxanaDto;
    }

    public RoomDto(int roomNumber, boolean bron, int maxPerson, int minPerson, ChayxanaDto chayxanaDto, BigDecimal price,List<RoomDetailDTO> roomDetailDTOS) {
        this.roomNumber = roomNumber;
        this.bron = bron;
        this.maxPerson = maxPerson;
        this.minPerson = minPerson;
        this.chayxanaDto = chayxanaDto;
        this.price = price;
        this.roomDetailDTOS=roomDetailDTOS;
    }
}