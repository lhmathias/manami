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
public class ContextConfigurationBean {

  private EventBus eventBus;


  @Bean
  public EventBus eventBus() {
    if (eventBus == null) {
      eventBus = new EventBus();
    }

    return eventBus;
  }
}
