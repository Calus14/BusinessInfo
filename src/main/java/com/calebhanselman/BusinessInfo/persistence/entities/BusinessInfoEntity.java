package com.calebhanselman.BusinessInfo.persistence.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The entity class that represents what information this application will chose to persist in azure in order to
 * execute the functionality needed.
 *
 * Currently only zipcodes, and neighborhoods are needed as we care about how many businesses are in an area, not how
 * many locations they have.
 */
@Entity
@Table(name="business_info")
@Data
public class BusinessInfoEntity {

    // The same business can have multiple locations. Because the problem is more concerned about physical locations
    // and inspecting the data it is safe to make this our primary key.
    @Id
    @Column(name="location_id", nullable = false, unique=true)
    private String locationId;

    // It is assumed that two businesses cannot have two identical names because of trademark infringement
    @Column(name="business_name", nullable = false)
    private String businessName;

    @Column(name="neighborhood", nullable = false)
    private String neighborhood;

    @Column(name="zipcode", nullable = false)
    private int zipCode;

    /**
     * Add further columns that would be relevant here
     */
}
