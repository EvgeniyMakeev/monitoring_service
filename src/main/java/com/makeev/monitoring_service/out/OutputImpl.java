package com.makeev.monitoring_service.out;

/**
 * An implementation of the Output interface for displaying String information to the console.
 * @author Evgeniy Makeev
 * @version 1.4
 */
public class OutputImpl implements Output<String> {

    /**
     * Outputs a String to the console followed by a newline character.
     *
     * @param s The String information to be output.
     */
    @Override
    public void output(String s){
        System.out.println(s);
    }
}
