package com.chayxana.chayxana.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailListDto {
    private List<Long> roomDetails;
    private List<Long> chayxanaDetails;
}