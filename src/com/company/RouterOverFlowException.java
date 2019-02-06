package com.company;

/**
 * The RouterOverFlowException Class defines the exception thrown when no routers are available to add to.
 *
 * @author Kirat Singh, ID: 112320621, E-mail: kirat.singh@stonybrook.edu
 */
public class RouterOverFlowException extends Exception {

    /**
     * The constructor for the class, it calls the standard super constructor for all exceptions.
     * @param s
     *      The message sent when the Exception is thrown.
     */
    public RouterOverFlowException(String s){
        super(s);
    }
}
