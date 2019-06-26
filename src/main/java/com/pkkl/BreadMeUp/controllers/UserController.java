package com.pkkl.BreadMeUp.controllers;

import com.pkkl.BreadMeUp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping(
            value = "admin/users/{login}/block"
    )
    public void block(@PathVariable(name = "login") String login) {
        this.userService.blockUser(login);
    }

    @PutMapping(
            value = "admin/users/{login}/unblock"
    )
    public void unblock(@PathVariable(name = "login") String login) {
        this.userService.unblockUser(login);
    }

    @PutMapping(
            value = "admin/users/{userId}/bakeries/{bakeryId}"
    )
    public void assignBakeryToUser(@PathVariable(name = "userId") Integer userId, @PathVariable(name = "bakeryId") int bakeryId) {
        this.userService.assignBakeryToUser(userId, bakeryId);
    }
}
