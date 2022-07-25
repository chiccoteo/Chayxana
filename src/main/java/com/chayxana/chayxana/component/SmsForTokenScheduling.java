package com.chayxana.chayxana.component;

import com.chayxana.chayxana.entity.SmsForToken;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.repo.SmsForApiRepo;
import com.chayxana.chayxana.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SmsForTokenScheduling {

    private final SmsForApiRepo smsForApiRepo;

    private final SmsService smsService;

    @Scheduled(fixedDelay = 60*60*5000, initialDelay = 1000*60*60)
    public void scheduleFixedDelayTask() {

        List<SmsForToken> all = smsForApiRepo.findAll();
        if (all.size()==0){
            SmsForToken smsForToken=new SmsForToken();
            ApiResponse apiResponse = smsService.addSmsApi(smsForToken);
            smsForToken = (SmsForToken) apiResponse.getObject();
            SmsForToken save = smsForApiRepo.save(smsForToken);
            System.out.println(save+": if" );
        }else {
            SmsForToken smsForToken = smsService.getSmsApi();
            SmsForToken newSmsForToken=new SmsForToken();
            newSmsForToken.setEmail(smsForToken.getEmail());
            newSmsForToken.setPassword(smsForToken.getPassword());
            newSmsForToken.setUrl(smsForToken.getUrl());
            smsService.addSmsApi(newSmsForToken);
            SmsForToken save = smsForApiRepo.save(newSmsForToken);
            System.out.println(save+": else");
        }


    }
}
