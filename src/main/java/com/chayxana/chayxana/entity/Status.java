package com.chayxana.chayxana.entity;

import com.chayxana.chayxana.entity.enums.StatusName;
import com.chayxana.chayxana.entity.template.AbsEntityLong;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "statuses")
public class Status extends AbsEntityLong {

    @Column(nullable = false, unique = true)
    @Enumerated(value = EnumType.STRING)
    private StatusName statusName;
}

