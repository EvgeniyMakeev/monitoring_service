package com.makeev.monitoring_service.servlets.indications_servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeev.monitoring_service.aop.annotations.Loggable;
import com.makeev.monitoring_service.dto.IndicationsOfUserDTO;
import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.exceptions.EmptyException;
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
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/indications/indicationsForUserForMonth")
public class GetAllIndicationsForUserForMonthServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private IndicationService indicationService;

    @Loggable
    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
        indicationService = new IndicationService(new ConnectionManagerImpl());
    }

    @Loggable
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String login = req.getParameter("login");
            String dateString = req.getParameter("date");
            if (login == null || login.isEmpty() || dateString == null || dateString.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Both login and date parameters are required");
                return;
            }

            LocalDate date = LocalDate.parse(dateString);
            List<IndicationsOfUser> indications = indicationService.getAllIndicationsForUserForMonth(login, date);

            // Convert List<IndicationsOfUser> to List<IndicationsOfUserDTO>
            List<IndicationsOfUserDTO> indicationDTOs = indications.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getOutputStream(), indicationDTOs);
        } catch (EmptyException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("No indications found for the user for the specified month");
        } catch (DateTimeParseException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid format for date parameter");
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred: " + e.getMessage());
        }
    }

    private IndicationsOfUserDTO convertToDTO(IndicationsOfUser indication) {
        // Implement the logic to convert IndicationsOfUser entity to IndicationsOfUserDTO
        return new IndicationsOfUserDTO(indication.login(), indication.counter(), indication.indication());
    }
}