package com.chayxana.chayxana.service;

import com.chayxana.chayxana.entity.*;
import com.chayxana.chayxana.payload.*;
import com.chayxana.chayxana.repo.*;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class MapperDTO {

    private final DistrictRepo districtRepo;

    private final RegionRepo regionRepo;

    private final RoomRepo roomRepo;
    private final RoomDetailRepo roomDetailRepo;

    private final ChayxanaRepo chayxanaRepo;

    private final IconRepo iconRepo;

    private final RoleRepo roleRepo;

    public ImageDTO generateImageToImageDTO(ChayxanaImage image) throws IOException {
        File file = new File(image.getImageUrl());
//        byte[] bytes = Files.readAllBytes(file.toPath());
        ImageDTO dto = new ImageDTO(image.getId(), image.getOriginalName(), image.getName(), image.getSize(), image.getContentType(), image.isMainImage());
        return dto;
    }

    public RegionDto generateRegionDtoFromRegion(Region region) {
        return new RegionDto(region.getId(), region.getName());
    }

    public DistrictDto generateDistrictDtoFromDistrict(District district) {
        DistrictDto districtDto = new DistrictDto();
        districtDto.setId(district.getId());
        districtDto.setRegionDto(generateRegionDtoFromRegion(district.getRegion()));
        districtDto.setName(district.getName());
        return districtDto;
    }

    public District generateDistrictFromDistrictDto(DistrictDto districtDto) {
        District district = new District();
        district.setName(districtDto.getName());
        district.setRegion(regionRepo.getById(districtDto.getRegionDto().getId()));
        return district;
    }

    public Region generateRegionFromRegionDto(RegionDto regionDto) {
        Region region = new Region();
        region.setName(regionDto.getName());
        return region;
    }

    public ChayxanaDetail generateChayxanaDetailFromChayxanaDetailDto(ChayxanaDetailDto chayxanaDetailDto) {

        ChayxanaDetail chayxanaDetail = new ChayxanaDetail();
        chayxanaDetail.setName(chayxanaDetailDto.getName());
        chayxanaDetail.setActive(chayxanaDetailDto.isActive());
        Optional<Icon> optionalIcon = iconRepo.findById(chayxanaDetailDto.getIconMultiSendDto().getId());
        if (optionalIcon.isEmpty())
            return null;
        chayxanaDetail.setIcon(optionalIcon.get());
        return chayxanaDetail;
    }

    @SneakyThrows
    public ChayxanaDetailDto generateChayxanaDetailDtoFromChayxanaDetail(ChayxanaDetail chayxanaDetail) {
        Icon icon = chayxanaDetail.getIcon();
//        String iconUrl = icon.getIconUrl();
//        Path path = Paths.get(iconUrl + icon.getName());
//        byte[] data = Files.readAllBytes(path);
        return ChayxanaDetailDto.builder()
                .id(chayxanaDetail.getId())
                .name(chayxanaDetail.getName())
                .active(chayxanaDetail.isActive())
                .iconMultiSendDto(new IconMultiSendDto(icon.getId(), icon.getFileOriginalName(), icon.getSize(), icon.getContentType(), icon.getName()))
                .build();
    }

    public Address generateAddressFromAddressDto(AddressDto addressDto) {
        Address address = new Address();
        Region region = new Region(addressDto.getDistrictDto().getRegionDto().getName());
        District district = new District(addressDto.getDistrictDto().getName(), region);
        boolean reg = regionRepo.existsByName(region.getName());
        boolean dis = districtRepo.existsByName(district.getName());
        Optional<District> districtbyName = districtRepo.findByName(district.getName());

        if (districtbyName.isEmpty()) {
            Optional<Region> repoByName = regionRepo.findByName(region.getName());
            if (repoByName.isEmpty()) {
                region = regionRepo.save(region);
            } else {
                region = repoByName.get();
            }
            district.setRegion(region);
            district = districtRepo.save(district);
        } else {
            district = districtbyName.get();
        }

//        if(!dis){
//            if(!reg){
//                regionRepo.save(region);
//            } else {
//                district.setRegion(regionRepo.getByName(region.getName()));
//            }
//            district = districtRepo.save(district);
//        }
//        else {
//            district = districtRepo.getByName(district.getName());
//        }

        address.setDistrict(district);
        address.setStreetName(addressDto.getStreetName());
        address.setLan(addressDto.getLan());
        address.setLat(addressDto.getLat());
        return address;


    }

    public AddressDto generateAddresDtoFromAddres(Address address) {
        AddressDto addressDto = new AddressDto();
        DistrictDto districtDto = generateDistrictDtoFromDistrict(address.getDistrict());
        RegionDto regionDto = generateRegionDtoFromRegion(address.getDistrict().getRegion());
        districtDto.setRegionDto(regionDto);
        addressDto.setDistrictDto(districtDto);
        addressDto.setLan(address.getLan());
        addressDto.setLat(address.getLat());
        addressDto.setStreetName(address.getStreetName());

        return addressDto;
    }

    public ChayxanaDto generateChayxanaDtoFromChayxana(Chayxana chayxana) {

        AddressDto addressDto = new AddressDto();
        Address address = chayxana.getAddress();
        District district = address.getDistrict();
        addressDto.setDistrictDto(new DistrictDto(district.getName()));
        addressDto.setStreetName(address.getStreetName());
        addressDto.setLan(address.getLan());
        addressDto.setLat(address.getLat());
        String startTime = chayxana.getStartTime().toString();
        String endTime = chayxana.getEndTime().toString();


        ChayxanaDto chayxanaDto = new ChayxanaDto();
        chayxanaDto.setChayxanaName(chayxana.getName());
        chayxanaDto.setPhoneNumber(chayxana.getPhoneNumber());
        chayxanaDto.setAddressDto(addressDto);
        chayxanaDto.setStartTime(startTime);
        chayxanaDto.setUserId(chayxana.getUser().getId());
        chayxanaDto.setEndTime(endTime);
        chayxanaDto.setChayxanaId(chayxana.getId());
        chayxanaDto.setActive(chayxana.isActive());
        return chayxanaDto;

    }

    public Chayxana generateChayxanaFromChayxanaDto(ChayxanaDto chayxanaDto) {

        return Chayxana.builder()
                .active(true)
                .descriptionEn(chayxanaDto.getDescriptionEn())
                .descriptionRu(chayxanaDto.getDescriptionRu())
                .descriptionUz(chayxanaDto.getDescriptionUz())
                .name(chayxanaDto.getChayxanaName())
                .phoneNumber(chayxanaDto.getPhoneNumber())
                .isDelete(false)
                .build();
    }

    public Room generateRoomDtoToRoom(RoomDto dto) throws Exception {
        Room room = new Room();
        if (dto.getId() != null) {
            room = roomRepo.findById(dto.getId()).orElseThrow();
        }
        room.setRoomNumber(dto.getRoomNumber());
        room.setBron(dto.isBron());
        room.setMaxPerson(dto.getMaxPerson());
        room.setMinPerson(dto.getMinPerson());
        room.setPrice(dto.getPrice());
        Optional<Chayxana> chayxanaRepoById = chayxanaRepo.findById(dto.getChayxanaDto().getChayxanaId());
        if (chayxanaRepoById.isPresent()) {
            room.setChayxana(chayxanaRepoById.get());
        } else {
            room.setChayxana(new Chayxana());
        }
        List<RoomDetail> roomDetails = new ArrayList<>();
        for (RoomDetailDTO roomDetailDTO : dto.getRoomDetailDTOS()) {
            Optional<RoomDetail> roomDetailOptional = roomDetailRepo.findById(roomDetailDTO.getId());
            roomDetailOptional.ifPresent(roomDetails::add);
            if (roomDetailOptional.isEmpty()) {
                throw new Exception("Roomdetail not found");
            }
        }
        room.setRoomDetails(roomDetails);
        return room;
    }

    public RoomDto generateRoomToRoomDto(Room room) {
        ChayxanaDto chayxanaDto = new ChayxanaDto();
        chayxanaDto.setChayxanaId(room.getChayxana().getId());
        RoomDto roomDto = new RoomDto();
        roomDto.setId(room.getId());
        roomDto.setRoomNumber(room.getRoomNumber());
        roomDto.setBron(room.isBron());
        roomDto.setMaxPerson(room.getMaxPerson());
        roomDto.setMinPerson(room.getMinPerson());
        roomDto.setChayxanaDto(chayxanaDto);
        roomDto.setPrice(room.getPrice());
        List<RoomDetailDTO> roomDetailDTOS = new ArrayList<>();
        for (RoomDetail roomDetail : room.getRoomDetails()) {
            RoomDetailDTO roomDetailDTO = new RoomDetailDTO();
            roomDetailDTO.setId(roomDetail.getId());
            roomDetailDTO.setName(roomDetail.getName());
            roomDetailDTO.setIconId(roomDetail.getIcon().getId());
            roomDetailDTOS.add(roomDetailDTO);
        }
        roomDto.setRoomDetailDTOS(roomDetailDTOS);
        return roomDto;
    }

    @SneakyThrows
    public RoomDto generateRoomDtoFromRoomAndRoomDetail(Room room, List<RoomDetail> roomDetails) {
        List<RoomDetailDTO> roomDetailDTOS = new ArrayList<>();
        for (RoomDetail roomDetail : roomDetails) {
            RoomDetailDTO roomDetailDTO = new RoomDetailDTO();
            roomDetailDTO.setName(roomDetail.getName());
            String iconUrl = roomDetail.getIcon().getIconUrl();
            Path path = Paths.get(iconUrl + roomDetail.getIcon().getName());
            byte[] data = Files.readAllBytes(path);
            roomDetailDTOS.add(roomDetailDTO);
        }
        RoomDto roomDto = new RoomDto();
        roomDto.setId(room.getId());
        roomDto.setBron(room.isBron());
        roomDto.setRoomNumber(room.getRoomNumber());
        roomDto.setPrice(room.getPrice());
        roomDto.setMinPerson(room.getMinPerson());
        roomDto.setMaxPerson(room.getMaxPerson());
        roomDto.setRoomDetailDTOS(roomDetailDTOS);
        return roomDto;
    }

    public AddressDto generateAddressDtoFromAddress(Address address) {
        AddressDto dto = new AddressDto();
        dto.setDistrictDto(generateDistrictDtoFromDistrict(address.getDistrict()));
        dto.setId(address.getId());
        dto.setStreetName(address.getStreetName());
        dto.setLan(address.getLan());
        dto.setLat(address.getLat());
        return dto;
    }

    public RateDto generateRateDtoFrom(Rate rate) {
        RateDto rateDto = new RateDto();
        rateDto.setUserDto(new UserDto(rate.getUser().getId(), rate.getUser().getFullName(), rate.getUser().getPhoneNumber(), rate.getUser().getImageUrl()));
        rateDto.setRate(rate.getRate());
        rateDto.setComment(rate.getComment());
        rateDto.setCreatedAt(rate.getCreatedAt());
        rateDto.setRoomNumber(rate.getRoomNumber());
        return rateDto;

    }

    public GetChayxanaDetailAndImageDto chayxanaDetailDtosAndImage(List<ChayxanaDetailDto> chayxanaDetailDtos, List<ImageDTO> imageDTOList) {
        GetChayxanaDetailAndImageDto getChayxanaDetailAndImageDto = new GetChayxanaDetailAndImageDto();
        getChayxanaDetailAndImageDto.setChayxanaDetailDtoList(chayxanaDetailDtos);
        getChayxanaDetailAndImageDto.setImageDTOList(imageDTOList);
        return getChayxanaDetailAndImageDto;
    }

    public User generateUserfromUserDto(UserDto userDto) {
        User user = new User();
        user.setFullName(userDto.getFullName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setLanguage(userDto.getLanguage());
        user.setImageUrl(userDto.getImageUrl());

        Optional<Role> optionalRole = roleRepo.findById(3L);
        if (optionalRole.isEmpty()) {
            return null;
        }
        user.setRole(optionalRole.get());
        return user;
    }

    public UserDto generateUserDtofromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFullName(user.getFullName());
        userDto.setLanguage(user.getLanguage());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setImageUrl(user.getImageUrl());
        userDto.setRoleDto(new RoleDto(user.getRole().getId(), user.getRole().getRoleName().name()));
//        userDto.setAddressDto(generateAddresDtoFromAddres(user.getAddress()));
        return userDto;
    }

    public List<ChayxanaDetailDto> genereteChayxanaDetailDtoListFromChDetail(List<ChayxanaDetail> chayxanaDetails) {
        List<ChayxanaDetailDto> chayxanaDetailDtos = new ArrayList<>();
        for (ChayxanaDetail chayxanaDetail : chayxanaDetails) {
            ChayxanaDetailDto chayxanaDetailDto = generateChayxanaDetailDtoFromChayxanaDetail(chayxanaDetail);
            chayxanaDetailDtos.add(chayxanaDetailDto);
        }
        return chayxanaDetailDtos;
    }

    public OrderDto generateOrderDtoFromOrder(Order order) {
        OrderDto dto = new OrderDto();
        dto.setUserDto(new UserDto(order.getUser().getId(), order.getUser().getFullName(), order.getUser().getPhoneNumber(), order.getUser().getImageUrl()));
        dto.setRoomDto(new RoomDto(order.getRoom().getId(), order.getRoom().getRoomNumber()));
        dto.setOrderTime(new Timestamp(order.getOrderTime().getTime()));
        dto.setPrice(order.getPrice());
        dto.setMessage(order.getMessage());
        dto.setPersonAmount(order.getPersonAmount());
        dto.setStatus(order.getStatus().getStatusName().name());
        return dto;
    }

    public DeviceData generateDeviceDataFromDeviceDataDto(DeviceDataDto deviceDataDto) {
        DeviceData deviceData = new DeviceData();
        deviceData.setDeviceId(deviceDataDto.getDeviceId());
        deviceData.setDeviceType(deviceDataDto.getDeviceType());
        deviceData.setDeviceToken(deviceDataDto.getDeviceToken());
        return deviceData;
    }
}


