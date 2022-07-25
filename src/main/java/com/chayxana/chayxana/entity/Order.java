package com.chayxana.chayxana.entity;

import com.chayxana.chayxana.entity.template.AbsEntityUUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.objenesis.SpringObjenesis;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "orders")
public class Order extends AbsEntityUUID {

    @ManyToOne
    private User user;

    @ManyToOne
    private Room room;

    @Column(nullable = false)
    private Timestamp orderTime;

    @Column(nullable = false)
    private int personAmount;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    private Status status;

    private String message;
}
