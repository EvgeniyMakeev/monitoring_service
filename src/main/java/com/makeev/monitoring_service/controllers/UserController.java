package com.makeev.monitoring_service.controllers;

import com.makeev.monitoring_service.dao.UserDAO;
import com.makeev.monitoring_service.dto.UserDTO;
import com.makeev.monitoring_service.mappers.UserMapper;
import com.makeev.monitoring_service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserMapper userMapper;
    private final UserDAO userDAO;

    @Autowired
    public UserController(UserMapper userMapper, UserDAO userDAO) {
        this.userMapper = userMapper;
        this.userDAO = userDAO;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userDAO.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(userMapper::toDTO)
                .toList();
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping(value = "/getUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserByLogin(
            @RequestParam(value = "login", required = false) String login) {
        if (login == null || login.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Login parameter is required");
        }
        User user = userDAO.getUserByLogin(login);
        UserDTO userDTO = userMapper.toDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping
    public ResponseEntity<?> addUser(
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "password", required = false) String password) {
        if (login == null || login.isEmpty() || password == null || password.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Both login and password parameters are required");
        }
        userDAO.existByLogin(login);
        userDAO.addUser(login, password);
        return ResponseEntity.status(HttpStatus.CREATED).body("New user added successfully");
    }

    @PutMapping
    public ResponseEntity<?> logIn(
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "password", required = false) String password) {
        if (login == null || login.isEmpty() || password == null || password.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Both login and password parameters are required");
        }
        userDAO.checkCredentials(login, password);
        return ResponseEntity.ok("User credentials verified successfully");
    }
}
