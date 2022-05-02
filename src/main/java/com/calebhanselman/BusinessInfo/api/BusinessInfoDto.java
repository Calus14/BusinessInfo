package com.calebhanselman.BusinessInfo.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple class that allows users to add BusinessInfo to make the application slightly more dynamic.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessInfoDto {
    String locationId;
    String businessName;
    String neighborhood;
    String zipCode;
}
