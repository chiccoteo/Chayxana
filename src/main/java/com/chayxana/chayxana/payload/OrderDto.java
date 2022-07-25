package com.chayxana.chayxana.payload;

import com.chayxana.chayxana.entity.ChayxanaImage;
import com.chayxana.chayxana.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private UUID orderId;
    //    private UUID userId;
    private UserDto userDto;
    //    private UUID roomId;
    private RoomDto roomDto;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp orderTime;
    private int personAmount;
    private BigDecimal price;
    private String  status;
    private String message;

    public OrderDto(UUID orderId,Timestamp orderTime, int personAmount, BigDecimal price, String status, String message) {
        this.orderId=orderId;
        this.orderTime = orderTime;
        this.personAmount = personAmount;
        this.price = price;
        this.status = status;
        this.message = message;
    }

}