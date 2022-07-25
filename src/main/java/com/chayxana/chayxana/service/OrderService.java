package com.chayxana.chayxana.service;

import com.chayxana.chayxana.entity.*;
import com.chayxana.chayxana.entity.enums.StatusName;
import com.chayxana.chayxana.exceptions.PageSizeException;
import com.chayxana.chayxana.payload.*;
import com.chayxana.chayxana.repo.*;
import com.chayxana.chayxana.utills.CommandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    Timer timer;

    @Autowired
    DeviceDataRepo deviceDataRepo;

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoomRepo roomRepo;

    @Autowired
    StatusRepo statusRepo;

    @Autowired
    MapperDTO mapperDTO;

    @Value("${url.for.main.image.of.chayxana}")
    private String urlForImage;

    public ApiResponse create(OrderDto orderDto) throws Exception {

        Order order = new Order();
        Optional<User> optionalUser = userRepo.findById(orderDto.getUserDto().getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            order.setUser(user);
            Optional<Room> optionalRoom = roomRepo.findById(orderDto.getRoomDto().getId());
            if (optionalRoom.isPresent()) {
                Room room = optionalRoom.get();
                order.setPrice(room.getPrice());
                order.setRoom(room);
                room.setBron(true);
                roomRepo.save(room);
                Optional<Status> optionalStatus = statusRepo.findByStatusName(StatusName.NEW);
                if (optionalStatus.isPresent()) {
                    Status status = optionalStatus.get();
                    order.setStatus(status);
                    order.setOrderTime(new Timestamp(orderDto.getOrderTime().getTime()));
                    order.setPersonAmount(orderDto.getPersonAmount());
                    order.setMessage(orderDto.getMessage());
                    order = orderRepo.save(order);
//                    schedule(sendOneDayLeft(order.getId()),order.getId());
                    return new ApiResponse(true, "Order saved", order.getId());
                }
                return new ApiResponse(false, "Not found status");
            }
            return new ApiResponse(false, "Not found room");
        }
        return new ApiResponse(false, "Not found user");
    }


