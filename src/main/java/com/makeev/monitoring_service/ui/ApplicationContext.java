package com.makeev.monitoring_service.ui;

import com.makeev.monitoring_service.exceptions.*;
import com.makeev.monitoring_service.in.Input;
import com.makeev.monitoring_service.in.InputImpl;
import com.makeev.monitoring_service.model.Counter;
import com.makeev.monitoring_service.service.AdminService;
import com.makeev.monitoring_service.service.IndicationService;
import com.makeev.monitoring_service.utils.ConnectionManagerImpl;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Represents the context of the application, managing indicationService, user authentication, and menus.
 */
public class ApplicationContext {

    /**
     * Service for managing user accounts and indications.
     */
    private final IndicationService indicationService = new IndicationService(new ConnectionManagerImpl());

    /**
     * Service for managing admin-related events and logs.
     */
    private final AdminService adminService = new AdminService(new ConnectionManagerImpl());

    /**
     * Input interface for user interaction.
     */
    private final Input input = new InputImpl();

    /**
     * Console message utility for displaying information to the user.
     */
    private final Messages console = new Messages();

    /**
     * Optional login of the current user.
     */
    private Optional<String> loginOfCurrentUser = Optional.empty();

    /**
     * User option selected by the user.
     */
    private int userOption = -1;

    /**
     * Flag indicating whether the application is still running.
     */
    private boolean appAreWorking = true;

    /**
     * Flags for controlling various menus and options.
     */
    private boolean showAuthorizationMenu = true;
    private boolean showAdminMenu = false;
    private boolean showUserMenu = false;
    private boolean showEnterIndicationMenu = false;
    private boolean showLoginMenu = false;
    private boolean showCurrentMeter = false;
    private boolean showIndicationForMonth = false;
    private boolean showHistoryForUser = false;
    private boolean showLogForUser = false;

    /**
     * Starts the application and enters the main loop for user interaction.
     */
    public void start() {
        console.welcomeMessage();
        while (appAreWorking) {
            if (showAuthorizationMenu) {
                authorizationMenu();
            }
            //if option "Log in" was selected
            if (showLoginMenu) {
                loginMenu();
            }
            //if login as user
            if (showUserMenu) {
                userMenu();
            }
        }
        console.endMessage();
    }

    private void authorizationMenu() {
        console.authorizationMenu();
        if (userOption < 0) {
            userOption = input.getInt(2);
        }

        switch (userOption) {
            case 1, 2 -> { //if option "Log in" was selected
                showAuthorizationMenu = false;
                showLoginMenu = true;
            }
            //if option "Sign in" was selected
            case 0 -> appAreWorking = false;
        }
    }

