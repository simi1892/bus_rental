package ch.simi1892.busrental.config;

import ch.simi1892.busrental.namingstrategy.CustomPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {
    /**
     * Custom hibernate physical naming strategy
     * @return the physical naming strategy
     */
    @Bean
    public CustomPhysicalNamingStrategy physicalNamingStrategy() {
        return new CustomPhysicalNamingStrategy();
    }
}
