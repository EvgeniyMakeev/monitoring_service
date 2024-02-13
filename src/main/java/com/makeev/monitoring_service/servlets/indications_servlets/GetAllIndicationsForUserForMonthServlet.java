package com.makeev.monitoring_service.servlets.indications_servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeev.monitoring_service.aop.annotations.Loggable;
import com.makeev.monitoring_service.dao.UserDAO;
import com.makeev.monitoring_service.dto.IndicationsOfUserDTO;
import com.makeev.monitoring_service.exceptions.*;
import com.makeev.monitoring_service.in.Input;
import com.makeev.monitoring_service.mappers.IndicationsOfUserMapper;
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

@WebServlet("/indications/indicationsForUserForMonth")
public class GetAllIndicationsForUserForMonthServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private IndicationService indicationService;
    private final IndicationsOfUserMapper indicationsOfUserMapper = IndicationsOfUserMapper.INSTANCE;
    private UserDAO userDAO;
    private Input input;

    @Loggable
    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
        indicationService = new IndicationService(new ConnectionManagerImpl());
        userDAO = new UserDAO(new ConnectionManagerImpl());
        input = new Input();
    }

    @Loggable
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String login = req.getParameter("login");
            if (login == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Login parameter is required");
                return;
            }
            userDAO.getUserByLogin(login);
            try {
                String yearStr = req.getParameter("year");
                String monthStr = req.getParameter("month");

                if (yearStr == null || monthStr == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("One or more parameters are missing");
                    return;
                }
                int year = input.getYear(yearStr);
                int month = input.getMonth(monthStr);
                LocalDate date = LocalDate.of(year, month, 1);

                List<IndicationsOfUser> indicationsOfUsers = indicationService.getAllIndicationsForUserForMonth(login, date);

                List<IndicationsOfUserDTO> indicationsOfUserDTOs = indicationsOfUsers
                        .stream()
                        .map(indicationsOfUserMapper::toDTO)
                        .toList();

                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(resp.getOutputStream(), indicationsOfUserDTOs);
            } catch (YearFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(e.getMessage());
            } catch (MonthFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(e.getMessage());
            } catch (EmptyException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("No indications found for the user");
            } catch (DaoException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Error occurred: " + e.getMessage());
            }
        } catch (UserNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(e.getMessage());
        }
    }
}