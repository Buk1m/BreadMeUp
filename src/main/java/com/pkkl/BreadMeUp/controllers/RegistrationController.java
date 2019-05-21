package com.pkkl.BreadMeUp.controllers;

import com.pkkl.BreadMeUp.dtos.UserCreateDto;
import com.pkkl.BreadMeUp.dtos.UserDetailsDto;
import com.pkkl.BreadMeUp.model.User;
import com.pkkl.BreadMeUp.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RegistrationController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Autowired
    public RegistrationController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(
            value = "/registration",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public UserDetailsDto register(@Valid @RequestBody UserCreateDto userCreateDto) {
        return this.mapUserToUserDetailsDto(
                this.userService.register(
                        this.mapUserCreateDtoToUser(userCreateDto)
                )
        );
    }

    private User mapUserCreateDtoToUser(UserCreateDto userCreateDto) {
        return this.modelMapper.map(userCreateDto, User.class);
    }

    private UserDetailsDto mapUserToUserDetailsDto(User user) {
        return this.modelMapper.map(user, UserDetailsDto.class);
    }
}
