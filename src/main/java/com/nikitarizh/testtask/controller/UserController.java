package com.nikitarizh.testtask.controller;

import com.nikitarizh.testtask.dto.user.UserCreateDTO;
import com.nikitarizh.testtask.dto.user.UserFullDTO;
import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.nikitarizh.testtask.mapper.UserMapper.USER_MAPPER;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/new")
    public UserFullDTO register(@RequestBody UserCreateDTO userCreateDTO) {
        // TODO: validation
        return userService.create(userCreateDTO);
    }
}