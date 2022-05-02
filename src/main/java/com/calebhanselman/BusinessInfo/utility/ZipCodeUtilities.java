package com.calebhanselman.BusinessInfo.utility;

/**
 * Static utility class for transforming valid zip code strings into 5 digit zip codes as well as checking to see
 * what a valid ZIP code string is.
 */
public class ZipCodeUtilities {

    public static final String validZipCodeMessage = "A valid US zip code is in the range from 00001 – 99950. 9 digit zip codes are also valid as long if they are in the form of 5 digits in the valid range followed by 4 digits separated by a space or hyphen.";

    /**
     * Attempts to extract the 5 digit zipcode from a given string.
     * @param zipcodeStr the given zipcode as a string
     * @return the equivalent 5 digit zipcode
     * @throws BusinessInfoExceptions.ZipCodeException If the given string is not a valid us zipcode by USPS standards
     */
    public static int getZipCodeAs5Digits(String zipcodeStr) throws BusinessInfoExceptions.ZipCodeException{
        if(isValidZipCode(zipcodeStr)){
            return Integer.parseInt(zipcodeStr.substring(0,5));
        }
        throw new BusinessInfoExceptions.ZipCodeException(validZipCodeMessage);
    }

    /**
     * Checks that a string is valid for a US area.
     * @param zipcodeStr String reperesntation of the zipcode
     * @return if the zip code is valid or not
     */
    public static boolean isValidZipCode(String zipcodeStr) {
        // simple 5 digit zipcode
        if(zipcodeStr.length() == 5){
            return isValid5DigitZipCode(zipcodeStr);
        }
        // 9 digit zip code
        else if(zipcodeStr.length() == 10){
            if(zipcodeStr.charAt(5) != ' ' && zipcodeStr.charAt(5) != '-' ){
                return false;
            }
            // Make sure that the first 5 digits are still valid
            if(isValid5DigitZipCode(zipcodeStr.substring(0,5))){
                // All that the USPS says is the last 4 digits must be all alpha numeric
                try{
                    Integer.parseInt(zipcodeStr.substring(6));
                    return true;
                }
                catch(NumberFormatException e){
                    return false;
                }
            }
        }
        return false;
    }

    // Helper method to keep code clean
    private static boolean isValid5DigitZipCode(String zipcodeStr){
        // Simple 5 digit zipcode
        if(zipcodeStr.length() == 5){
            try{
                int zipCode = Integer.parseInt(zipcodeStr);
                if(zipCode > 0 && zipCode < 99951){
                    return true;
                }
            }
            catch(NumberFormatException e){
                return false;
            }
        }
        return false;
    }
}
