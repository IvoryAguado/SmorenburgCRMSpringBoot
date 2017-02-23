package me.smorenburg;

import me.smorenburg.api.rest.media.storage.StorageProperties;
import me.smorenburg.api.rest.media.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
        @PropertySource(value = "classpath:application.properties")
})
@EnableConfigurationProperties(StorageProperties.class)
public class SmorenburgCRMSpringBootApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SmorenburgCRMSpringBootApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SmorenburgCRMSpringBootApplication.class);
    }



    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
