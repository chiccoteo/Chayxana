package com.chayxana.chayxana.entity;

import com.chayxana.chayxana.entity.template.AbsEntityUUID;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class User extends AbsEntityUUID implements UserDetails {

    private String fullName;

    @Column(unique = true)
    private String phoneNumber;

    private String verificationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;


    private String language;

    private String imageUrl;

    private int bronCount;

    private Timestamp spamTime;

    // bu filtni Userni disabled qilish uchun quydim yani (User umuman Brong qilolmasligi uchun yaratilgan filt)
    private boolean disabled=false;

    private boolean accountNonExpired=true;
    private boolean accountNonLocked=true;
    private boolean credentialsNonExpired=true;
    private boolean enabled=true;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    @Override
    public String getPassword() {
        return verificationCode;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    public User(String fullName, String phoneNumber, String verificationCode, Role role,  String language, int bronCount, Timestamp spamTime, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.verificationCode = verificationCode;
        this.role = role;
        this.language = language;
        this.bronCount = bronCount;
        this.spamTime = spamTime;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public User(String fullName, String phoneNumber, String verificationCode, Role role,  String language, int bronCount, Timestamp spamTime, boolean disabled, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.verificationCode = verificationCode;
        this.role = role;
        this.language = language;
        this.bronCount = bronCount;
        this.spamTime = spamTime;
        this.disabled = disabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }
}
