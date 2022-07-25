package com.chayxana.chayxana.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


public interface LastOneMonthDto {

     UUID getId();
     double getPrice();
}
