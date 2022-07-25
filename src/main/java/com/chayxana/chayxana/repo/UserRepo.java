package com.chayxana.chayxana.repo;

import com.chayxana.chayxana.entity.Role;
import com.chayxana.chayxana.entity.User;
import com.chayxana.chayxana.entity.enums.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> getUsersByBronCountEquals(int bronCount);

//    Page<User> findAllByRoleIsNot(Role role, Pageable pageable);

    Page<User> findAllByRoleIsNotAndAccountNonLockedTrue(Role role, Pageable pageable);

    Page<User> findAllByRole(Role role, Pageable pageable);

//    Page<User> findAllByRoleRoleName(RoleName role_roleName, Pageable pageable);

    Page<User> findAllByRoleRoleNameAndAccountNonLockedTrue(RoleName role_roleName, Pageable pageable);

    boolean existsByPhoneNumber(String phoneNumber);

}
