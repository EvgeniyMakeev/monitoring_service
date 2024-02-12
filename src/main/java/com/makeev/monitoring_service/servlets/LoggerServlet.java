package com.makeev.monitoring_service.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeev.monitoring_service.aop.annotations.Loggable;
import com.makeev.monitoring_service.dto.UserEventDTO;
import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.mappers.UserEventMapper;
import com.makeev.monitoring_service.model.UserEvent;
import com.makeev.monitoring_service.service.AdminService;
import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@Loggable
@WebServlet("/logs")
public class LoggerServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private AdminService adminService;
    private UserEventMapper userEventMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
        adminService = new AdminService(new ConnectionManagerImpl());
        userEventMapper = userEventMapper.INSTANCE;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<UserEvent> userEvents = adminService.getAllEvents();

            List<UserEventDTO> userEventDTOs = userEvents.stream()
                    .map(userEventMapper::toDTO)
                    .toList();

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getOutputStream(), userEventDTOs);
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred: " + e.getMessage());
        }
    }
}