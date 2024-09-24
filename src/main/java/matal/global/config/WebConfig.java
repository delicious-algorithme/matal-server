package matal.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${spring.cors.allowed-origins}")
    private String ALLOWED_ORIGINS;

    @Value("${spring.cors.allowed-methods}")
    private String ALLOWED_METHODS;

    @Value("${spring.cors.allowed-headers-1}")
    private String ALLOWED_HEADERS_KEY_1;

    @Value("${spring.cors.allowed-headers-2}")
    private String ALLOWED_HEADERS_KEY_2;

    @Value("${spring.cors.exposed-headers}")
    private String EXPOSED_HEADERS;

    @Value("${spring.cors.allow-credentials}")
    private boolean ALLOW_CREDENTIALS;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(ALLOWED_ORIGINS.split(","))
                .allowedMethods(ALLOWED_METHODS.split(","))
                .allowedHeaders(ALLOWED_HEADERS_KEY_1, ALLOWED_HEADERS_KEY_2)
                .exposedHeaders(EXPOSED_HEADERS)
                .allowCredentials(ALLOW_CREDENTIALS)
                .maxAge(3600);
    }
}
