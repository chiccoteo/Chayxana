package com.chayxana.chayxana.entity;

import com.chayxana.chayxana.entity.template.AbsEntityUUID;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Address extends AbsEntityUUID {

    @ManyToOne(fetch = FetchType.LAZY)
    private District district;

    private String streetName;


    @Column(nullable = false)
    private double lan;

    @Column(nullable = false)
    private double lat;
}
