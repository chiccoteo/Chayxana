package com.chayxana.chayxana.entity;

import com.chayxana.chayxana.entity.template.AbsEntityUUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "room")
public class Room extends AbsEntityUUID {

    @Column(nullable = false)
    private int roomNumber;

    private boolean bron;

    @Column(nullable = false)
    private int maxPerson;

    @Column(nullable = false)
    private int minPerson;

    @ManyToOne
    private Chayxana chayxana;

    @ManyToMany()
    private List<RoomDetail> roomDetails;

    @Column(nullable = false)
    private BigDecimal price;

    // bu filt chayxana xonasini agar remont qilish kerek bulsa deactive qilib quyadi clientlarga kurinmaydi. 0
//    @Column(nullable = false)
    private boolean active = true;

    public Room(int roomNumber, boolean bron, int maxPerson, int minPerson, UUID chayxana_id, BigDecimal price) {

    }
}
