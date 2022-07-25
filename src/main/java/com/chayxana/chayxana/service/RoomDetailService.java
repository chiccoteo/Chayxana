package com.chayxana.chayxana.service;

import com.chayxana.chayxana.entity.Icon;
import com.chayxana.chayxana.entity.RoomDetail;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.RoomDetailDTO;
import com.chayxana.chayxana.repo.IconRepo;
import com.chayxana.chayxana.repo.RoomDetailRepo;
import com.chayxana.chayxana.repo.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomDetailService {


    private final RoomDetailRepo roomDetailRepo;

    private final IconRepo iconRepo;

    public ApiResponse getAllRoomDetails(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RoomDetail> allRoomDetail = roomDetailRepo.findAll(pageable);
        return !allRoomDetail.isEmpty() ? new ApiResponse(true, "Success", allRoomDetail)
                : new ApiResponse(false, "Empty");
    }


    public ApiResponse getRoomDetailById(Long id) {
        Optional<RoomDetail> optionalRoomDetail = roomDetailRepo.findById(id);
        if (optionalRoomDetail.isEmpty()) {
            return new ApiResponse(false, "Not Found");
        }
        return new ApiResponse(true, "Success", optionalRoomDetail.get());
    }


    public ApiResponse addRoomDetail(RoomDetailDTO dto) {
        Optional<Icon> optionalIcon = iconRepo.findById(dto.getIconId());
        if (optionalIcon.isEmpty()) {
            return new ApiResponse(false, "Cannot find the Icon");
        }
        if (roomDetailRepo.existsByName(dto.getName())) {
            return new ApiResponse(false, "Room Detail already exists with such name");
        }
        RoomDetail roomDetail = new RoomDetail(dto.getName(), true, optionalIcon.get());
        roomDetailRepo.save(roomDetail);
        return new ApiResponse(true, "Saved", roomDetail.getId());
    }


    public ApiResponse editRoomDetailById(Long id, RoomDetailDTO dto) {
        Optional<RoomDetail> optionalRoomDetail = roomDetailRepo.findById(id);
        if (optionalRoomDetail.isEmpty()) {
            return new ApiResponse(false, "Cannot find Room Detail");
        }

        Optional<Icon> optionalIcon = iconRepo.findById(dto.getIconId());
        if (optionalIcon.isEmpty()) {
            return new ApiResponse(false, "Cannot find the Icon");
        }
        if (roomDetailRepo.existsByNameAndIdNot(dto.getName(), id)) {
            return new ApiResponse(false, "Room Detail already exists with such name");
        }
        RoomDetail roomDetail = optionalRoomDetail.get();
        roomDetail.setIcon(optionalIcon.get());
        roomDetail.setName(dto.getName());
        roomDetail.setActive(dto.isActive());
        roomDetailRepo.save(roomDetail);
        return new ApiResponse(true, "Edited", roomDetail.getId());
    }

    public ApiResponse deleteRoomDetailById(Long id) {
        try {
            roomDetailRepo.deleteById(id);
            return new ApiResponse(true, "Deleted");
        } catch (Exception e) {
            return new ApiResponse(true, "It is functionally connected to another room");
        }
    }
}