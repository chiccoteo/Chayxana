package com.chayxana.chayxana.entity;

import com.chayxana.chayxana.entity.template.AbsEntityUUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.LinkedList;
import java.util.Queue;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DeviceData extends AbsEntityUUID {

    @Column(unique = true)
    private String deviceId;

    private String deviceType;

    private String deviceToken;

    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
