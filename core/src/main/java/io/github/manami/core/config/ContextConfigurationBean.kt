package io.github.manami.core.config;

import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration bean for the spring context.
 */
@Configuration
@ComponentScan("io.github.manami")
class ContextConfigurationBean {

    private var eventBus: EventBus? = null


    @Bean
    fun eventBus(): EventBus? {
        if (eventBus == null) {
            eventBus = EventBus()
        }

        return eventBus
    }
}
