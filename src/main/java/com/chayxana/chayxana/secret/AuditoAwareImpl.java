package com.chayxana.chayxana.secret;

import com.chayxana.chayxana.entity.User;
import com.chayxana.chayxana.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.UUID;

@Service
public class AuditoAwareImpl implements AuditorAware<UUID> {

    @Autowired
    UserRepo userRepo;


    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication == null ||
                !authentication.isAuthenticated()||
                "anonymousUser".equals(""+authentication.getPrincipal()))){
            return
                    Optional.of(((User) authentication.getPrincipal()).getId());
        }
        return Optional.empty();
    }
}
