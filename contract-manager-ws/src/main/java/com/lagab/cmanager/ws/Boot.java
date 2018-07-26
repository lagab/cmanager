package com.lagab.cmanager.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author gabriel
 * @since 12/07/2018.
 */
@SpringBootApplication(scanBasePackages = {
        "com.lagab.cmanager.ws"
})
@EntityScan(basePackages = {
        "com.lagab.cmanager.persistance.model"
})
@EnableJpaRepositories(basePackages = {
        "com.lagab.cmanager.persistance.repository"
})
public class Boot extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Boot.class);
    }

    /** Entry Point of the Application **/
    public static void main(String[] args) {
        SpringApplication.run(Boot.class, args);
    }

}
