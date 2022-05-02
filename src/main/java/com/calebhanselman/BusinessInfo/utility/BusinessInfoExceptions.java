package com.calebhanselman.BusinessInfo.utility;

/**
 * This will act as the applications holder class for all custom exceptions that would be thrown.
 *
 * To add a new custom exception please add it here as public static and inherit the default exception.
 *
 * This is usefull for custom handlers for controllers and in the future other aspects that would be created if this
 * was a full scale app.
 */
public class BusinessInfoExceptions {

    public static class ZipCodeException extends Exception{
        public ZipCodeException(String message){
            super(message);
        }
    }
}
