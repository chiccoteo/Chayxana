package com.chayxana.chayxana.service;

import com.chayxana.chayxana.entity.*;
import com.chayxana.chayxana.exceptions.PageSizeException;
import com.chayxana.chayxana.payload.*;
import com.chayxana.chayxana.repo.*;
import com.chayxana.chayxana.utills.CommandUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChayxanaService {

    private final MapperDTO mapper;

    private final ChayxanaRepo chayxanaRepo;

    private final AddressService addressService;

    private final UserService userService;

    private final NotificationService notificationService;

    private final RoomRepo roomRepo;

    private final OrderRepo orderRepo;

    private final ChayxanaDetailRepo chayxanaDetailRepo;

    private final ChayxanaImageRepo chayxanaImageRepo;

    private final DeviceDataRepo deviceDataRepo;

    @Value("${url.for.main.image.of.chayxana}")
    private String urlForImage;


    public ApiResponse createChayxana(ChayxanaDto chayxanaDto) {

        Chayxana chayxana = mapper.generateChayxanaFromChayxanaDto(chayxanaDto);
        Address address = addressService.getAddressFromAddressDto(chayxanaDto.getAddressDto());
        User user = userService.getUserById(chayxanaDto.getUserId());
        if (chayxana == null || address == null || user == null) {
            return new ApiResponse(false, "Not saved");

        }
        Time startTime = stringToTime(chayxanaDto.getStartTime());
        Time endTime = stringToTime(chayxanaDto.getEndTime());
        chayxana.setStartTime(startTime);
        chayxana.setEndTime(endTime);
        chayxana.setUser(user);
        chayxana.setAddress(address);
        List<Long> lists = chayxanaDto.getChayxanaDetailDtos()
                .stream()
                .map(ChayxanaDetailDto::getId)
                .collect(Collectors.toList());
        List<ChayxanaDetail> allChayxanaDetailByChayxanaDetailsId = chayxanaDetailRepo.findAllById(lists);
        chayxana.setChayxanaDetails(allChayxanaDetailByChayxanaDetailsId);
        Chayxana save = chayxanaRepo.save(chayxana);
        ChayxanaDto chayxanaDto1 = generateOneChayxana(save);
        if (sendNotification(save))
            return new ApiResponse(true, "Successfully saved, sendNotification has been sent to all users.", chayxanaDto1);
        return new ApiResponse(true, "Successfully saved, Error has been occurred during sending notification to users.", chayxanaDto1);
    }

    private boolean sendNotification(Chayxana chayxana) {
        boolean send;
        try {
            String title = "\" !! BIZ OCHILDIK !!\"";
            String message = "Yangi " + chayxana.getName() + " nomli Chayxana ochildi. Oshga kelaverasizlar!. " +
                    "\n Murojat uchun: " + chayxana.getPhoneNumber() + " qo'ng'iroq qilishingiz mumkin!";
            List<DeviceData> deviceDataList = deviceDataRepo.findAllByActiveTrue();
            for (DeviceData deviceData : deviceDataList) {
                notificationService.sendNotification(title, message, deviceData, urlForImage + chayxana.getId());
            }
            send = true;
        } catch (Exception e) {
            send = false;
        }
        return send;
    }
    public ApiResponse getAllChayxana() {
        List<Chayxana> chayxanaList = chayxanaRepo.findAll();
        if (chayxanaList.isEmpty()) {
            return new ApiResponse(false, "chayxana not");
        }
        List<ChayxanaDto> chayxanaDtoList = new ArrayList<>();
        for (Chayxana chayxana : chayxanaList) {
            if (!chayxana.isDelete()) {
                ChayxanaDto chayxanaDto = generateAndAddLastMonth(chayxana);
                chayxanaDtoList.add(chayxanaDto);
            }
        }

        return new ApiResponse(true, "Successfully send", chayxanaDtoList);
    }


    public ApiResponse getById(UUID chayxanaId) {

        Optional<Chayxana> byId = chayxanaRepo.findById(chayxanaId);
        if (byId.isPresent()) {
            Chayxana chayxana = byId.get();
            ChayxanaDto chayxanaDto = generateOneChayxana(chayxana);
            int roomsNum = roomRepo.getRoomNumberByChayxanaId(chayxanaId);
            chayxanaDto.setRoomNumber(roomsNum);

            return new ApiResponse(true, "Successfully send", chayxanaDto);
        }
        return new ApiResponse(false, "Not Found by this ID");
    }


    private ChayxanaDto generateOneChayxana(Chayxana chayxana) {

        ChayxanaDto chayxanaDto = mapper.generateChayxanaDtoFromChayxana(chayxana);
        AddressDto addressDto = mapper.generateAddresDtoFromAddres(chayxana.getAddress());
        List<ChayxanaDetailDto> chayxanaDetailDtos = mapper.genereteChayxanaDetailDtoListFromChDetail(chayxana.getChayxanaDetails());
        chayxanaDto.setChayxanaId(chayxana.getId());

        chayxanaDto.setDescriptionUz(chayxana.getDescriptionUz());
        chayxanaDto.setDescriptionEn(chayxana.getDescriptionEn());
        chayxanaDto.setDescriptionRu(chayxana.getDescriptionRu());
        chayxanaDto.setAddressDto(addressDto);
        chayxanaDto.setChayxanaDetailDtos(chayxanaDetailDtos);
        return chayxanaDto;
    }


    public ApiResponse getAllByUserId(UUID userId) {

        List<Chayxana> chayxanaList = chayxanaRepo.findAllByUserId(userId);
        if (!chayxanaList.isEmpty()) {
            List<ChayxanaDto> chayxanaDtoList = new ArrayList<>();
            for (Chayxana chayxana : chayxanaList) {
                if (!chayxana.isDelete()) {
                    ChayxanaDto chayxanaDto = generateAndAddLastMonth(chayxana);
                    chayxanaDtoList.add(chayxanaDto);
                }
            }
            return new ApiResponse(true, "Successfully send", chayxanaDtoList);

        }
        return new ApiResponse(false, "This user has no chayxana yet");
    }


    public ApiResponse deleteChayxana(UUID chayxanaId) {
        Optional<Chayxana> byId = chayxanaRepo.findById(chayxanaId);
        if (byId.isPresent()) {
            Chayxana chayxana = byId.get();
            chayxana.setDelete(true);
            chayxanaRepo.save(chayxana);
            return new ApiResponse(true, "Successfully deleted");
        }
        return new ApiResponse(false, "Not Found Chayxana this ID");
    }
    public ApiResponse editChayxana(UUID chayxanaId, ChayxanaDto chayxanaDto) {
        if (chayxanaId != null) {
            Optional<Chayxana> byId = chayxanaRepo.findById(chayxanaId);
            if (byId.isPresent()) {
                Address address = addressService.getAddressFromAddressDto(chayxanaDto.getAddressDto());
                if (address != null) {
                    Time startTime = stringToTime(chayxanaDto.getStartTime());
                    Time endTime = stringToTime(chayxanaDto.getEndTime());
                    Chayxana chayxana = byId.get();
                    chayxana.setName(chayxanaDto.getChayxanaName());
                    chayxana.setAddress(address);
                    chayxana.setStartTime(startTime);
                    chayxana.setEndTime(endTime);
                    List<Long> lists = chayxanaDto.getChayxanaDetailDtos()
                            .stream()
                            .map(ChayxanaDetailDto::getId)
                            .collect(Collectors.toList());
                    List<ChayxanaDetail> allChayxanaDetailByChayxanaDetailsId = chayxanaDetailRepo.findAllById(lists);
                    chayxana.setChayxanaDetails(allChayxanaDetailByChayxanaDetailsId);
                    chayxana.setDescriptionEn(chayxanaDto.getDescriptionEn());
                    chayxana.setDescriptionUz(chayxanaDto.getDescriptionUz());
                    chayxana.setDescriptionRu(chayxanaDto.getDescriptionRu());
                    Chayxana editedChayxana = chayxanaRepo.save(chayxana);
                    ChayxanaDto chayxanaDto1 = generateOneChayxana(editedChayxana);

                    return new ApiResponse(true, "Successfully edited", chayxanaDto1);
                }
                return new ApiResponse(false, "Address Mustn't empty");
            }
            return new ApiResponse(false, "Not edited");
        }
        return new ApiResponse(false, "Id Mustn't empty");
    }


    private Time stringToTime(String time) {
        Time sendtime;
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        try {
            sendtime = new Time(formatter.parse(time).getTime());
        } catch (ParseException e) {
            return null;
        }
        return sendtime;
    }


    public ApiResponse getAllPageble(Integer page, Integer size) {
        List<ChayxanaDto> chayxanaDtoList;
        try {
            Page<Chayxana> chayxanaPage = chayxanaRepo.findAllByDelete(CommandUtils.simplePageable(page - 1, size));
            //      Page<Chayxana> chayxanaPage = chayxanaRepo.findAll(CommandUtils.simplePageable(page - 1, size));
            chayxanaDtoList = chayxanaPage
                    .stream()
                    .map(this::generateAndAddLastMonth)
                    .collect(Collectors.toList());

            return new ApiResponse(true, "All Products", chayxanaDtoList, chayxanaPage.getTotalElements());
        } catch (PageSizeException e) {
            e.printStackTrace();
            return new ApiResponse(false, "Error");
        }
    }


    private ChayxanaDto generateAndAddLastMonth(Chayxana chayxana) {
        List<LastOneMonthDto> lastOneMonthOrdersSumList = orderRepo.getSumOrdersLastOneMonth();
        ChayxanaDto chayxanaDto = mapper.generateChayxanaDtoFromChayxana(chayxana);

        Optional<LastOneMonthDto> matchingObject = lastOneMonthOrdersSumList.stream()
                .filter(i -> i.getId().equals(chayxana.getId())).
                findFirst();
        matchingObject.ifPresent(queryReturns -> chayxanaDto.setPrice(queryReturns.getPrice()));
        return chayxanaDto;
    }


    public ApiResponse getAllChayxanaForClient(Integer page, Integer size) {
        List<ChayxanaDto> chayxanaDtoList;
        try {
            Page<Chayxana> chayxanaPage = chayxanaRepo.findAllforClient(CommandUtils.simplePageable(page - 1, size));
            chayxanaDtoList = chayxanaPage
                    .stream()
                    .map(this::generateAndAddChDetail)
                    .collect(Collectors.toList());
            return new ApiResponse(true, "All Products", chayxanaDtoList, chayxanaPage.getTotalElements());
        } catch (PageSizeException e) {
            e.printStackTrace();
            return new ApiResponse(false, "Error");
        }


    }


    private ChayxanaDto generateAndAddChDetail(Chayxana chayxana) {
        ChayxanaDto chayxanaDto = mapper.generateChayxanaDtoFromChayxana(chayxana);
        chayxanaDto.setChayxanaDetailDtos(mapper.genereteChayxanaDetailDtoListFromChDetail(chayxana.getChayxanaDetails()));
        return chayxanaDto;
    }


    public ApiResponse getAllOrdersFromChayxana(UUID chayxanaId, String name) {

        boolean existsById = chayxanaRepo.existsById(chayxanaId);
        if (existsById) {
            name = name.toUpperCase(Locale.ROOT);
            List<LastSixMonthResultsDto> resultsDtos;
            switch (name) {
                case "DAY":
                    resultsDtos = orderRepo.getLastMonthByDayCount(chayxanaId);
                    break;
                case "WEEK":
                    resultsDtos = orderRepo.getLastMonthByWeekCount(chayxanaId);
                    break;
                case "MONTH":
                    resultsDtos = orderRepo.getLastMonthByMonthCount(chayxanaId);
                    break;
                default:
                    return new ApiResponse(false, "Error in name");

            }

//            Map<LocalDate, Integer> integerMap = new HashMap<>();
//
//            for (LastSixMonthResultsDto l : resultsDtos) {
//                assert false;
//                integerMap.put(l.getTimestamps().toLocalDateTime().toLocalDate(),
//                        l.getCounts());
//
//            }
            Collections.reverse(resultsDtos);
            return new ApiResponse(true, "Sended", resultsDtos);
        }
        return new ApiResponse(false, "This chayxana doesn't exist");

    }


    public ApiResponse getbyLonLat(double lon, double lat) {
        List<Chayxana> chayxanaList = chayxanaRepo.getALlByLonAndLat(lon, lat);
        List<ChayxanaDto> chayxanaDtoList = new ArrayList<>();
        for (Chayxana chayxana : chayxanaList) {
            if (!chayxana.isDelete()) {
                ChayxanaDto chayxanaDto = generateAndAddChDetail(chayxana);
                chayxanaDtoList.add(chayxanaDto);
            }
        }

        return new ApiResponse(true, "Successfully send", chayxanaDtoList);
    }

    public ApiResponse editIsActive(UUID id, boolean isActive) {
        Optional<Chayxana> byId = chayxanaRepo.findById(id);
        if (byId.isPresent()) {
            Chayxana chayxana = byId.get();
            if (isActive) {
                if (!chayxana.isActive()) {
                    chayxana.setActive(true);
                    chayxanaRepo.save(chayxana);
                    return new ApiResponse(true, "Edited");
                }
            } else {
                if (chayxana.isActive()) {
                    chayxana.setActive(false);
                    chayxanaRepo.save(chayxana);
                    return new ApiResponse(true, "Edited");
                }
            }

            return new ApiResponse(false, "Already changed");

        } else return new ApiResponse(false, "this chayxana didn't exist");

    }

    public ApiResponse getAllChayxanaByName(String name) {
        List<SearchChayxanaDto> allByNameContaining = chayxanaRepo.findAllByNameContaining(name);
        if (allByNameContaining.isEmpty()) {
            return new ApiResponse(false, "this chayxana not found");
        }
        return new ApiResponse(true, "found", allByNameContaining);
    }

    public ApiResponse getMainPictureOfChayxana(UUID id, HttpServletResponse response) throws IOException {
        Optional<Chayxana> optionalChayxana = chayxanaRepo.findById(id);
        if (optionalChayxana.isEmpty()) {
            return new ApiResponse(false, "Chayxana Not Found!");
        }
        List<ChayxanaImage> chayxanaImages = chayxanaImageRepo.findChayxanaImagesByChayxanasId(id);
        for (ChayxanaImage chayxanaImage : chayxanaImages) {
            if (chayxanaImage.isMainImage()) {
                response.setHeader("Content-Disposition",
                        "attachment; filename=\""
                                + chayxanaImage.getOriginalName() + "\"");

                response.setContentType(chayxanaImage.getContentType());

                FileInputStream inputStream = new FileInputStream(chayxanaImage.getImageUrl());
                FileCopyUtils.copy(inputStream, response.getOutputStream());
                return new ApiResponse(true, "Success");
            }
        }
        return new ApiResponse(false, "Not found Main Image of Chayxana!");
    }


    public ApiResponse getAmountOfPicturesOfChayxana(UUID id) {
        Optional<Chayxana> optionalChayxana = chayxanaRepo.findById(id);
        if (optionalChayxana.isEmpty()) {
            return new ApiResponse(false, "Chayxana Not Found!");
        }
        List<ChayxanaImage> imageList = chayxanaImageRepo.findChayxanaImagesByChayxanasId(id);
        return new ApiResponse(true, "Success", imageList.size());
    }

    public List<ChayxanaDto> getChayxanaDetailsByID(List<Long> ids) {
        List<Chayxana> chayxanaList = new ArrayList<>();
        List<Chayxana> chayxanas = chayxanaRepo.findAll();
        boolean check = true;
        for (Chayxana c : chayxanas) {
            for (Long detailId : ids) {
                if (c.getChayxanaDetails().stream().noneMatch(s -> s.getId().equals(detailId))) {
                    check = false;
                    break;
                }

            }
            if (check) {
                chayxanaList.add(c);
            }
            check = true;
        }
        List<ChayxanaDto> chayxanaDtoList = chayxanaList
                .stream()
                .map(this::generateAndAddChDetail)
                .collect(Collectors.toList());

        return chayxanaDtoList;
    }


    public List<ChayxanaDto> getChayxanasByRoomDetails(List<Long> ids) {
        List<Room> roomList = new ArrayList<>();
        List<Room> rooms = roomRepo.findAll();
        Set<Chayxana> chayxanas = new HashSet<>();
        boolean check = true;
        for (Room room : rooms) {
            for (Long id : ids) {
                if (room.getRoomDetails().stream().noneMatch(r -> r.getId().equals(id))) {
                    check = false;
                    break;
                }
            }
            if (check) {
                roomList.add(room);
            }
            check = true;
        }

        if (roomList.isEmpty()) {
            return new ArrayList<>();
        }
        for (Room room : roomList) {
            chayxanas.add(room.getChayxana());
        }
        List<ChayxanaDto> chayxanaDtoList=new ArrayList<>();
        for (Chayxana chayxana : chayxanas) {
            ChayxanaDto chayxanaDto = mapper.generateChayxanaDtoFromChayxana(chayxana);
            chayxanaDtoList.add(chayxanaDto);
        }
        return chayxanaDtoList;

    }

    public ApiResponse getChayxanaByDetailList(DetailListDto detailListDto) {
        List<ChayxanaDto> chayxanaDetailsByID = getChayxanaDetailsByID(detailListDto.getChayxanaDetails());
        List<ChayxanaDto> chayxanasByRoomDetails = getChayxanasByRoomDetails(detailListDto.getRoomDetails());
        chayxanaDetailsByID.retainAll(chayxanasByRoomDetails);

//        List<ChayxanaDto>

        return new ApiResponse(true, "succesfully", chayxanaDetailsByID);
    }
}
