package com.calebhanselman.BusinessInfo.api;

import com.calebhanselman.BusinessInfo.utility.BusinessInfoExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Interface that allows users to interact with the data set that is stored azure to meet the basic requirements
 * of the problem which are as follows:
 *
 * 1) Each business has a neighborhood. One of the APIs should be to return a count of all businesses in each neighborhood.
 *
 * 2) Each business has a zip code. Zip code is in multiple formats in the source data like 93454, 94545-3423, 04545 3444. When ingesting the data, normalize the zip code to 5 digits zip code. And build an API that will return counts of businesses in each zip code
 *
 */
public interface BusinessInfoApi {

    /**
     * Method that will look into our data store for a business with a specific neighboorhood and return the full count.
     * If implementing a connection to an actual database make sure to check the input string for SQL injection attacks
     * and other such issues.
     *
     * @param neighborHood Neighboorhood you wish to find the total number of busneisses in
     * @return the total number of busneisses found.
     */
    @RequestMapping(value = "neighborhoodCount", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.ACCEPTED)
    Integer getNeighborhoodCount(@RequestParam String neighborHood);

    /**
     * Method that will look into our data store for a business with a specific zipcode and return the full count.
     * If implementing a connection to an actual database make sure to check the input string for SQL injection attacks
     * and other such issues
     *
     * @param zipCode Standard US Zipcode that can be parsed down to a 5 digit zip code
     * @return
     */
    @RequestMapping(value = "zipCodeCount", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.ACCEPTED)
    Integer getZipCodeCount(@RequestParam String zipCode) throws BusinessInfoExceptions.ZipCodeException;

    @RequestMapping(value="addBusinessInfo", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    void addBusinessInfos(@RequestBody List<BusinessInfoDto> businessInfoDtosList);

}
