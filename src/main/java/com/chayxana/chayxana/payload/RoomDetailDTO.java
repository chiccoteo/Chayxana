package com.chayxana.chayxana.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDetailDTO {

    private Long id;

    @NotNull (message = "Cannot be blank!")
    private String name;

    private UUID iconId;

    private boolean active;

    public RoomDetailDTO(Long id) {
        this.id = id;
    }

    public RoomDetailDTO(Long id, UUID iconId) {
        this.id = id;
        this.iconId = iconId;
    }
}