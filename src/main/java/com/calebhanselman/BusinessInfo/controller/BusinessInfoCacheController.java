package com.calebhanselman.BusinessInfo.controller;

import com.calebhanselman.BusinessInfo.api.BusinessInfoApi;
import com.calebhanselman.BusinessInfo.api.BusinessInfoDto;
import com.calebhanselman.BusinessInfo.persistence.entities.BusinessInfoEntity;
import com.calebhanselman.BusinessInfo.service.BusinessInfoCachedService;
import com.calebhanselman.BusinessInfo.service.BusinessInfoService;
import com.calebhanselman.BusinessInfo.utility.BusinessInfoExceptions;
import com.calebhanselman.BusinessInfo.utility.ZipCodeUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * Implementation of the BusinessInfoApi that expects to use a Service bean which will use some form of caching,
 * be it redis, hazelcast, in memory caching etc... This removes security checks and is will be generally faster.
 */
@RestController
@RequestMapping("/businessInfo")
@Slf4j
public class BusinessInfoCacheController implements BusinessInfoApi {

    @Autowired
    BusinessInfoCachedService businessInfoService;

    // If i wanted to make this check for something such as a malicous neighborhood string, i would create some utility
    // or service to validate that the string is safe to pass through directly to our database. However because this
    // cached implementation i do not need to.
    @Override
    public Integer getNeighborhoodCount(String neighborHood) {
        return businessInfoService.getBusinessesByNeighborhood(neighborHood).size();
    }

    @Override
    public Integer getZipCodeCount(String zipCode) throws BusinessInfoExceptions.ZipCodeException {
        // If this is a bad zipcode it will throw an exception and be picked up by the exception handler
        int zipCodeNumber =ZipCodeUtilities.getZipCodeAs5Digits(zipCode);
        return businessInfoService.getBusinessesByZipCode(zipCodeNumber).size();
    }

    @Override
    public void addBusinessInfos(List<BusinessInfoDto> businessInfoDtosList) {
        List<BusinessInfoEntity> entityList = new ArrayList<>();
        businessInfoDtosList.forEach(dto ->{
            try {
                BusinessInfoEntity entityToAdd = new BusinessInfoEntity();
                entityToAdd.setLocationId(dto.getLocationId());
                entityToAdd.setBusinessName(dto.getBusinessName());
                entityToAdd.setNeighborhood(dto.getNeighborhood());
                entityToAdd.setZipCode(ZipCodeUtilities.getZipCodeAs5Digits(dto.getZipCode()));
                entityList.add(entityToAdd);
            } catch (BusinessInfoExceptions.ZipCodeException e) {
                log.error("Failed to add business info dto "+dto.toString()+" because it has a bad zipcode.");
            }
        });
        businessInfoService.addBusinessInfoRows(entityList);
    }
}
