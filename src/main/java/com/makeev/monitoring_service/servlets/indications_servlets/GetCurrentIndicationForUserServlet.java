package com.makeev.monitoring_service.servlets.indications_servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeev.monitoring_service.aop.annotations.Loggable;
import com.makeev.monitoring_service.dao.UserDAO;
import com.makeev.monitoring_service.dto.IndicationsOfUserDTO;
import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.exceptions.EmptyException;
import com.makeev.monitoring_service.exceptions.UserNotFoundException;
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
import java.util.List;

@WebServlet("/indications/currentIndicationForUser")
public class GetCurrentIndicationForUserServlet extends HttpServlet {
    public static final String CONTENT_TYPE = "application/json";

    private ObjectMapper objectMapper;
    private IndicationService indicationService;
    private final IndicationsOfUserMapper indicationsOfUserMapper = IndicationsOfUserMapper.INSTANCE;
    private UserDAO userDAO;

    @Loggable
    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
        indicationService = new IndicationService(new ConnectionManagerImpl());
        userDAO = new UserDAO(new ConnectionManagerImpl());
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
                List<IndicationsOfUser> indicationsOfUsers = indicationService.getCurrentIndication(login);

                List<IndicationsOfUserDTO> indicationDTOs = indicationsOfUsers
                        .stream()
                        .map(indicationsOfUserMapper::toDTO)
                        .toList();

                resp.setContentType(CONTENT_TYPE);
                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(resp.getOutputStream(), indicationDTOs);
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