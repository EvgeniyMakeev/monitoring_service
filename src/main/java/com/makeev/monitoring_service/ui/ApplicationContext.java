package com.makeev.monitoring_service.ui;

import com.makeev.monitoring_service.constants.CounterOf;
import com.makeev.monitoring_service.in.Input;
import com.makeev.monitoring_service.in.InputImpl;
import com.makeev.monitoring_service.service.AccountService;
import com.makeev.monitoring_service.service.AccountServiceImpl;
import com.makeev.monitoring_service.service.AdminService;
import com.makeev.monitoring_service.service.AdminServiceImpl;

import java.util.Optional;

/**
 * Represents the context of the application, managing services, user authentication, and menus.
 * @author Evgeniy Makeev
 * @version 1.4
 */
public class ApplicationContext {

    /**
     * Service for managing user accounts and indications.
     */
    private final AccountService accountService = new AccountServiceImpl();

    /**
     * Service for managing admin-related events and logs.
     */
    private final AdminService adminService = new AdminServiceImpl();

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
                console.authorizationMenu();
            }
            if (userOption < 0) {
                userOption = input.getInt(2);
            }

            switch (userOption) {
                case 1 -> { //if option "Log in" was selected
                    showAuthorizationMenu = false;
                    showLoginMenu = true;
                }
                case 2 -> { //if option "Sign in" was selected
                    showAuthorizationMenu = false;
                    showLoginMenu = true;
                }
                case 0 -> appAreWorking = false;
            }
            //if option "Log in" was selected
            if (showLoginMenu) {
                boolean goBack = false;
                while (loginOfCurrentUser.isEmpty()) {
                    console.loginMenu();
                    String login = input.getString();
                    console.passwordMessage();
                    if (userOption == 1) {
                        if (accountService.findAndCheckUser(login, input.getString())) {
                            console.print("Access is allowed!");
                            loginOfCurrentUser = Optional.of(login);
                            adminService.addEvent(accountService.getUserByLogin(loginOfCurrentUser),
                                    "Login.");
                            break;
                        } else {
                            console.print("Access is denied!");
                            console.wrongLoginMessage();
                            console.tryOrBackMessage();
                        }
                    } else {
                        if (accountService.addUser(login, input.getString())) {
                            console.print("Account was created!");
                            loginOfCurrentUser = Optional.of(login);
                            adminService.addEvent(accountService.getUserByLogin(loginOfCurrentUser),
                                    "Account was created and login.");
                            break;
                        } else {
                            console.print("A user with this login already exists!");
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

            //if login as user
            if (showUserMenu) {
                while (showUserMenu) {
                    console.greetingMessage(loginOfCurrentUser.orElseThrow());
                    console.userMenu();
                    if (userOption < 0) {
                        userOption = input.getInt(6);
                    }
                    switch (userOption) {
                        case 1 ->  //if "Show current meter indications" was selected
                                showCurrentMeter = true;
                        case 2 -> //if "Submit meter of counters" was selected
                                showEnterIndicationMenu = true;
                        case 3 -> //if "Show indications for the selected month" was selected
                                showIndicationForMonth = true;
                        case 4 -> { //if "Show indications submission history" was selected
                                console.print(accountService.getAllIndicationsForUser(loginOfCurrentUser.orElseThrow()));
                                userOption = -1;
                                adminService.addEvent(accountService.getUserByLogin(loginOfCurrentUser),
                                        "Indications submission history was viewed.");
                        }
                        case 5 -> { //if "Admin options" was selected
                            if (accountService.isAdmin(loginOfCurrentUser.orElseThrow())) {
                                showAdminMenu = true;
                                userOption = -1;
                                adminService.addEvent(accountService.getUserByLogin(loginOfCurrentUser),
                                        "Enter com.makeev.in admin option.");
                            } else {
                                console.notAdminMessage();
                                userOption = -1;
                                adminService.addEvent(accountService.getUserByLogin(loginOfCurrentUser),
                                        "Tried to entered com.makeev.in admin option.");
                            }
                        }
                        case 6 -> { //if "Log com.makeev.out" was selected
                                adminService.addEvent(accountService.getUserByLogin(loginOfCurrentUser),
                                        "Logout.");
                                loginOfCurrentUser = Optional.empty();
                                showAuthorizationMenu = true;
                                showUserMenu = false;
                                userOption = -1;
                                }
                        case 0 ->  //if "Exit" was selected
                                System.exit(0);
                    }

                    //if "Show current meter indications" was selected
                    if (showCurrentMeter) {
                        console.setCounterMessage();
                        CounterOf counterOf = null;
                        userOption = input.getInt(3);
                        switch (userOption) {
                            case 1 -> counterOf = CounterOf.HEATING;
                            case 2 -> counterOf = CounterOf.HOT_WATER;
                            case 3 -> counterOf = CounterOf.COLD_WATER;
                        }

                        console.print(accountService.getCurrentMeter(loginOfCurrentUser.orElseThrow(), counterOf));

                        showCurrentMeter = false;
                        userOption = -1;
                        adminService.addEvent(accountService.getUserByLogin(loginOfCurrentUser),
                                "Current meter indications was viewed.");
                    }

                    //if "Submit meter of counters" was selected
                    if (showEnterIndicationMenu) {
                        console.setCounterMessage();
                        CounterOf counterOf = null;
                        userOption = input.getInt(3);
                        switch (userOption) {
                            case 1 -> counterOf = CounterOf.HEATING;
                            case 2 -> counterOf = CounterOf.HOT_WATER;
                            case 3 -> counterOf = CounterOf.COLD_WATER;
                        }

                        console.setYearMessage();
                        Integer year;
                        do {
                            year = input.getInteger(4, 0, 9999);
                        } while (year < 0);

                        console.setMonthMessage();
                        Integer month;
                        do {
                            month = input.getInteger(2, 1, 12);
                        } while (month <= 0 && month > 12);

                        console.setValueMessage();
                        Double value = input.getDouble();

                        if (accountService.addIndicationOfUser(loginOfCurrentUser.orElseThrow(), year, month, counterOf, value) == 1) {
                            console.addSuccessful();
                            adminService.addEvent(accountService.getUserByLogin(loginOfCurrentUser),
                                    "Submitted a meter of counters.");
                        } else {
                            console.addNotSuccessful();
                            adminService.addEvent(accountService.getUserByLogin(loginOfCurrentUser),
                                    "Tried to submit a meter of counters.");
                        }

                        showEnterIndicationMenu = false;
                        userOption = -1;
                    }

                    //if "Show indications for the selected month" was selected
                    if (showIndicationForMonth) {
                        console.setYearMessage();
                        Integer year;
                        do {
                            year = input.getInteger(4, 0, 9999);
                        } while (year < 0);

                        console.setMonthMessage();
                        Integer month;
                        do {
                            month = input.getInteger(2, 1, 12);
                        } while (month <= 0 && month > 12);


                        console.print(accountService.getAllIndicationsForUserForMonth
                                (loginOfCurrentUser.orElseThrow(), year, month));

                        showIndicationForMonth = false;
                        userOption = -1;

                        adminService.addEvent(accountService.getUserByLogin(loginOfCurrentUser),
                                "Indications for the selected month was viewed.");
                    }

                    //if "Admin options" was selected
                    if (showAdminMenu) {
                        while (showAdminMenu) {
                            console.adminMenu();
                            userOption = input.getInt(5);
                            switch (userOption) {
                                case 1 -> { //if "Show indications submission history for all users" was selected
                                        console.print(accountService.getAllIndications());
                                        userOption = -1;
                                        adminService.addEvent(accountService.getUserByLogin(loginOfCurrentUser),
                                            "Indications submission history for all users was viewed.");
                                    }
                                case 2 ->  //if "Show indications submission history for user" was selected
                                        showHistoryForUser = true;
                                case 3 -> //if "Show log for user" was selected
                                        showLogForUser = true;
                                case 4 -> { //if "Show log" was selected
                                        console.print(adminService.getAllEvents());
                                        adminService.addEvent(accountService.getUserByLogin(loginOfCurrentUser),
                                            "Log was viewed.");
                                        }
                                case 5 -> { //if "Back to User menu" was selected
                                        userOption = -1;
                                        showAdminMenu = false;
                                        adminService.addEvent(accountService.getUserByLogin(loginOfCurrentUser),
                                            "Exit from admin options.");
                                        }
                                case 0 ->  //if "Exit" was selected
                                        System.exit(0);
                            }
                            if (showHistoryForUser) {
                                console.loginMessage();
                                String login = input.getString();
                                if (accountService.findUser(login)) {
                                    console.print(accountService.getAllIndicationsForUser(login));
                                    adminService.addEvent(accountService.getUserByLogin(loginOfCurrentUser),
                                            "Indications submission history for user was viewed.");
                                } else {
                                    console.notFoundUserMessage();
                                    adminService.addEvent(accountService.getUserByLogin(loginOfCurrentUser),
                                            "Tried to view a indications submission history for user.");
                                }
                                userOption = -1;
                                showHistoryForUser = false;
                            }
                            if (showLogForUser) {
                                console.loginMessage();
                                String login = input.getString();
                                if (accountService.findUser(login)) {
                                    console.print(adminService.getAllEventsForUser(login));
                                    adminService.addEvent(accountService.getUserByLogin(loginOfCurrentUser),
                                            "Log for user was viewed.");
                                } else {
                                    console.notFoundUserMessage();
                                    adminService.addEvent(accountService.getUserByLogin(loginOfCurrentUser),
                                            "Tried to view a log for user.");
                                }
                                userOption = -1;
                                showLogForUser = false;
                            }
                        }
                    }
                }
            }
        }
        console.endMessage();
    }
}
