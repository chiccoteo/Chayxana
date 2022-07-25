package com.chayxana.chayxana.payload.SMS;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class SMSDto {

    private Data data;

    private String message;

    private String tokenType;


}
