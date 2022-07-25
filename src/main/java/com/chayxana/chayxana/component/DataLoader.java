package com.chayxana.chayxana.component;

import com.chayxana.chayxana.entity.*;
import com.chayxana.chayxana.entity.enums.RoleName;
import com.chayxana.chayxana.entity.enums.StatusName;
import com.chayxana.chayxana.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepo roleRepo;
    private final SmsForApiRepo smsForApiRepo;
    private final StatusRepo statusRepo;
    private final RegionRepo regionRepo;
    private final DistrictRepo districtRepo;
    private final AddressRepo addressRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.sql.init.mode}")
    private String mode;

    @Override
    public void run(String... args) throws Exception {
        if (mode.equals("always")) {
            List<Role> roles = new ArrayList<>();
            roles.add(new Role(RoleName.ROLE_SUPERADMIN));
            roles.add(new Role(RoleName.ROLE_CHAYXANACHI));
            roles.add(new Role(RoleName.ROLE_USER));
            roleRepo.saveAll(roles);

            Region toshkent = new Region("Toshkent");
            toshkent = regionRepo.save(toshkent);
            District shayhontoxur = new District("Shayhontoxur", toshkent);
            shayhontoxur = districtRepo.save(shayhontoxur);
            Address address = new Address(shayhontoxur, "Beruniy shox ko'chasi", 152.01, 125.01);
            address = addressRepo.save(address);
            User superAdmin = new User(
                    "SuperAdmin",
                    " 998945774543",
                    "",
                    roleRepo.findRoleByRoleName(RoleName.ROLE_SUPERADMIN),

                    "uz",
                    0,
                    null,
                    true,
                    true,
                    true,
                    true
            );
            User chayxanachi = new User(
                    "chayxanachi",
                    " 998916864416",
                    "",
                    roleRepo.findRoleByRoleName(RoleName.ROLE_CHAYXANACHI),
                    "uz",
                    0,
                    null,
                    true,
                    true,
                    true,
                    true,
                    true
            );
            User admin2 = new User(
                    "Admin2",
                    " 998949244114",
                    "",
                    roleRepo.findRoleByRoleName(RoleName.ROLE_SUPERADMIN),

                    "uz",
                    0,
                    null,
                    true,
                    true,
                    true,
                    true
            );
            User test = new User(
                    "Test",
                    " 998945774545",
                    "",
                    roleRepo.findRoleByRoleName(RoleName.ROLE_CHAYXANACHI),

                    "uz",
                    0,
                    null,
                    true,
                    true,
                    true,
                    true
            );
            userRepo.save(superAdmin);
            userRepo.save(chayxanachi);
            userRepo.save(admin2);
            userRepo.save(test);

            List<Status> statuses = new ArrayList<>();
            statuses.add(new Status(StatusName.NEW));
            statuses.add(new Status(StatusName.OLD));
            statuses.add(new Status(StatusName.DELETE));
            statusRepo.saveAll(statuses);
            smsForApiRepo.save(new SmsForToken("test@eskiz.uz","j6DWtQjjpLDNjWEk74Sx",
                    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjUsInJvbGUiOiJ1c2VyIiwiZGF0YSI6eyJpZCI6NSwibmFtZSI6Ilx1MDQyN1x1MDQxZiBCZXN0IEludGVybmV0IFNvbHV0aW9uIiwiZW1haWwiOiJ0ZXN0QGVza2l6LnV6Iiwicm9sZSI6InVzZXIiLCJhcGlfdG9rZW4iOm51bGwsInN0YXR1cyI6ImFjdGl2ZSIsInNtc19hcGlfbG9naW4iOiJlc2tpejIiLCJzbXNfYXBpX3Bhc3N3b3JkIjoiZSQkayF6IiwidXpfcHJpY2UiOjUwLCJiYWxhbmNlIjo3NTUwLCJpc192aXAiOjAsImhvc3QiOiJzZXJ2ZXIxIiwiY3JlYXRlZF9hdCI6bnVsbCwidXBkYXRlZF9hdCI6IjIwMjItMDQtMTlUMTM6MzY6NTEuMDAwMDAwWiJ9LCJpYXQiOjE2NTA0OTkwNDcsImV4cCI6MTY1MzA5MTA0N30.dcANlfii2_LaS5eA8jGB2LFDjOMg-eSjLhZnr0WzsQw",
                    "https://notify.eskiz.uz/api/auth/login"));

        }
    }

}
