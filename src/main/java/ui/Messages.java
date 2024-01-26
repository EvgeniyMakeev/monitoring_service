package ui;

import out.Output;
import out.OutputImpl;

public class Messages {
    private final Output<String> console = new OutputImpl();

    public void notAdminMessage() {
        String NOT_ADMIN = "You do not have administrator rights.";
        console.output(NOT_ADMIN);
    }

    public void welcomeMessage() {
        String WELCOME = "Welcome!";
        console.output(WELCOME);
    }
    public void authorizationMenu() {
        String AUTHORIZATION_MENU = "======= MAIN MENU =======\n" +
                "1. Log in.\n" +
                "2. Sign in.\n\n" +
                "0. Exit";
        console.output(AUTHORIZATION_MENU);
    }
    public void userMenu() {
        String USER_MENU = "=========== USER MENU ===========\n" +
                "1. Show current meter indications.\n" +
                "2. Submit meter of counters.\n" +
                "3. Show indications for the selected month.\n" +
                "4. Show indications submission history.\n" +
                "5. Admin options.\n" +
                "6. Log out.\n\n" +
                "0. Exit";
        console.output(USER_MENU);
    }
    public void loginMessage() {
        String LOGIN = "Enter Login: ";
        console.output(LOGIN);
    }
    public void passwordMessage() {
        String PASSWORD = "Please enter password:";
        console.output(PASSWORD);
    }
    public void wrongLoginMessage() {
        String WRONG_LOGIN = "Wrong login or password.\n" +
                "1 - Try again.\n" +
                "2 - Back.";
        console.output(WRONG_LOGIN);
    }
    public void setYearMessage() {
        String YEAR = "Please enter year:";
        console.output(YEAR);
    }
    public void setMonthMessage() {
        String MONTH = "Please enter month:";
        console.output(MONTH);
    }

    public void endMessage() {
        String GOODBYE = "Goodbye!\n";
        console.output(GOODBYE);
    }
    public void greetingMessage(String loginOfCurrentUser) {
        console.output("\nWelcome, " + loginOfCurrentUser + "!");
    }
    public void print(String s) {
        console.output(s);
    }

    public void setValueMessage() {
        String VALUE = "Please enter value:";
        console.output(VALUE);
    }

    public void setCounterMessage() {
        String COUNTER_TYPE = "Counter of what?\n" +
                "1 - Heating.\n" +
                "2 - Hot watter.\n" +
                "3 - Cold watter.";
        console.output(COUNTER_TYPE);
    }

    public void addSuccessful() {
        String ADD_SUCCESS = "Meter readings have been sent successfully.";
        console.output(ADD_SUCCESS);
    }
    public void addNotSuccessful() {
        String ADD_NOT_SUCCESS = "Meter readings have not been sent.\n" +
                "Testimony can be submitted only once a month\n" +
                "and it cannot be less than the current one.";
        console.output(ADD_NOT_SUCCESS);
    }

    public void adminMenu() {
        String ADMIN_MENU = "=========== ADMIN MENU ===========\n" +
                "1. Show indications submission history for all users.\n" +
                "2. Show indications submission history for user.\n" +
                "3. Show log for user.\n" +
                "4. Show log.\n" +
                "5. Back to User menu.\n\n" +
                "0. Exit";
        console.output(ADMIN_MENU);
    }
    public void loginMenu() {
        String LOGIN_MENU = "======== LOGIN ========\n"
                + "Enter your Login: ";
        console.output(LOGIN_MENU);
    }

    public void notFoundUserMessage() {
        String USER_NOT_FOUND = "User not found.";
        console.output(USER_NOT_FOUND);
    }
}
