package com.chayxana.chayxana.service;

import com.chayxana.chayxana.entity.Address;
import com.chayxana.chayxana.entity.District;
import com.chayxana.chayxana.payload.AddressDto;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.repo.AddressRepo;
import com.chayxana.chayxana.repo.DistrictRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepo addressRepo;

    private final MapperDTO mapper;

    private final DistrictRepo districtRepo;

    public Address getAddressFromAddressDto(AddressDto addressDto){
        Address address = mapper.generateAddressFromAddressDto(addressDto);
        if (address != null){
            address = addressRepo.save(address);
            return address;
        }
        return null;
    }

    public ApiResponse saveAddress(AddressDto dto) {
        Address address = getAddressFromAddressDto(dto);
        AddressDto addressDto = mapper.generateAddressDtoFromAddress(address);
        return new ApiResponse(address != null, address != null ? "Succesfully saved" : "Conflict", address != null ? addressDto : null);
    }

    public ApiResponse editAddress(UUID id, AddressDto dto) {
        if (id != null){
            Optional<Address> addressOptional = addressRepo.findById(id);
            if (addressOptional.isPresent()){
                Address address = addressOptional.get();
                District district = districtRepo.getById(dto.getDistrictDto().getId());
                address.setDistrict(district);
                address.setStreetName(dto.getStreetName());
                address.setLan(dto.getLan());
                address.setLat(dto.getLat());
                address = addressRepo.save(address);
                AddressDto addressDto = mapper.generateAddressDtoFromAddress(address);
                return new ApiResponse(true, "Succesfully edited", addressDto);
            }
            return new ApiResponse(false, "Not found Address");
        }
        return new ApiResponse(false, "Id Mustn't empty");
    }

    public ApiResponse deleteAddress(UUID id) {
        Optional<Address> addressOptional = addressRepo.findById(id);
        if (addressOptional.isPresent()){
            addressRepo.delete(addressOptional.get());
            return new ApiResponse(true, "Succesfully deleted");
        }
        return new ApiResponse(false,"Not found");
    }
}
