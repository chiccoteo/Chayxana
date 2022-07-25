package com.chayxana.chayxana.component;

import com.chayxana.chayxana.entity.User;
import com.chayxana.chayxana.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class UserBanScheduling {

    private final UserRepo userRepo;

    /**
     * millisecond da
     * 86400000L ==>  bir kun
     * initialDelay ==> bu birinchi programma run bo'lganda 3 sekunddan keyin ishlidi
     * ikkinchi marttasida esa fixedDelay ==> har shu fixedDelay vaqtida taskni bajaradi.
     *
     * cron = "0 0 1 * * *" ==> har kuni 01:00:00 da run bo'lib schedule ishlaydi.
     */
    @Scheduled(fixedDelay = 86400000L, initialDelay = 3000L)
//    @Scheduled(cron = "0 53 10 * * *")
    @Scheduled(cron = "0 0 1 * * *")
    public void checkSpamAndBanUsers() {
        List<User> userList = userRepo.getUsersByBronCountEquals(3);
        for (User user : userList) {
            Timestamp userSpamTime = user.getSpamTime();
            LocalDateTime pldt = userSpamTime.toLocalDateTime();
            LocalDateTime ldt = pldt.plusDays(30);
            Timestamp endSpamTime = Timestamp.valueOf(ldt);
//            System.out.println(userSpamTime);
//            System.out.println(endSpamTime);
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp currentTime = new Timestamp(currentTimeMillis);
//            System.out.println(currentTime);
            if (!endSpamTime.after(currentTime)) {
                user.setBronCount(0);
                userRepo.save(user);
            }
        }
    }
}
