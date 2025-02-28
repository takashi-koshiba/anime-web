package com.example.web.etc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class CacheFonts  implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/anime-web/font/**")
                .addResourceLocations("classpath:/static/anime-web/font/")  
                .setCachePeriod(86400 * 31); 
    }
}