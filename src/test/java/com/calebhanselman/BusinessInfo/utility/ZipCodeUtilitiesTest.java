package com.calebhanselman.BusinessInfo.utility;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Test the ZipCodeUtilities to verify that we dont let sql injections come through and that we validate the strings given.
 */
class ZipCodeUtilitiesTest {

    static final Random rand = new Random();

    // Creates 100 valid random zip codes and makes sure they are valid
    @Test
    public void test5DigitZipCodesValid(){
        for(int i =0; i<100; i++){
            int validZip = rand.nextInt(99951)+1;
            String testZipStr = String.format("%05d", validZip);
            assertTrue(ZipCodeUtilities.isValidZipCode(testZipStr));
        }
    }

    // Creates 100 valid random 9 digit zip codes and makes sure they are valid
    @Test
    public void test9DigitZipCodesValid(){
        for(int i =0; i<100; i++){
            int validZip = rand.nextInt(99951)+1;
            int valid4Digits = rand.nextInt(10000);
            String divider = rand.nextInt()%2 == 0 ? "-" : " ";
            String testZipStr = String.format("%05d", validZip)+divider+String.format("%04d", valid4Digits);
            assertTrue(ZipCodeUtilities.isValidZipCode(testZipStr));
        }
    }

}