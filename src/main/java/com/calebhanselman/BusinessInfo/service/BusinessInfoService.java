package com.calebhanselman.BusinessInfo.service;

import com.calebhanselman.BusinessInfo.persistence.entities.BusinessInfoEntity;

import java.util.List;

/**
 * Defines what all businessInfoService beans should be able to do.
 *
 * Importantly is designed with the distinction such that implementing classes must inform if they touch live databases
 * to their controllers, and also have some methods to access data via their chosen persistence layer.
 */
public interface BusinessInfoService {

    /**
     * Method with will look at its persistence layer to find all businesses that match exactly the same neighborhood
     * as the one given.
     *
     * @param neighborhood The neighboorhood in string representation. Implementors of this interface need to be careful to validate the string if they use it to directly touch an insecure persistent layer
     * @return
     */
    List<String> getBusinessesByNeighborhood(String neighborhood);

    /**
     * Method will look at its persistence layer to find all businesses that match exactly the zip code after being
     * transformed into a 5 digit zip code.
     * @param zipCode
     * @return
     */
    List<String> getBusinessesByZipCode(int zipCode);

    /**
     * Utility method that is not required for this problem just to make sure i can load my data set and help test
     * my azure resources and groups
     * @param entities List of entities that are going to be added to the database
     */
    void addBusinessInfoRows(List<BusinessInfoEntity> entities);

}
