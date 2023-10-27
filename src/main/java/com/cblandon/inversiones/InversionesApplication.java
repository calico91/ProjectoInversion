package com.cblandon.inversiones;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class InversionesApplication {

    public static void main(String[] args) {
        SpringApplication.run(InversionesApplication.class, args);
    }

    @Bean
    public OpenAPI documentacionAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Proyecto inversiones")
                        .version("1.0")
                        .description("Control de inversiones")

                );
    }

}
