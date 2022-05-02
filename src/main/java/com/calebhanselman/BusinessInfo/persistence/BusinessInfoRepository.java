package com.calebhanselman.BusinessInfo.persistence;

import com.calebhanselman.BusinessInfo.persistence.entities.BusinessInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Currently just uses spring magic to connect to azure's sql database. For my first iteration, I will just use this
 * connection to pull all rows to fill my in memory cache as well as provide an endpoint to upload too to initially
 * populate the table.
 */
@Repository
public interface BusinessInfoRepository extends JpaRepository<BusinessInfoEntity, String> {
}