//    public Page<Order> getByChayxanaId(UUID chayxanaId, int offset, int pageSize) {
//       Pageable pageable = PageRequest.of(offset,pageSize).withSort(Sort.by("orderTime"));
//        return orderRepo.findAllByRoom_ChayxanaId(chayxanaId, pageable);
//    }

    public ApiResponse getByChayxanaId(UUID chayxanaId, int page, int size) throws PageSizeException {
        Page<Order> orders = orderRepo.findAllByRoom_ChayxanaIdOrderByOrderTimeAsc(chayxanaId, CommandUtils.simplePageable(page - 1, size));
        List<Order> content = orders.getContent();
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (Order order : content) {
            OrderDto dto = mapperDTO.generateOrderDtoFromOrder(order);
            orderDtoList.add(dto);
        }
        return new ApiResponse(true, "Succesfully get", orderDtoList);
    }

    public ApiResponse nexStatus(UUID id) {
        Optional<Order> optionalOrder = orderRepo.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            Optional<Status> optionalStatus = statusRepo.findByStatusName(StatusName.OLD);
            if (optionalStatus.isPresent()) {
                Status status = optionalStatus.get();
                order.setStatus(status);
                orderRepo.save(order);
                return new ApiResponse(true, "Successfully change status", order.getId());
            }
            return new ApiResponse(false, "Not found status");
        }
        return new ApiResponse(false, "Not found order");
    }

    public ApiResponse getAllOrdersByUserId(UUID id) {
        Optional<User> userOptional = userRepo.findById(id);
        List<OrderDto> orderDtoList = new ArrayList<>();
        if (userOptional.isEmpty()) {
            return new ApiResponse(false, "User not found");
        } else {
            List<Order> orders = orderRepo.findAllByUserId(userOptional.get().getId());
            for (Order order : orders) {
                OrderDto orderDto = new OrderDto(
                        order.getId(),
                        order.getOrderTime(),
                        order.getPersonAmount(),
                        order.getPrice(),
                        order.getStatus().getStatusName().toString(),
                        order.getMessage()
                );

                AddressDto addressDto = mapperDTO.generateAddresDtoFromAddres(order.getRoom().getChayxana().getAddress());
                ChayxanaDto chayxanaDto = new ChayxanaDto(
                        order.getRoom().getChayxana().getId(),
                        addressDto,
                        order.getRoom().getChayxana().getPhoneNumber(),
                        order.getRoom().getChayxana().getName()
                );
                RoomDto roomDto = new RoomDto(
                        order.getRoom().getId(),
                        order.getRoom().getRoomNumber(),
                        chayxanaDto);
                orderDto.setRoomDto(roomDto);
                orderDtoList.add(orderDto);
            }
            return new ApiResponse(true, "All orders", orderDtoList);
        }


    }

    /**
     * Change order status (DONE or CANCEL)
     *
     * @param doneOrCancel boolean
     * @param id           UUID
     * @return ApiResponse
     * If such an order does not exist we return false at ApiResponse else true at ApiResponse
     */
    public ApiResponse changeOrderStatus(boolean doneOrCancel, UUID id) {Optional<Order> optionalOrder = orderRepo.findById(id);
        if (optionalOrder.isEmpty()) {
            return new ApiResponse(false, "This order does not exist");
        }
        Order order = optionalOrder.get();
        Optional<Status> optionalStatus;
        if (doneOrCancel) {
            optionalStatus = statusRepo.findByStatusName(StatusName.OLD);
            UUID userId = order.getUser().getId();
            List<DeviceData> deviceDataList = deviceDataRepo.findAllByUserIdAndActiveTrue(userId);
            for (DeviceData deviceData : deviceDataList) {
                String title = "Chayxana";
                String body = "Sizning " +
                        order.getOrderTime() + " soatdagi buyurtmangiz qabul qilindi.";
                notificationService.sendNotification(title, body, deviceData, urlForImage + order.getRoom().getChayxana().getId());
            }
        } else {
            optionalStatus = statusRepo.findByStatusName(StatusName.DELETE);
            UUID userId = order.getUser().getId();
            List<DeviceData> deviceDataList = deviceDataRepo.findAllByUserIdAndActiveTrue(userId);
            for (DeviceData deviceData : deviceDataList) {
                String title = "Chayxana";
                String body = "Sizning " +
                        order.getOrderTime() + " soatdagi buyurtmangiz qabul qilinmadi.";
                notificationService.sendNotification(title, body, deviceData, urlForImage + order.getRoom().getChayxana().getId());
            }
        }
        optionalStatus.ifPresent(order::setStatus);
        orderRepo.save(order);
        return new ApiResponse(true, "Successfully changed");
    }

    public ApiResponse changeNewStatus(UUID id) {
        Optional<Order> optionalOrder = orderRepo.findById(id);
        if (optionalOrder.isEmpty()) {
            return new ApiResponse(false, "This order does not exist");
        }
        Order order = optionalOrder.get();
        Optional<Status> optionalStatus;
        optionalStatus = statusRepo.findByStatusName(StatusName.DELETE);
        optionalStatus.ifPresent(order::setStatus);
        orderRepo.save(order);
        return new ApiResponse(true, "Successfully changed");
    }

    private void schedule(TimerTask task, UUID orderId) throws Exception {
        Optional<Order> orderOptional = orderRepo.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            Date input = new Date(order.getOrderTime().getTime());
            LocalDateTime ldt = LocalDateTime.ofInstant(input.toInstant(), ZoneId.systemDefault());
            ldt = ldt.minusHours(29);
            Timestamp timestamp = Timestamp.valueOf(ldt);
            timer.schedule(task, new Date(timestamp.getTime()));
        } else {
            throw new Exception("Bunday order yo'q");
        }
    }

    private TimerTask sendOneDayLeft(UUID orderId) {
        return new TimerTask() {
            @Override
            public void run() {
                String body = null;
                DeviceData deviceData = null;
                Order order = null;
                Optional<Order> orderOptional = orderRepo.findById(orderId);
                if (orderOptional.isPresent()) {
                    order = orderOptional.get();
                    String name = order.getRoom().getChayxana().getName();
                    int roomNumber = order.getRoom().getRoomNumber();
                    int hour = order.getOrderTime().toLocalDateTime().getHour();
                    body = "Ertaga " + name + " choyxonasida " + roomNumber + "-xonada soat " + hour + " da choyxona !!!";
                    if (deviceDataRepo.findByUserId(order.getUser().getId()).isPresent()) {
                        deviceData = deviceDataRepo.findByUserId(order.getUser().getId()).get();

                    }
                }
                notificationService.sendNotification("Chayxana", body, deviceData, urlForImage + order.getRoom().getChayxana().getId());
            }
        };
    }
}