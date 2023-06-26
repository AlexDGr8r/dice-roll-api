package io.alexdgr8r.diceroll;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class RollApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RollApiApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info().title("Dice Roll API")
                        .version("1.2")
                        .description("API for rolling dice! Including custom dice notation support!")
                        .contact(new Contact().name("AlexDGr8r")
                                .url("https://github.com/AlexDGr8r/dice-roll-api"))
                        .summary("Roll dice!"));
    }

}
