package com.chayxana.chayxana.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


public interface LastSixMonthResultsDto {

    Timestamp getTimestamps();
     int getCounts();

}