    private void loginMenu() {
        boolean goBack = false;
        while (loginOfCurrentUser.isEmpty()) {
            console.loginMenu();
            String login = input.getString();
            console.passwordMessage();
            if (userOption == 1) {
                try {
                    indicationService.userDAO.checkCredentials(login, input.getString());
                    console.print("Access is allowed!");
                    loginOfCurrentUser = Optional.of(login);
                    adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                            "Login.");
                    break;
                } catch (VerificationException e) {
                    console.print(e.getMessage());
                }
            } else {
                try {
                    indicationService.userDAO.existByLogin(login);
                    indicationService.addUser(login, input.getString());
                    console.print("Account was created!");
                    loginOfCurrentUser = Optional.of(login);
                    adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                            "Account was created and login.");
                    break;
                } catch (LoginAlreadyExistsException e) {
                    console.print(e.getMessage());
                }
            }
            console.tryOrBackMessage();
            if (input.getInt(2) == 2) {
                goBack = true;
                break;
            }
        }
        if (goBack) {
            showAuthorizationMenu = true;
            showLoginMenu = false;
            userOption = -1;
        } else {
            showUserMenu = true;
            showLoginMenu = false;
            userOption = -1;
        }
    }

    private void userMenu() {
        while (showUserMenu) {
            console.greetingMessage(loginOfCurrentUser.orElseThrow());
            console.userMenu();
            if (userOption < 0) {
                userOption = input.getInt(6);
            }
            switch (userOption) {
                case 1 -> //if "Submit meter of counters" was selected
                        showEnterIndicationMenu = true;
                case 2 ->  //if "Show current meter indications" was selected
                        showCurrentMeter = true;
                case 3 -> //if "Show indications for the selected month" was selected
                        showIndicationForMonth = true;
                case 4 ->  //if "Show indications submission history" was selected
                    showIndicationsSubmissionHistory();
                case 5 -> //if "Admin options" was selected
                        goToAdminOptions();
                case 6 -> //if "Log out" was selected
                        logOut();
                case 0 ->  //if "Exit" was selected
                        System.exit(0);
            }
            //if "Show current meter indications" was selected
            if (showCurrentMeter) {
                currentMeters();
            }
            //if "Submit meter of counters" was selected
            if (showEnterIndicationMenu) {
                enterIndicationMenu();
            }
            //if "Show indications for the selected month" was selected
            if (showIndicationForMonth) {
              indicationForMonth();
            }
            //if "Admin options" was selected
            if (showAdminMenu) {
                adminMenu();
            }
        }
    }

    private void enterIndicationMenu() {
        Counter counter = null;
        while (counter == null) {
            console.setCounterMessage();
            console.getCounters(indicationService.counterDAO.getAll());
            console.choiceCounterMessage();
            userOption = input.getInt(2);
            switch (userOption) {
                case 1 -> {
                    console.getCounters(indicationService.counterDAO.getAll());
                    int index = input.getInt(indicationService.counterDAO.getNumberOfCounters());
                    counter = indicationService.counterDAO.getBy((long) index).orElseThrow();
                }
                case 2 -> {
                    console.setNameOfCounterMessage();
                    try {
                        counter = indicationService.counterDAO.add(input.getString());
                        console.print("A new meter of counters was added.");
                        adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                                "Add a new meter of counters.");
                    } catch (CounterAlreadyExistsException e) {
                        console.print(e.getMessage());
                    }
                }
            }
        }

        LocalDate date = getDate();
        console.setValueMessage();
        Double value = input.getDouble();
        try {
            indicationService.addIndicationOfUser(
                    loginOfCurrentUser.orElseThrow(), counter, date, value);
            console.addSuccessful();
            adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                    "Submitted a meter of counters.");
        } catch (IncorrectValuesException e) {
            console.print(e.getMessage());
            adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                    "Tried to submit a meter of counters.");
        }

        showEnterIndicationMenu = false;
        userOption = -1;
    }

    private void currentMeters() {
        try {
            console.printIndicationOfCounter(indicationService.getCurrentIndication(loginOfCurrentUser.orElseThrow())
                    , "Current meter indications:\n");
        } catch (EmptyException e) {
            console.print(e.getMessage());
        }
        showCurrentMeter = false;
        userOption = -1;
        adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                "Current meter indications was viewed.");
    }

    /**
     * Retrieves all indications for a user for a specific month.
     */
    private void indicationForMonth() {
        LocalDate date = getDate();
        try {
            console.printIndicationOfCounter(indicationService
                            .getAllIndicationsForUserForMonth
                                    (loginOfCurrentUser.orElseThrow(), date),
                    "Indications submission history for "
                            + date.getMonth() + " - " + date.getYear() + ":\n");
        } catch (EmptyException e) {
            console.print(e.getMessage());
        }

        showIndicationForMonth = false;
        userOption = -1;

        adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                "Indications for the selected month was viewed.");
    }

    /**
     * Shows indications submission history for a specific user.
     */
    private void showIndicationsSubmissionHistory() {
        try {
            console.printIndicationOfCounter(indicationService.
                            getAllIndicationsForUser(loginOfCurrentUser.orElseThrow()),
                    "All indications for " + loginOfCurrentUser.orElseThrow() + ":\n");
            userOption = -1;
            adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                    "Indications submission history was viewed.");
        } catch (EmptyException e) {
            throw new RuntimeException(e);
        }
    }
    private void goToAdminOptions() {
        if (indicationService.userDAO.isAdmin(loginOfCurrentUser.orElseThrow())) {
            showAdminMenu = true;
            userOption = -1;
            adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                    "Enter in admin option.");
        } else {
            console.notAdminMessage();
            userOption = -1;
            adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                    "Tried to entered in admin option.");
        }
    }
    private void adminMenu() {
        while (showAdminMenu) {
            console.adminMenu();
            userOption = input.getInt(5);
            switch (userOption) {
                case 1 -> //if "Show indications submission history for all users" was selected
                    showIndicationsSubmissionHistoryForAllUsers();
                case 2 ->  //if "Show indications submission history for user" was selected
                        showHistoryForUser = true;
                case 3 -> //if "Show log for user" was selected
                        showLogForUser = true;
                case 4 -> { //if "Show log" was selected
                    try {
                        console.printUserEvents(adminService.getAllEvents(),
                                "Log for all users:\n");
                    } catch (EmptyException e) {
                        console.print(e.getMessage());
                    }
                    adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                            "Log was viewed.");
                }
                case 5 -> { //if "Back to User menu" was selected
                    userOption = -1;
                    showAdminMenu = false;
                    adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                            "Exit from admin options.");
                }
                case 0 ->  //if "Exit" was selected
                        System.exit(0);
            }
            if (showHistoryForUser) {
                showIndicationsSubmissionHistoryForUser();
            }
            if (showLogForUser) {
                logForUser();
            }
        }
    }

    private void showIndicationsSubmissionHistoryForAllUsers() {
        try {
            console.printAllIndicationOfCounter(indicationService.getAllIndications(),
                    "Indications submission history for all users:\n");
            adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                    "Indications submission history for all users was viewed.");
        } catch (EmptyException e) {
            console.print(e.getMessage());
        }
        userOption = -1;
    }

    private void showIndicationsSubmissionHistoryForUser() {
        console.loginMessage();
        String login = input.getString();
        try {
            if (indicationService.userDAO.getBy(login).isPresent()) {
                console.printIndicationOfCounter(indicationService
                                .getAllIndicationsForUser(login),
                        "Indications submission history for " + login + ":\n");
                adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                        "Indications submission history for user was viewed.");
            } else {
                console.notFoundUserMessage();
                adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                        "Tried to view a indications submission history for user.");
            }
        } catch (EmptyException e) {
            console.print(e.getMessage());
            adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                    "Indications submission history for user was viewed.");
        }
        userOption = -1;
        showHistoryForUser = false;
    }

    private void logForUser() {
        console.loginMessage();
        String login = input.getString();
        if (indicationService.userDAO.getBy(login).isPresent()) {
            try {
                console.printUserEvents(adminService.getAllEventsForUser(login),
                        "Log for " + login + ":\n");
            } catch (EmptyException e) {
                console.print(e.getMessage());
            }
            adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                    "Log for user was viewed.");
        } else {
            console.notFoundUserMessage();
            adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                    "Tried to view a log for user.");
        }
        userOption = -1;
        showLogForUser = false;
    }


    private void logOut() {
        adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                "Logout.");
        loginOfCurrentUser = Optional.empty();
        showAuthorizationMenu = true;
        showUserMenu = false;
        userOption = -1;
    }

    private LocalDate getDate() {
        console.setYearMessage();
        Integer year;
        do {
            year = input.getInteger(4);
        } while (year < 0);

        console.setMonthMessage();
        Integer month;
        do {
            month = input.getInteger(2);
        } while (month <= 0 && month > 12);
        return LocalDate.of(year, month, 1);
    }
}
