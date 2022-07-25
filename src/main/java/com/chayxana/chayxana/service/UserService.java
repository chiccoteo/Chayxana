package com.chayxana.chayxana.service;

import com.chayxana.chayxana.entity.Address;
import com.chayxana.chayxana.entity.Chayxana;
import com.chayxana.chayxana.entity.Role;
import com.chayxana.chayxana.entity.User;
import com.chayxana.chayxana.entity.enums.RoleName;
import com.chayxana.chayxana.exceptions.PageSizeException;
import com.chayxana.chayxana.payload.AddressDto;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.UserDto;
import com.chayxana.chayxana.repo.AddressRepo;
import com.chayxana.chayxana.repo.ChayxanaRepo;
import com.chayxana.chayxana.repo.RoleRepo;
import com.chayxana.chayxana.repo.UserRepo;
import com.chayxana.chayxana.utills.CommandUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    private final MapperDTO mapperDTO;



    private final ChayxanaRepo chayxanaRepo;

    private final RoleRepo roleRepo;

    public ApiResponse getOneUser(UUID id) {
        Optional<User> optionalUser = userRepo.findById(id);

        if (optionalUser.isEmpty()) {
            return new ApiResponse(false, "User Not Found");
        }
        UserDto userDto = mapperDTO.generateUserDtofromUser(optionalUser.get());
        return new ApiResponse(true, "User", userDto);
    }

    public ApiResponse deleteUser(UUID id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            return new ApiResponse(false, "User not found");
        }
        disabledChayxanaByUserId(optionalUser.get().getId(),true);
        optionalUser.get().setAccountNonLocked(false);
        optionalUser.get().setPhoneNumber(optionalUser.get().getPhoneNumber() + " & " + UUID.randomUUID());
        userRepo.save(optionalUser.get());
        return new ApiResponse(true, "User blocked");
    }

    public ApiResponse getAllPageable(Integer page, Integer size) throws PageSizeException {
        Role roleByRoleName = roleRepo.findRoleByRoleName(RoleName.ROLE_SUPERADMIN);
        Page<User> users = userRepo.findAllByRoleIsNotAndAccountNonLockedTrue(roleByRoleName, CommandUtils.simplePageable(page - 1, size));
        return new ApiResponse(true, "Users", users);
    }

    public ApiResponse addUser(UserDto userDto) {
        boolean existPhoneNUmber = userRepo.existsByPhoneNumber(userDto.getPhoneNumber());
        if (existPhoneNUmber) {
            return new ApiResponse(false,"This number is already exist");
        }
        User user = mapperDTO.generateUserfromUserDto(userDto);
        if (user == null) {
            return new ApiResponse(false, "Role Not Found");
        }

        User savedUser = userRepo.save(user);
        UserDto userDto1 = mapperDTO.generateUserDtofromUser(savedUser);
        return new ApiResponse(true, "User saved", userDto1);
    }

    public ApiResponse editUserByUser(UUID id, UserDto userDto) {
        if (id != null) {
            Optional<User> optionalUser = userRepo.findById(id);
            if (optionalUser.isPresent()) {

                if(userDto.getRoleDto() == null) {

                    boolean existPhoneNUmber = userRepo.existsByPhoneNumber(userDto.getPhoneNumber());
                    if (existPhoneNUmber) {
                        return new ApiResponse(false,"This number is already exist");
                    }
                        User user = optionalUser.get();
                        user.setPhoneNumber(userDto.getPhoneNumber());
                        user.setFullName(userDto.getFullName());
                        user.setLanguage(userDto.getLanguage());
                        user.setImageUrl(userDto.getImageUrl());
                        User editedUser = userRepo.save(user);
                        UserDto userDtoTo = mapperDTO.generateUserDtofromUser(editedUser);
                        return new ApiResponse(true, "User edited", userDtoTo);
                      } else {
                    User user = editUserRole(userDto, optionalUser.get());
                    if(user != null) {
                        UserDto userDtoTo = mapperDTO.generateUserDtofromUser(user);
                        return new ApiResponse(true, "User role edited", userDtoTo);
                    }
                }
            }
            return new ApiResponse(false, "User not found");

        }
        return new ApiResponse(false, "Id Mustn't empty");

    }

    private User editUserRole(UserDto userDto, User user){
        if(userDto.getRoleDto().getId() != null){
            Optional<Role> byId = roleRepo.findById(userDto.getRoleDto().getId());
             if(byId.isPresent()){
               user.setRole(byId.get());
                 return userRepo.save(user);
           }
              return null;
        }
        return null;
    }
    public User getUserById(UUID uuid) {
        Optional<User> userOptional = userRepo.findById(uuid);
        User user = null;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        }
        return user;
    }


    /**
     * Chayxanachini disable qiladigan method!
     *
     * @param id UUID
     * @return ApiResponse
     */
    public ApiResponse disableUser(UUID id, boolean disable) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            return new ApiResponse(false, "User not found");
        }
        User user = optionalUser.get();
        if (disable) {
            user.setDisabled(disable);
            userRepo.save(user);
            if (user.getRole().getRoleName().equals(RoleName.ROLE_CHAYXANACHI)) {
                disabledChayxanaByUserId(user.getId(), disable);
            }
            return new ApiResponse(true, "User successfully Disabled!");
        } else {
            user.setDisabled(disable);
            userRepo.save(user);
            if (user.getRole().getRoleName().equals(RoleName.ROLE_CHAYXANACHI)) {
                disabledChayxanaByUserId(id, disable);
            }
            return new ApiResponse(true, "User successfully Enabled!");
        }
    }

    public void disabledChayxanaByUserId(UUID id, boolean disabled){
        List<Chayxana> chayxanaList = chayxanaRepo.findAllByUserId(id);
        if (!chayxanaList.isEmpty()) {
            for (Chayxana chayxana : chayxanaList) {
                chayxana.setActive(!disabled);
            }
            chayxanaRepo.saveAll(chayxanaList);
        }
    }

    public ApiResponse getAllChayxanachi(Integer page, Integer size) throws PageSizeException {
//        Role roleByRoleName = roleRepo.findRoleByRoleName(RoleName.ROLE_CHAYXANACHI);
//        Page<User> allByRole = userRepo.findAllByRole(roleByRoleName, CommandUtils.simplePageable(page - 1, size));
        Page<User> allByRole = userRepo.findAllByRoleRoleNameAndAccountNonLockedTrue(RoleName.ROLE_CHAYXANACHI, CommandUtils.simplePageable(page - 1, size));
        return new ApiResponse(true, "Users", allByRole);


    }

    public ApiResponse bangUser(UUID id, boolean bang) {
        Optional<User> bangUser = userRepo.findById(id);
        if (bangUser.isPresent()) {
            User user = bangUser.get();
            if (bang){
                user.setBronCount(3);
                user.setSpamTime(new Timestamp(System.currentTimeMillis()));
                userRepo.save(user);
                return new ApiResponse(true, "Banned Success");
            }else {
                user.setBronCount(0);
                user.setSpamTime(null);
                userRepo.save(user);
                return new ApiResponse(true, "UnBanned Success");
            }
        } else {
            return new ApiResponse(false, "Not found user");
        }
    }

    public ApiResponse getUserByToken(User user) {
        UserDto userDto = mapperDTO.generateUserDtofromUser(user);
        return new ApiResponse(true, "Succesfully", userDto);
    }
}
