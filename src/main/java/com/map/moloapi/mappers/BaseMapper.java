package com.map.moloapi.mappers;


import com.map.moloapi.contracts.entreprises.EntrepriseRequest;
import com.map.moloapi.contracts.users.requests.CreateUserRequest;
import com.map.moloapi.dtos.EntrepriseDto;
import com.map.moloapi.dtos.RoleDto;
import com.map.moloapi.dtos.UserDto;
import com.map.moloapi.entities.Entreprise;
import com.map.moloapi.entities.Role;
import com.map.moloapi.entities.User;
import com.map.moloapi.securities.UserDetailsImpl;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author DIOMANDE Souleymane
 * @Date 04/10/2022 20:05
 */
@Mapper(componentModel = "spring")
public interface BaseMapper {

    // USER MAPPERS
    UserDto toDto(UserDetailsImpl user);

    UserDto toDto(User user);

    List<UserDto> toUserDtos(List<User> users);

    User toEntity(UserDto userDto);

    User toEntity(CreateUserRequest request);

    // Role MAPPERS
    RoleDto toDto(Role otp);

    List<RoleDto> toRoleDtos(List<Role> otps);

    Role toEntity(RoleDto otpDto);

    Entreprise toEntity(EntrepriseRequest request);

    EntrepriseDto toDto(Entreprise otp);

    List<EntrepriseDto> toEntrepriseDtos(List<Entreprise> otps);
}
