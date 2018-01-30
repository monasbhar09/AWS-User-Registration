/**
 * Dhanashree Chavan, 001222495, chavan.d@husky.neu.edu
 * Monas Bhar, 001232781, bhar.m@husky.neu.edu
 * Akanksha Harshe, 001228921 , harshe.a@husky.neu.edu
 * Jyotsna Khatter, 001285017, khatter.j@husky.neu.edu
 **/
package com.csye6225.demo;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DemoApplication.class);
	}

}
