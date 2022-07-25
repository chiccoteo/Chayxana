package com.chayxana.chayxana.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChayxanaDetailDto {

    private Long id;

    private String name;

    private boolean active;



    private IconMultiSendDto iconMultiSendDto;

}
