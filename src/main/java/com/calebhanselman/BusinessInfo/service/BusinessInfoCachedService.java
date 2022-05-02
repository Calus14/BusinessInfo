package com.calebhanselman.BusinessInfo.service;

import com.calebhanselman.BusinessInfo.persistence.BusinessInfoRepository;
import com.calebhanselman.BusinessInfo.persistence.entities.BusinessInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Implementation of the BusinessInfo Service which will upon starting the application read all rows from the
 * businessInfo table, loading them into in cache memory. By doing so we avoid risks of writing queries from user input
 * as well as increase the response speed.
 *
 *
 */
@Service
public class BusinessInfoCachedService implements BusinessInfoService{

    @Autowired
    private BusinessInfoRepository businessInfoRepository;

    // Increases the speed at which this application will run at the cost of memory.
    // If this became a problem in the future i would modify the JVM size and if that was still to large i would use
    // redis.
    protected HashMap<String, BusinessInfoEntity> cachedBusinessInfos = new HashMap<>();

    // The two below maps are here for utility and speed. Their size will most likely be incredibly small because the
    // original table has a list of ALL businesses and there are a lot of repeat businesses with locations in SF
    protected HashMap<String, Set<String>> neighborhoodBusinessMap = new HashMap<>();
    protected HashMap<Integer, Set<String>> zipcodeBusinessMap = new HashMap<>();

    @PostConstruct
    protected void loadCache(){
        List<BusinessInfoEntity> allCurrentRows = businessInfoRepository.findAll();
        if(allCurrentRows != null && !allCurrentRows.isEmpty()){
            allCurrentRows.forEach(businessInfo ->{
                cachedBusinessInfos.put(businessInfo.getLocationId(), businessInfo);

                // Initialize the neighboorhood and zipcode sets with hash sets
                if(!neighborhoodBusinessMap.containsKey(businessInfo.getNeighborhood())){
                    neighborhoodBusinessMap.put(businessInfo.getNeighborhood(), new HashSet<>());
                }
                if(!zipcodeBusinessMap.containsKey(businessInfo.getZipCode())){
                    zipcodeBusinessMap.put(businessInfo.getZipCode(), new HashSet<>());
                }

                // Add the actual business name to the set.
                neighborhoodBusinessMap.get(businessInfo.getNeighborhood()).add(businessInfo.getBusinessName());
                zipcodeBusinessMap.get(businessInfo.getZipCode()).add(businessInfo.getBusinessName());
            });
        }
    }

    @Override
    public List<String> getBusinessesByNeighborhood(String neighborhood) {
        List<String> businessNames = new ArrayList<>();
        if(neighborhoodBusinessMap.containsKey(neighborhood))
            businessNames.addAll(neighborhoodBusinessMap.get(neighborhood));
        return businessNames;
    }

    @Override
    public List<String> getBusinessesByZipCode(int zipCode) {
        List<String> businessNames = new ArrayList<>();
        if(zipcodeBusinessMap.containsKey(zipCode))
            businessNames.addAll(zipcodeBusinessMap.get(zipCode));
        return businessNames;
    }

    // Will maintain our caches on add's by overwriting just as SQL will
    @Override
    public void addBusinessInfoRows(List<BusinessInfoEntity> entities){
        businessInfoRepository.saveAll(entities);
        entities.forEach(businessInfo -> {
            cachedBusinessInfos.put(businessInfo.getLocationId(), businessInfo);

            // Initialize the neighboorhood and zipcode sets with hash sets
            if(!neighborhoodBusinessMap.containsKey(businessInfo.getNeighborhood())){
                neighborhoodBusinessMap.put(businessInfo.getNeighborhood(), new HashSet<>());
            }
            if(!zipcodeBusinessMap.containsKey(businessInfo.getZipCode())){
                zipcodeBusinessMap.put(businessInfo.getZipCode(), new HashSet<>());
            }

            // Add the actual business name to the set.
            neighborhoodBusinessMap.get(businessInfo.getNeighborhood()).add(businessInfo.getBusinessName());
            zipcodeBusinessMap.get(businessInfo.getZipCode()).add(businessInfo.getBusinessName());
        });
    }
}
