package com.chayxana.chayxana.payload;

import com.chayxana.chayxana.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private UUID id;
    private String fullName;

    private String phoneNumber;

    private RoleDto roleDto;

    private String language;

    private String imageUrl;

    private Timestamp spamTime;

    private Timestamp createdAt;

    public UserDto(String fullName, String phoneNumber, RoleDto roleDto, String language) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.roleDto = roleDto;
        this.language = language;
    }

    public UserDto(UUID id, String fullName, String phoneNumber, String imageUrl) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
    }
}