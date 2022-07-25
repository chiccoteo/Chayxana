package com.chayxana.chayxana.service;

import com.chayxana.chayxana.entity.Chayxana;
import com.chayxana.chayxana.entity.Room;
import com.chayxana.chayxana.entity.RoomDetail;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.CheckedRoomsOneMonth;
import com.chayxana.chayxana.payload.RoomDto;
import com.chayxana.chayxana.repo.ChayxanaRepo;
import com.chayxana.chayxana.repo.RoomDetailRepo;
import com.chayxana.chayxana.repo.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepo roomRepo;

    private final ChayxanaRepo chayxanaRepo;

    private final RoomDetailRepo roomDetailRepo;

    private final MapperDTO mapperDTO;


    public ApiResponse saveOrEdit(Room room) {
        ApiResponse apiResponse = new ApiResponse();
        if (room.getId() == null) {
            if(room.getChayxana().getId()==null){
                apiResponse.setSuccess(false);
                apiResponse.setMessage("Chayxana not found");
            }
            else {
                List<Room> roomList = roomRepo.findAll();
                for (Room room1 : roomList) {
                    if (room.getRoomNumber()==room1.getRoomNumber()){
                        apiResponse.setSuccess(false);
                        apiResponse.setMessage("There is a room with this number  "+ room.getRoomNumber());
                    }
                }
                Room savedRoom = roomRepo.save(room);
                apiResponse.setSuccess(true);
                apiResponse.setMessage("Successfully saved");
                apiResponse.setObject(mapperDTO.generateRoomToRoomDto(savedRoom));
            }
        } else  {
            Room updatedRoom = roomRepo.saveAndFlush(room);
            apiResponse.setSuccess(true);
            apiResponse.setMessage("Successfully updated");
            apiResponse.setObject(mapperDTO.generateRoomToRoomDto(updatedRoom));
        }
        return apiResponse;
    }

    public ApiResponse getAllRooms() {
        List<Room> allRooms = roomRepo.findAll();
        List<RoomDto> alRoomdtos = new ArrayList<>();
        if (allRooms.isEmpty()) {
            return new ApiResponse(false, "Any rooms not added");
        } else {
            for (Room room : allRooms) {
                RoomDto roomDto = mapperDTO.generateRoomToRoomDto(room);
                alRoomdtos.add(roomDto);
            }
            return new ApiResponse(true, "List of all rooms ", alRoomdtos);
        }
    }

    public ApiResponse getRoomById(UUID id) {
        Optional<Room> optionalRoom = roomRepo.findById(id);
        if(optionalRoom.isPresent()) {
            Room room = optionalRoom.get();
            RoomDto roomDto = mapperDTO.generateRoomToRoomDto(room);
            return new ApiResponse(true, "Success", roomDto);
        }
        return new ApiResponse(false, "Room not found");


    }

    public ApiResponse deleteRoom(UUID id) {
        if (roomRepo.findById(id).isPresent()) {
            roomRepo.deleteById(id);
            return new ApiResponse(true, "Successfully deleted");
        } else {
            return new ApiResponse(false, "Room not found");
        }
    }

    /**
     * Get all rooms by Chayxana id
     * @param chayxanaId UUID
     * @return ApiResponse
     * If such a chayxana does not exist, we return false at ApiResponse else true at ApiResponse
     */
    public ApiResponse getRoomsByChayxanaId(UUID chayxanaId) {
        // Checking if there is such a chayxana by id
        Optional<Chayxana> optionalChayxana = chayxanaRepo.findById(chayxanaId);
        if (optionalChayxana.isEmpty())
            return new ApiResponse(false, "Such a chayxana doesn't exist");

        // Get all rooms by chayxana id
        List<Room> rooms = roomRepo.findAllByChayxana_Id(chayxanaId);

        List<RoomDto> roomDtoList = new ArrayList<>();
        for (Room room : rooms) {
            //
            List<RoomDetail> roomDetails = room.getRoomDetails();

            // Generate RoomDto From Room and RoomDetail
            RoomDto roomDto = mapperDTO.generateRoomDtoFromRoomAndRoomDetail(room, roomDetails);
            roomDtoList.add(roomDto);
        }
        return new ApiResponse(true, "All rooms", roomDtoList);
    }

    public ApiResponse getRoomRemont(UUID id, boolean activeOrDeActive) {
        Optional<Room> roomRemont = roomRepo.findById(id);
        if (roomRemont.isPresent()) {
            roomRemont.get().setActive(activeOrDeActive);
            roomRepo.save(roomRemont.get());
            return new ApiResponse(true, "Successfully updated");
        }
        return new ApiResponse(false, "Room not found");
    }

    public ApiResponse checkIsBron(UUID roomId, int num) {
        boolean check = roomRepo.existsById(roomId);
        if (check) {
            List<CheckedRoomsOneMonth> resultsDtos = roomRepo.checkRoomIdIsBronByMonth(roomId, num);
            Map<LocalDate, Boolean> integerMap = new HashMap<>();

            for (CheckedRoomsOneMonth l : resultsDtos) {
                assert false;
                integerMap.put(l.getTimestamps().toLocalDateTime().toLocalDate(),
                        l.getCheck());

            }
            return new ApiResponse(true, "Sended", integerMap);

        }
        return new ApiResponse(false, "This room id  doesn't exist");


    }

    public ApiResponse getByUserSizeAndChayxanaId(UUID chayxanaId, int size) {

        boolean id = chayxanaRepo.existsById(chayxanaId);
        if (id) {
            List<Room> rooms = roomRepo.getByUserSize(chayxanaId, size);
            List<RoomDto> roomDtoList = rooms.stream()
                    .map(mapperDTO::generateRoomToRoomDto)
                    .collect(Collectors.toList());

            return new ApiResponse(true, "sended", roomDtoList);

        }
        return new ApiResponse(false, "this chayxana doesn't exist");

    }
}
