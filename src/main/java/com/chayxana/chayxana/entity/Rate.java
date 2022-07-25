package com.chayxana.chayxana.entity;


import com.chayxana.chayxana.entity.template.AbsEntityUUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rate  extends AbsEntityUUID    {
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Chayxana chayxana;

    @Column(nullable = false)
    private Integer rate;

    private String comment;

    private int roomNumber;

}