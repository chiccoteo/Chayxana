package com.chayxana.chayxana.entity;

import com.chayxana.chayxana.entity.template.AbsEntityUUID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "chayxana")
@Builder
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Chayxana extends AbsEntityUUID {

    private String name;

    @ManyToOne
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Address address;

    private String descriptionUz;

    @Column(nullable = false)
    private String descriptionRu;

    @Column(nullable = false)
    private String descriptionEn;

    @Column(nullable = false)
    private Time startTime;

    @Column(nullable = false)
    private Time endTime;

    @Column(nullable = false)
    private String phoneNumber;

    private boolean active;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JsonIgnore
    private Set<ChayxanaImage> tutorials = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private List<ChayxanaDetail> chayxanaDetails;

    private boolean isDelete;

    public Chayxana(String name) {
        this.name = name;
    }
}
