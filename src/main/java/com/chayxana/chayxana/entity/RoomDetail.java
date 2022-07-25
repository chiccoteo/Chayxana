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
public class RoomDetail extends AbsEntityLong {

    @Column(nullable = false)
    private String name;

    private boolean active;

    @ManyToOne
    private Icon icon;

    public RoomDetail(Long id, String name, boolean active, Icon icon) {
        super(id);
        this.name = name;
        this.active = active;
        this.icon = icon;
    }


}
