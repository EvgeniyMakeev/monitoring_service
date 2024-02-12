package com.makeev.monitoring_service.servlets.counters_servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeev.monitoring_service.aop.annotations.Loggable;
import com.makeev.monitoring_service.dao.CounterDAO;
import com.makeev.monitoring_service.dto.CounterDTO;
import com.makeev.monitoring_service.exceptions.CounterAlreadyExistsException;
import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.mappers.CounterMapper;
import com.makeev.monitoring_service.model.Counter;
import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Loggable
@WebServlet("/counters")
public class CountersServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private CounterDAO counterDAO;
    private CounterMapper counterMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
        counterDAO = new CounterDAO(new ConnectionManagerImpl());
        counterMapper = CounterMapper.INSTANCE;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Counter> counters = counterDAO.getAll();

            List<CounterDTO> counterDTOs = counters.stream()
                    .map(counterMapper::toDTO)
                    .collect(Collectors.toList());

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getOutputStream(), counterDTOs);
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String nameOfCounter = req.getParameter("nameOfCounter");
            resp.setContentType("application/json");
            if (nameOfCounter == null || nameOfCounter.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Name of counter is required");
                return;
            }
            Counter counter = counterDAO.add(nameOfCounter);
            CounterDTO counterDTO = counterMapper.toDTO(counter);
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("Account added successfully");
            objectMapper.writeValue(resp.getOutputStream(), counterDTO);
        } catch (CounterAlreadyExistsException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write(e.getMessage());
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred: " + e.getMessage());
        }
    }
}