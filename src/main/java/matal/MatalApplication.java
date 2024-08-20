package matal;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableBatchProcessing
@SpringBootApplication
@EnableJpaRepositories(basePackages = "matal.repository")
public class MatalApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatalApplication.class, args);
    }
}
