package com.chayxana.chayxana.component;

import com.chayxana.chayxana.entity.ChayxanaImage;
import com.chayxana.chayxana.entity.DeviceData;
import com.chayxana.chayxana.entity.Order;
import com.chayxana.chayxana.entity.enums.StatusName;
import com.chayxana.chayxana.repo.ChayxanaImageRepo;
import com.chayxana.chayxana.repo.DeviceDataRepo;
import com.chayxana.chayxana.repo.OrderRepo;
import com.chayxana.chayxana.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class NotificationSchedule {

    private final DeviceDataRepo deviceDataRepo;

    private final OrderRepo orderRepo;

    private final NotificationService notificationService;

    private final ChayxanaImageRepo chayxanaImageRepo;

    @Value("${url.for.main.image.of.chayxana}")
    private String urlForImage;


    /**
     * millisecond da
     * 86400000L ==>  bir kun
     * initialDelay ==> bu birinchi programma run bo'lganda 3 sekunddan keyin ishlidi
     * ikkinchi marttasida esa fixedDelay ==> har shu fixedDelay vaqtida taskni bajaradi.
     */
    @Scheduled(fixedDelay = 3_600_000L, initialDelay = 3_000L)
    public void notificationForClient() {
        List<Order> statusNameOld = orderRepo.findAllByStatusStatusName(StatusName.OLD);
        if (statusNameOld.isEmpty()) {
            return;
        }
        for (Order order : statusNameOld) {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if (order.getOrderTime().compareTo(currentTime) >= -60
                    &&
                    order.getOrderTime().compareTo(currentTime) < -120) {
                UUID chayxanachiId = order.getRoom().getChayxana().getUser().getId();
                List<DeviceData> deviceDataList = deviceDataRepo.findAllByUserIdAndActiveTrue(chayxanachiId);
                for (DeviceData deviceData : deviceDataList) {
                    String title = order.getId() + "idlik orderga 2 soatdan kam qoldi";
                    String body = order.getRoom() + "raqamli xonaga " +
                            order.getPersonAmount() + " kishi " +
                            order.getOrderTime() + "vaqtda kelishida.";
                    notificationService.sendNotification(title,
                            body,
                            deviceData,
                            urlForImage + order.getRoom().getChayxana().getId());
                }
            }
        }
    }
}