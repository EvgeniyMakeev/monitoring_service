package com.makeev.monitoring_service.servlets.users_servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeev.monitoring_service.aop.annotations.Loggable;
import com.makeev.monitoring_service.dao.UserDAO;
import com.makeev.monitoring_service.dto.UserDTO;
import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.mappers.UserMapper;
import com.makeev.monitoring_service.model.User;
import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@Loggable
@WebServlet("/users/getUser")
public class GetUserByLoginServlet extends HttpServlet {


    private ObjectMapper objectMapper;
    private UserDAO userDAO;
    private UserMapper userMapper;


    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
        userDAO = new UserDAO(new ConnectionManagerImpl());
        userMapper = userMapper.INSTANCE;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String login = req.getParameter("login");
            if (login == null || login.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Login parameter is required");
                return;
            }

            Optional<User> user = userDAO.getUserByLogin(login);
            if (user.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("User with this login not found");
                return;
            }
            UserDTO userDTO = userMapper.toDTO(user.get());
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getOutputStream(), userDTO);
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred: " + e.getMessage());
        }
    }
}