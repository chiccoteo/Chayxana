package com.chayxana.chayxana.service;

import com.chayxana.chayxana.entity.DeviceData;
import com.chayxana.chayxana.entity.User;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.DeviceDataDto;
import com.chayxana.chayxana.repo.DeviceDataRepo;
import com.chayxana.chayxana.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceDataService {

    @Autowired
    DeviceDataRepo deviceDataRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    MapperDTO mapperDTO;

    public ApiResponse saveOrSetUser(DeviceDataDto deviceDataDto) {
        Optional<DeviceData> dataOptional = deviceDataRepo.findByDeviceId(deviceDataDto.getDeviceId());
        if (dataOptional.isPresent()) {
            DeviceData deviceData = dataOptional.get();
            Optional<User> optionalUser = userRepo.findById(deviceDataDto.getUserDto().getId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                setOffActiveDeviceByUserId(user.getId());
                deviceData.setUser(user);
                deviceData.setActive(true);
                deviceDataRepo.save(deviceData);
                return new ApiResponse(true, "Edit user for device id.");
            } else {
                return new ApiResponse(false, "Not Found User");
            }
        }
        DeviceData deviceData = mapperDTO.generateDeviceDataFromDeviceDataDto(deviceDataDto);
        Optional<User> optionalUser = userRepo.findById(deviceDataDto.getUserDto().getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            setOffActiveDeviceByUserId(user.getId());
            deviceData.setUser(user);
            deviceDataRepo.save(deviceData);
            return new ApiResponse(true, "Succesfully Saved");
        } else {
            return new ApiResponse(false, "Not Found User");
        }
    }

    private void setOffActiveDeviceByUserId(UUID userId){
        List<DeviceData> deviceDataList = deviceDataRepo.findAllByUserIdAndActiveTrue(userId);
        for (DeviceData deviceData : deviceDataList) {
            deviceData.setActive(false);
            deviceDataRepo.save(deviceData);
        }
    }
}
