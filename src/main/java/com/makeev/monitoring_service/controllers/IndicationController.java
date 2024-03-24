package com.makeev.monitoring_service.controllers;

import com.makeev.monitoring_service.dao.CounterDAO;
import com.makeev.monitoring_service.dao.UserDAO;
import com.makeev.monitoring_service.dto.IndicationsOfUserDTO;
import com.makeev.monitoring_service.in.Input;
import com.makeev.monitoring_service.mappers.IndicationsOfUserMapper;
import com.makeev.monitoring_service.model.Counter;
import com.makeev.monitoring_service.model.IndicationsOfUser;
import com.makeev.monitoring_service.service.IndicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/indications", produces = MediaType.APPLICATION_JSON_VALUE)
public class IndicationController {

    private final IndicationsOfUserMapper indicationsOfUserMapper;
    private final CounterDAO counterDAO;
    private final UserDAO userDAO;
    private final IndicationService indicationService;
    private final Input input;

    @Autowired
    public IndicationController(IndicationsOfUserMapper indicationsOfUserMapper,
                                CounterDAO counterDAO, UserDAO userDAO, IndicationService indicationService,
                                Input input) {
        this.indicationsOfUserMapper = indicationsOfUserMapper;
        this.counterDAO = counterDAO;
        this.userDAO = userDAO;
        this.indicationService = indicationService;
        this.input = input;
    }

    @GetMapping
    public ResponseEntity<List<IndicationsOfUserDTO>> getAllIndications() {
        List<IndicationsOfUser> indicationsOfUsers = indicationService.getAllIndications();
        List<IndicationsOfUserDTO> indicationsOfUserDTOs = indicationsOfUsers
                .stream()
                .map(indicationsOfUserMapper::toDTO)
                .toList();
        return ResponseEntity.ok(indicationsOfUserDTOs);
    }

    @PostMapping
    public ResponseEntity<String> addIndication(
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "nameOfCounter", required = false) String nameOfCounter,
            @RequestParam(value = "year", required = false) String yearStr,
            @RequestParam(value = "month", required = false) String monthStr,
            @RequestParam(value = "month", required = false) String valueStr) {

        if (login == null || nameOfCounter == null || yearStr == null
                || monthStr == null || valueStr == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("One or more parameters are missing");
        }
        int year = input.getYear(yearStr);
        int month = input.getMonth(monthStr);
        Double value = input.getDouble(valueStr);
        LocalDate date = LocalDate.of(year, month, 1);
        Counter counter = counterDAO.getCounterByName(nameOfCounter);

        indicationService.addIndicationOfUser(login, counter, date, value);

        return ResponseEntity.status(HttpStatus.CREATED).body("Indication added successfully");
    }

    @GetMapping(value = "/currentIndicationForUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCurrentIndicationForUser(
            @RequestParam(value = "login", required = false) String login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Login parameter is required");
        }
        userDAO.getUserByLogin(login);
        List<IndicationsOfUser> indicationsOfUsers =
                indicationService.getCurrentIndication(login);
        List<IndicationsOfUserDTO> indicationDTOs = indicationsOfUsers
                .stream()
                .map(indicationsOfUserMapper::toDTO)
                .toList();

        return ResponseEntity.ok(indicationDTOs);
    }

    @GetMapping(value = "/indicationsForUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getIndicationsForUser(
            @RequestParam(value = "login", required = false) String login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Login parameter is required");
        }
        userDAO.getUserByLogin(login);
        List<IndicationsOfUser> indicationsOfUsers =
                indicationService.getAllIndicationsForUser(login);
        List<IndicationsOfUserDTO> indicationsOfUserDTOs = indicationsOfUsers
                .stream()
                .map(indicationsOfUserMapper::toDTO)
                .toList();

        return ResponseEntity.ok(indicationsOfUserDTOs);
    }

    @GetMapping(value = "/indicationsForUserForMonth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getIndicationsForUserForMonth(
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "year", required = false) String yearStr,
            @RequestParam(value = "month", required = false) String monthStr) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Login parameter is required");
        }
        userDAO.getUserByLogin(login);

        if (yearStr == null || monthStr == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("One or more parameters are missing");
        }
        int year = input.getYear(yearStr);
        int month = input.getMonth(monthStr);
        LocalDate date = LocalDate.of(year, month, 1);

        List<IndicationsOfUser> indicationsOfUsers = indicationService.getAllIndicationsForUserForMonth(login, date);

        List<IndicationsOfUserDTO> indicationsOfUserDTOs = indicationsOfUsers
                .stream()
                .map(indicationsOfUserMapper::toDTO)
                .toList();

        return ResponseEntity.ok(indicationsOfUserDTOs);
    }
}