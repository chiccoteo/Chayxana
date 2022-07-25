package com.chayxana.chayxana.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {

    private UUID id;
    private String originalName;
    private String name;
    private Long size;
    private String contentType;
    private boolean mainImage;
    private byte[] bytes;

    public ImageDTO(UUID id, String originalName, String name, Long size, String contentType, boolean mainImage) {
        this.id = id;
        this.originalName = originalName;
        this.name = name;
        this.size = size;
        this.contentType = contentType;
        this.mainImage = mainImage;
    }
}
