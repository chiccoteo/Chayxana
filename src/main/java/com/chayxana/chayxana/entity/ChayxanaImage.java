package com.chayxana.chayxana.entity;

import com.chayxana.chayxana.entity.template.AbsEntityUUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChayxanaImage extends AbsEntityUUID {

    private String name;

    private String originalName;

    private String contentType;

    private Long size;

    private String imageUrl;

    private boolean mainImage = false;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "chayxanaImage_chayxana",
            joinColumns = { @JoinColumn(name = "chayxanaImage_id") },
            inverseJoinColumns = { @JoinColumn(name = "chayxana_id") })
    private Set<Chayxana> chayxanas = new HashSet<>();

//    @ManyToMany(fetch = FetchType.LAZY)
//    private List<Chayxana> chayxanas;
}
