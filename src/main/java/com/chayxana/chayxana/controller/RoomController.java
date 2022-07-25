package com.chayxana.chayxana.controller;

import com.chayxana.chayxana.entity.Room;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.RoomDto;
import com.chayxana.chayxana.repo.RoomRepo;
import com.chayxana.chayxana.service.MapperDTO;
import com.chayxana.chayxana.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/open/mobile")
@CrossOrigin(maxAge = 3600)
public class RoomController {
    @Autowired
    MapperDTO mapperDTO;

    @Autowired
    RoomRepo roomRepo;


    @Autowired
    RoomService roomService;

    @GetMapping("/free/room/getAllRooms")
    public HttpEntity<?> getAllRooms() {
        ApiResponse apiResponse = roomService.getAllRooms();
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping("/room/getById/{id}")
    public HttpEntity<?> getRoomById(@PathVariable UUID id) {
        ApiResponse apiResponse = roomService.getRoomById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }


    @PostMapping("/room/saveOrEdit")
    public HttpEntity<?> saveOrEdit(@RequestBody RoomDto roomDto) throws Exception {
        Room room = mapperDTO.generateRoomDtoToRoom(roomDto);
        ApiResponse apiResponse = roomService.saveOrEdit(room);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @DeleteMapping("/room/delete/{id}")
    public HttpEntity<?> deleteRoom(@PathVariable UUID id) {
        ApiResponse apiResponse = roomService.deleteRoom(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    /**
     * Get all rooms by chayxana id
     *
     * @param chayxanaId UUID
     * @return ResponseEntity
     * If ApiResponse return true, we return 200 (OK) at HttpStatus else 404 (NOT FOUND) at HttpStatus
     */
    @GetMapping
    public HttpEntity<?> getRoomsByChayxanaId(@RequestParam(name = "chayxanaId") UUID chayxanaId) {
        ApiResponse apiResponse = roomService.getRoomsByChayxanaId(chayxanaId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/room/{id}/{activeOrDeActive}")
    public HttpEntity<?> getRoomRemont(@PathVariable UUID id,@PathVariable boolean activeOrDeActive){
        ApiResponse apiResponse=roomService.getRoomRemont(id,activeOrDeActive);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }
    @GetMapping("/checkOneMonthIsBron")
    public HttpEntity<?> checkRoomsIsBron(@RequestParam(name = "roomId") UUID roomId,
                                          @RequestParam(name = "num") int num){
        ApiResponse apiResponse = roomService.checkIsBron(roomId,num);

        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }


    @GetMapping("/getByUserSize")
    public HttpEntity<?> getByUserSizeAndChayxanaId(@RequestParam(name = "chayxanaId") UUID chayxanaId,
                                                    @RequestParam(name = "size") int size ){
ApiResponse apiResponse = roomService.getByUserSizeAndChayxanaId(chayxanaId,size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }
}
