package com.makeev.monitoring_service.servlets.indications_servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeev.monitoring_service.aop.annotations.Loggable;
import com.makeev.monitoring_service.dto.IndicationsOfUserDTO;
import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.exceptions.EmptyException;
import com.makeev.monitoring_service.mappers.IndicationsOfUserMapper;
import com.makeev.monitoring_service.service.IndicationService;
import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/indications/indicationsForUser")
public class GetAllIndicationsForUserServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private IndicationService indicationService;
    private IndicationsOfUserMapper indicationsOfUserMapper;

    @Loggable
    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
        indicationService = new IndicationService(new ConnectionManagerImpl());
        indicationsOfUserMapper = Mappers.getMapper(IndicationsOfUserMapper.class);
    }

    @Loggable
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String login = req.getParameter("login");
            if (login == null || login.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Login parameter is required");
                return;
            }

            List<IndicationsOfUserDTO> indicationDTOs = indicationService.getAllIndicationsForUser(login)
                    .stream()
                    .map(indicationsOfUserMapper::toDTO)
                    .collect(Collectors.toList());

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getOutputStream(), indicationDTOs);
        } catch (EmptyException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("No indications found for the user");
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred: " + e.getMessage());
        }
    }
}