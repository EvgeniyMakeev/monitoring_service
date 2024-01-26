package in;

import java.util.Scanner;

public class InputImpl implements Input {
    private final Scanner input = new Scanner(System.in);

    @Override
    public int getInt(int max) {
        //option that user entered
        String optionString;

        //option that will convert for the system
        int option = -1;

        do {
            optionString = input.nextLine();

            if (optionString.matches("[0-" + max + "]+") && optionString.length() <= 1) {
                option = Integer.parseInt(optionString);
            } else {
                System.out.println("Enter only digits 0 - " + max);
            }
        } while (!optionString.matches("[0-" + max + "]+") || optionString.length() > 1);
        return option;
    }

    @Override
    public String getString() {
        return input.next();
    }

    @Override
    public Double getDouble() {
        String str = "";
        Double result = (double) -1;
        boolean scan = true;
        while (scan) {
            str = input.nextLine();
            result = Double.parseDouble(str);
                if (result >= 0) {
                    scan = false;
                }
            }
        return result;
    }

    @Override
    public Integer getInteger(int maxLength, int min, int max) {
        String str = "";
        Integer result = -1;
        boolean scan = true;
            while (scan) {
                str = input.nextLine();
                if (str.length() == maxLength) {
//                    for (int i = 0; i < maxLength; i++) {
//                        scan = !Character.isDigit(str.charAt(i));
//                    }
                    result = Integer.parseInt(str);
                    if (result >= min && result <= max) {
                        scan = false;
                    }
                }
            }
        return result;
    }

}
