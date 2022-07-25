package com.chayxana.chayxana.payload;

import com.chayxana.chayxana.entity.ChayxanaImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetChayxanaDetailAndImageDto {

        private List<ChayxanaDetailDto> chayxanaDetailDtoList;

        private List<ImageDTO> imageDTOList;


}
