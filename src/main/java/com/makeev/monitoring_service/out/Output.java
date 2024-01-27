package com.makeev.monitoring_service.out;

/**
 * An interface defining a generic output method for displaying information of type T.
 * @author Evgeniy Makeev
 * @version 1.4
 *
 * @param <T> The type of information to be output.
 */
public interface Output<T> {

    /**
     * Outputs information of type T.
     *
     * @param t The information to be output.
     */
    void output(T t);
}
