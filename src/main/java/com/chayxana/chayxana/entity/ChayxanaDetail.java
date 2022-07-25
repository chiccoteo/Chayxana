package com.chayxana.chayxana.entity;

import com.chayxana.chayxana.entity.template.AbsEntityLong;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChayxanaDetail extends AbsEntityLong {

    @Column(nullable = false)
    private String name;

    private boolean active = true;

    @ManyToOne
    private Icon icon;
}
