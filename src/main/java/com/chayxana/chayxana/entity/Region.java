package com.chayxana.chayxana.entity;

import com.chayxana.chayxana.entity.template.AbsEntityLong;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Region extends AbsEntityLong {

    @Column(nullable = false)
    private String name;
}
