package com.makeev.monitoring_service.controllers;

import com.makeev.monitoring_service.dao.CounterDAO;
import com.makeev.monitoring_service.dto.CounterDTO;
import com.makeev.monitoring_service.mappers.CounterMapper;
import com.makeev.monitoring_service.model.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/counters", produces = MediaType.APPLICATION_JSON_VALUE)
public class CounterController {

    private final CounterMapper counterMapper;
    private final CounterDAO counterDAO;

    @Autowired
    public CounterController(CounterMapper counterMapper, CounterDAO counterDAO) {
        this.counterMapper = counterMapper;
        this.counterDAO = counterDAO;
    }

    @GetMapping
    public ResponseEntity<List<CounterDTO>> getAllCounters() {
        List<Counter> counters = counterDAO.getAllCounters();
        List<CounterDTO> counterDTOs = counters.stream()
                .map(counterMapper::toDTO)
                .toList();
        return ResponseEntity.ok(counterDTOs);
    }

    @GetMapping(value = "/getCounter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserByLogin(
            @RequestParam(value = "id", required = false) String idString) {
        if (idString == null || idString.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("ID parameter is required");
        }
        long id = Long.parseLong(idString);
        Counter counter = counterDAO.getCounterById(id);
        CounterDTO counterDTO = counterMapper.toDTO(counter);
        return ResponseEntity.ok(counterDTO);
    }

    @PostMapping
    public ResponseEntity<String> addCounter(
            @RequestParam(value = "nameOfCounter", required = false) String nameOfCounter) {
        if (nameOfCounter == null || nameOfCounter.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name of counter is required");
        }
        counterDAO.addCounter(nameOfCounter);
        return ResponseEntity.status(HttpStatus.CREATED).body(nameOfCounter + " counter added successfully");
    }
}
