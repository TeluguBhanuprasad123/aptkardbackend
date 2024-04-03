
package com.aptCard.example;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CrosConfig implements WebMvcConfigurer {
	
	@Override 
    public void addCorsMappings(CorsRegistry registry) { 
        registry.addMapping("/**") 
                .allowedOriginPatterns("*") 
                .allowedMethods("GET", "POST", "PUT", "DELETE") 
                .allowedHeaders("Accept") // Allow requests with "Accept" header 
                .allowCredentials(true); 
    }

}
