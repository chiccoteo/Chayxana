package com.chayxana.chayxana.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "firebaseEntity")
public class Firebase {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @ManyToOne
     private Notification notification;

     @ManyToOne
     private User user;

     private boolean read;

     @ElementCollection // 1
     @CollectionTable(name = "my_list", joinColumns = @JoinColumn(name = "id")) // 2
     @Column(name = "list") // 3
     private List<String> registration_ids;

     private String click_action;

     public Firebase(Notification notification) {
          this.notification = notification;
     }
}