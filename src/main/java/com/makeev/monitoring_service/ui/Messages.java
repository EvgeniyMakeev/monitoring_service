package com.makeev.monitoring_service.ui;

import com.makeev.monitoring_service.out.Output;
import com.makeev.monitoring_service.out.OutputImpl;

/**
 * Represents various messages and menus displayed to the user.
 * @author Evgeniy Makeev
 * @version 1.4
 */
public class Messages {
    private final Output<String> console = new OutputImpl();

    public void notAdminMessage() {
        console.output("You do not have administrator rights.");
    }

    /**
     * Displays a welcome message to the user.
     */
    public void welcomeMessage() {
        console.output("Welcome!");
    }

    /**
     * Displays the authorization menu to the user.
     */
    public void authorizationMenu() {
        console.output("======= MAIN MENU =======\n" +
                "1. Log in.\n" +
                "2. Sign in.\n\n" +
                "0. Exit");
    }
    public void userMenu() {
        console.output("=========== USER MENU ===========\n" +
                "1. Show current meter indications.\n" +
                "2. Submit meter of counters.\n" +
                "3. Show indications for the selected month.\n" +
                "4. Show indications submission history.\n" +
                "5. Admin options.\n" +
                "6. Log out.\n\n" +
                "0. Exit");
    }
    public void loginMessage() {
        console.output("Enter Login: ");
    }
    public void passwordMessage() {
        console.output("Please enter password:");
    }
    public void wrongLoginMessage() {
        console.output("Wrong login or password.\n");
    }
    public void tryOrBackMessage() {
        console.output("1. Try again.\n" +
                "2. Back.");
    }
    public void setYearMessage() {
        console.output("Please enter year (4 digits):");
    }
    public void setMonthMessage() {
        console.output("Please enter month (2 digits):");
    }
    public void endMessage() {
        console.output("Goodbye!\n");
    }
    public void greetingMessage(String loginOfCurrentUser) {
        console.output("\nWelcome, " + loginOfCurrentUser + "!");
    }
    public void setValueMessage() {
        console.output("Please enter value:");
    }
    public void setCounterMessage() {
        console.output("Counter of what?\n" +
                "1. Heating.\n" +
                "2. Hot watter.\n" +
                "3. Cold watter.");
    }
    public void addSuccessful() {
        console.output("Meter readings have been sent successfully.");
    }
    public void addNotSuccessful() {
        console.output("Meter readings have not been sent.\n" +
                "Testimony can be submitted only once a month\n" +
                "and it cannot be less than the current one.");
    }
    public void adminMenu() {
        console.output("=========== ADMIN MENU ===========\n" +
                "1. Show indications submission history for all users.\n" +
                "2. Show indications submission history for user.\n" +
                "3. Show log for user.\n" +
                "4. Show log.\n" +
                "5. Back to User menu.\n\n" +
                "0. Exit");
    }
    public void loginMenu() {
        console.output("Enter your Login: ");
    }
    public void notFoundUserMessage() {
        console.output("User not found.");
    }
    public void print(String s) {
        console.output(s);
    }
}
