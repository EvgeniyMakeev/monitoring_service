package com.makeev.monitoring_service.servlets.indications_servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeev.monitoring_service.aop.annotations.Loggable;
import com.makeev.monitoring_service.dao.CounterDAO;
import com.makeev.monitoring_service.dto.IndicationsOfUserDTO;

import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.exceptions.EmptyException;
import com.makeev.monitoring_service.exceptions.IncorrectValuesException;
import com.makeev.monitoring_service.exceptions.MonthFormatException;
import com.makeev.monitoring_service.exceptions.NoCounterNameException;
import com.makeev.monitoring_service.exceptions.UserNotFoundException;
import com.makeev.monitoring_service.exceptions.YearFormatException;
import com.makeev.monitoring_service.in.Input;
import com.makeev.monitoring_service.mappers.IndicationsOfUserMapper;
import com.makeev.monitoring_service.model.Counter;
import com.makeev.monitoring_service.model.IndicationsOfUser;
import com.makeev.monitoring_service.service.IndicationService;
import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Loggable
@WebServlet("/indications")
public class IndicationsServlet extends HttpServlet {
    public static final String CONTENT_TYPE = "application/json";

    private ObjectMapper objectMapper;
    private IndicationService indicationService;
    private final IndicationsOfUserMapper indicationsOfUserMapper = IndicationsOfUserMapper.INSTANCE;
    private CounterDAO counterDAO;
    private Input input;

    private Map<Class<? extends RuntimeException>, ExceptionHandler> exceptionHandlers;

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
        indicationService = new IndicationService(new ConnectionManagerImpl());
        counterDAO = new CounterDAO(new ConnectionManagerImpl());
        input = new Input();
        exceptionHandlers = new HashMap<>();
        exceptionHandlers.put(UserNotFoundException.class, this::handleUserNotFoundException);
        exceptionHandlers.put(NoCounterNameException.class, this::handleNoCounterNameException);
        exceptionHandlers.put(IncorrectValuesException.class, this::handleIncorrectValuesException);
        exceptionHandlers.put(NumberFormatException.class, this::handleInvalidCounterFormat);
        exceptionHandlers.put(YearFormatException.class, this::handleYearFormatException);
        exceptionHandlers.put(MonthFormatException.class, this::handleMonthFormatException);
        exceptionHandlers.put(EmptyException.class, this::handleEmptyException);
        exceptionHandlers.put(DaoException.class, this::handleInternalServerError);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<IndicationsOfUser> indicationsOfUsers = indicationService.getAllIndications();

            List<IndicationsOfUserDTO> indicationsOfUserDTOs = indicationsOfUsers
                    .stream()
                    .map(indicationsOfUserMapper::toDTO)
                    .toList();

            resp.setContentType(CONTENT_TYPE);
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getOutputStream(), indicationsOfUserDTOs);
        } catch (RuntimeException e) {
            Optional.ofNullable(exceptionHandlers.get(e.getClass())).ifPresent(handler -> handler.handle(resp));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter("login");
        String counterName = req.getParameter("nameOfCounter");
        String yearStr = req.getParameter("year");
        String monthStr = req.getParameter("month");
        String valueStr = req.getParameter("value");

        if (login == null || counterName == null || yearStr == null
                || monthStr == null || valueStr == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("One or more parameters are missing");
            return;
        }

        try {
            int year = input.getYear(yearStr);
            int month = input.getMonth(monthStr);
            Double value = input.getDouble(valueStr);
            LocalDate date = LocalDate.of(year, month,1);
            Counter counter = counterDAO.getCounterByName(counterName);

            indicationService.addIndicationOfUser(login, counter, date, value);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("Indication added successfully");
        } catch (RuntimeException e) {
            Optional.ofNullable(exceptionHandlers.get(e.getClass())).ifPresent(handler -> handler.handle(resp));
        }
    }
    @FunctionalInterface
    interface ExceptionHandler {
        void handle(HttpServletResponse resp);
    }

    private void handleUserNotFoundException(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        try {
            resp.getWriter().write(new UserNotFoundException().getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void handleIncorrectValuesException(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        try {
            resp.getWriter().write(new IncorrectValuesException().getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleNoCounterNameException(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        try {
            resp.getWriter().write(new NoCounterNameException().getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleInvalidCounterFormat(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        try {
            resp.getWriter().write("Invalid format for value of counter");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleYearFormatException(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        try {
            resp.getWriter().write(new YearFormatException().getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleMonthFormatException(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        try {
            resp.getWriter().write(new MonthFormatException().getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleEmptyException(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        try {
            resp.getWriter().write(new EmptyException().getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleInternalServerError(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        try {
            resp.getWriter().write("Error occurred " + new DaoException(new RuntimeException()).getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}