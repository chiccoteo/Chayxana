package com.chayxana.chayxana.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IconMultiSendDto {

    private UUID id;
    private String fileOriginalName;
    private Long size;
    private String contentType;
    private String name;
    private byte[] bytes;

    public IconMultiSendDto(UUID id, String name) {
        this.id = id;
        this.name = name;

    }



    public IconMultiSendDto(UUID id, String fileOriginalName, Long size, String contentType, String name) {
        this.id = id;
        this.fileOriginalName = fileOriginalName;
        this.size = size;
        this.contentType = contentType;
        this.name = name;
    }
}
