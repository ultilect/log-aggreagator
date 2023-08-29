package com.deneb.logaggregator.controller;

import com.deneb.logaggregator.dto.UserDTO;
import com.deneb.logaggregator.entity.User;
import com.deneb.logaggregator.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired()
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public String check() {
        return "check";
    }
    @GetMapping(path = "all", produces = APPLICATION_JSON_VALUE)
    public List<UserDTO> findAllUsers() {
        List<User> users = this.userService.findAllUsers();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        return this.modelMapper.map(user, UserDTO.class);
    }

}
