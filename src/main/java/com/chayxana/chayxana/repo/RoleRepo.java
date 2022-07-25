package com.chayxana.chayxana.repo;

import com.chayxana.chayxana.entity.Role;
import com.chayxana.chayxana.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {

    Role findRoleByRoleName(RoleName roleName);



}
