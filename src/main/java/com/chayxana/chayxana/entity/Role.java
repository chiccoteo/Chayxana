package com.chayxana.chayxana.entity;

import com.chayxana.chayxana.entity.enums.RoleName;
import com.chayxana.chayxana.entity.template.AbsEntityLong;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "roles")
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Role extends AbsEntityLong implements GrantedAuthority {

    @Column(nullable = false, unique = true)
    @Enumerated(value = EnumType.STRING)
    private RoleName roleName;

    @Override
    public String getAuthority() {
        return roleName.name();
    }
}
