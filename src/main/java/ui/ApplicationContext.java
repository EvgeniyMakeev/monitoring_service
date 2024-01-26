package ui;

import constants.CounterOf;
import in.Input;
import in.InputImpl;
import service.AccountService;
import service.AccountServiceImpl;
import service.AdminService;
import service.AdminServiceImpl;

import java.util.Optional;

public class ApplicationContext {
    private final AccountService accountService = new AccountServiceImpl();
    private final AdminService adminService = new AdminServiceImpl();
    private final Input input = new InputImpl();
    private final Messages console = new Messages();
    private Optional<String> loginOfCurrentUser = Optional.empty();

    private int userOption = -1;
    private boolean appAreWorking = true;
    private boolean showAuthorizationMenu = true;
    private boolean showAdminMenu = false;
    private boolean showUserMenu = false;
    private boolean showEnterIndicationMenu = false;
    private boolean showLoginMenu = false;
    private boolean showCurrentMeter = false;
    private boolean showIndicationForMonth = false;
    private boolean showHistoryForUser = false;
    private boolean showLogForUser = false;

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
                            adminService.addEvent(loginOfCurrentUser.get(), "Login.");
                            break;
                        } else {
                            console.print("Access is denied!");
                            console.wrongLoginMessage();
                        }
                    } else {
                        if (accountService.addUser(login, input.getString())) {
                            console.print("Account was created!");
                            loginOfCurrentUser = Optional.of(login);
                            adminService.addEvent(loginOfCurrentUser.get(), "Account was created and login.");
                            break;
                        } else {
                            console.print("A user with this login already exists!");
                        }
                    }
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
                                adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                                        "Indications submission history was viewed.");
                        }
                        case 5 -> { //if "Admin options" was selected
                            if (accountService.isAdmin(loginOfCurrentUser.orElseThrow())) {
                                showAdminMenu = true;
                                userOption = -1;
                                adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                                        "Enter in admin option.");
                            } else {
                                console.notAdminMessage();
                                userOption = -1;
                                adminService.addEvent(loginOfCurrentUser.get(),
                                        "Tried to entered in admin option.");
                            }
                        }
                        case 6 -> { //if "Log out" was selected
                                adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                                    "Logout.");loginOfCurrentUser = Optional.empty();
                                showAuthorizationMenu = true;
                                showUserMenu = false;
                                userOption = -1;
                                }
                        case 0 ->  //if "Exit" was selected
                                appAreWorking = false;
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
                        adminService.addEvent(loginOfCurrentUser.get(), "Current meter indications was viewed.");
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
                            adminService.addEvent(loginOfCurrentUser.get(), "Submitted a meter of counters.");
                        } else {
                            console.addNotSuccessful();
                            adminService.addEvent(loginOfCurrentUser.get(), "Tried to submit a meter of counters.");
                        }

                        showEnterIndicationMenu = false;
                        userOption = -1;
                    }

                    //if "Show indications for the selected month" was selected
                    if (showIndicationForMonth) {
                        console.setYearMessage();
                        Integer year = input.getInt(4);
                        console.setMonthMessage();
                        Integer month = input.getInt(2);

                        console.print(accountService
                                .getAllIndicationsForUserForMonth(loginOfCurrentUser.orElseThrow(), year, month));

                        showIndicationForMonth = false;
                        userOption = -1;

                        adminService.addEvent(loginOfCurrentUser.get(), "Indications for the selected month was viewed.");
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
                                        adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                                            "Indications submission history for all users was viewed.");
                                    }
                                case 2 ->  //if "Show indications submission history for user" was selected
                                        showHistoryForUser = true;
                                case 3 -> //if "Show log for user" was selected
                                        showLogForUser = true;
                                case 4 -> { //if "Show log" was selected
                                        console.print(adminService.getAllEvents());
                                        adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                                            "Log was viewed.");
                                        }
                                case 5 -> { //if "Back to User menu" was selected
                                        showAdminMenu = false;
                                        userOption = -1;
                                        adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                                            "Exit from admin options.");
                                        }
                                case 0 ->  //if "Exit" was selected
                                        appAreWorking = false;
                            }
                            if (showHistoryForUser) {
                                console.loginMessage();
                                String login = input.getString();
                                if (accountService.findUser(login)) {
                                    console.print(accountService.getAllIndicationsForUser(login));
                                    adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                                            "Indications submission history for user was viewed.");
                                } else {
                                    console.notFoundUserMessage();
                                    adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                                            "Tried to view a indications submission history for user.");
                                }
                                userOption = -1;
                            }
                            if (showLogForUser) {
                                console.loginMessage();
                                String login = input.getString();
                                if (accountService.findUser(login)) {
                                    console.print(adminService.getAllEventsForUser(login));
                                    adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                                            "Log for user was viewed.");
                                } else {
                                    console.notFoundUserMessage();
                                    adminService.addEvent(loginOfCurrentUser.orElseThrow(),
                                            "Tried to view a log for user.");
                                }
                                userOption = -1;
                            }
                        }
                    }
                }
            }
        }
        console.endMessage();
    }
}
