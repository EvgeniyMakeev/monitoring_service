package com.makeev.monitoring_service.servlets.indications_servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeev.monitoring_service.aop.annotations.Loggable;
import com.makeev.monitoring_service.dao.CounterDAO;
import com.makeev.monitoring_service.dto.IndicationsOfUserDTO;
import com.makeev.monitoring_service.exceptions.*;

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
import java.util.List;

@Loggable
@WebServlet("/indications")
public class IndicationsServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private IndicationService indicationService;
    private IndicationsOfUserMapper indicationsOfUserMapper;
    private CounterDAO counterDAO;
    private Input input;

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
        indicationService = new IndicationService(new ConnectionManagerImpl());
        indicationsOfUserMapper = indicationsOfUserMapper.INSTANCE;
        counterDAO = new CounterDAO(new ConnectionManagerImpl());
        input = new Input();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<IndicationsOfUser> indicationsOfUsers = indicationService.getAllIndications();

            List<IndicationsOfUserDTO> indicationsOfUserDTOs = indicationsOfUsers.stream()
                    .map(indicationsOfUserMapper::toDTO)
                    .toList();

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getOutputStream(), indicationsOfUserDTOs);
        } catch (EmptyException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(e.getMessage());
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred: " + e.getMessage());
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
            LocalDate date = LocalDate.of(year,month,1);
            Counter counter = counterDAO.getCounterByName(counterName).orElseThrow();

            indicationService.addIndicationOfUser(login, counter, date, value);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("Indication added successfully");
        } catch (IncorrectValuesException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(e.getMessage());
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid format for value of counter");
        } catch (YearFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(e.getMessage());
        } catch (MonthFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(e.getMessage());
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred: " + e.getMessage());
        }
    }
}