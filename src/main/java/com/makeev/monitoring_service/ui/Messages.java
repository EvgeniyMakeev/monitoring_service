package com.makeev.monitoring_service.ui;

import com.makeev.monitoring_service.exceptions.EmptyException;
import com.makeev.monitoring_service.model.Counter;
import com.makeev.monitoring_service.model.Indication;
import com.makeev.monitoring_service.model.User;
import com.makeev.monitoring_service.model.UserEvent;
import com.makeev.monitoring_service.out.Output;
import com.makeev.monitoring_service.out.OutputImpl;

import java.util.List;
import java.util.Map;

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
        console.output("""
                ======= MAIN MENU =======
                1. Log in.
                2. Sign in.
                
                0. Exit
                """);
    }
    public void userMenu() {
        console.output("""
                =========== USER MENU ===========
                1. Submit meter of counters.
                2. Show current meter indications.
                3. Show indications for the selected month.
                4. Show indications submission history.
                5. Admin options.
                6. Log out.
                
                0. Exit
                """);
    }
    public void loginMessage() {
        console.output("Enter Login: ");
    }
    public void passwordMessage() {
        console.output("Please enter password:");
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
        console.output("Counter of what?");
    }
    public void choiceCounterMessage() {
        console.output("""
                1. Select from the list.
                2. Add a new counter.
                """);
    }
    public void setNameOfCounterMessage() {
        console.output("Write the name of the counter: ");
    }

    public void getCounters(List<Counter> listOfCounter) {
        StringBuilder stringBuilder = new StringBuilder();
        int n = 1;
        for (Counter counter : listOfCounter) {
            stringBuilder.append(n).append(". ").append(counter.name()).append("\n");
            n++;
        }
        console.output(stringBuilder.toString());
    }
    public void addSuccessful() {
        console.output("Meter readings have been sent successfully.");
    }

    public void adminMenu() {
        console.output("""
                =========== ADMIN MENU ===========
                1. Show indications submission history for all users.
                2. Show indications submission history for user.
                3. Show log for user.
                4. Show log.
                5. Back to User menu
                
                0. Exit
                """);
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

    public void printIndicationOfCounter(Map<Counter,
            List<Indication>> mapOfIndicationOfCounter, String message) {
        StringBuilder result = new StringBuilder(message);
        mapOfIndicationOfCounter.forEach((k,v) -> {
            for (Indication indication : v) {
                result.append(k.name())
                        .append(" | ")
                        .append(indication.date().getYear())
                        .append(" - ")
                        .append(indication.date().getMonth())
                        .append(" | ")
                        .append(indication.value())
                        .append("\n");
            }
        });
        console.output(result.toString());
    }

    public void printAllIndicationOfCounter(List<User> listOfUsers, String message) throws EmptyException {
        if (listOfUsers.size() < 2) {
            throw new EmptyException();
        }
        StringBuilder result = new StringBuilder(message);
        for (User user : listOfUsers) {
            if (user.mapOfIndicationOfCounter().isEmpty()) {
                break;
            } else {
                result.append(user.login())
                        .append(" | ");
                user.mapOfIndicationOfCounter().forEach((k,v) -> {
                    for (Indication indication : v) {
                        result.append(k.name())
                                .append(" | ")
                                .append(indication.date().getYear())
                                .append(" - ")
                                .append(indication.date().getMonth())
                                .append(" | ")
                                .append(indication.value())
                                .append("\n");
                    }
                });
            }
        }
        console.output(result.toString());
    }

    public void printCurrentMeters(Map<Counter, Indication> mapOfCurrentIndication, String message) {
        StringBuilder result = new StringBuilder(message);
        mapOfCurrentIndication.forEach((k,v) -> result.append(k.name())
                .append(" | ")
                .append(v.date().getYear())
                .append(" - ")
                .append(v.date().getMonth())
                .append(" | ")
                .append(v.value())
                .append("\n"));
        console.output(result.toString());
    }

    public void printUserEvents(List<UserEvent> listOfUserEvent, String message) {
        StringBuilder result = new StringBuilder(message);
        for (UserEvent userEvent : listOfUserEvent) {
            result.append(userEvent.date())
                    .append(" | ")
                    .append(userEvent.login())
                    .append(" | ")
                    .append(userEvent.message())
                    .append("\n");
        }
        console.output(result.toString());
    }
}
