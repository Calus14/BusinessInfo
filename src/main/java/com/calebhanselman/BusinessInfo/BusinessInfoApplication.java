package com.calebhanselman.BusinessInfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Application meant to expose access to data which is considered static from the following source
 * https://data.sfgov.org/Economy-and-Community/Registered-Business-Locations-San-Francisco/g8m3-pdis
 *
 * Data is being hosted freely in azure and will be initialized via a call to the service which will
 * add all rows present in the resources/info.csv file which is present on the container.
 */
@SpringBootApplication
@EnableJpaRepositories()
public class BusinessInfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusinessInfoApplication.class, args);
	}

}
