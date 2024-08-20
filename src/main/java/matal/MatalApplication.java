package matal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableBatchProcessing
@SpringBootApplication
@EnableJpaRepositories(basePackages = "matal.repository")
public class MatalApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatalApplication.class, args);
    }
}
