package io.github.manami.core.config;

import com.google.common.eventbus.EventBus;
import io.github.manami.dto.events.OpenedFileChangedEvent;
import java.nio.file.Path;
import javafx.util.Duration;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Contains the path for all configuration files as well as the path for the currently opened anime
 * list file.
 *
 * @author manami-project
 * @since 2.0.0
 */
@Named
public class Config {

  /**
   * File which is currently being worked on.
   */
  private Path file;

  public static final Duration NOTIFICATION_DURATION = Duration.seconds(6.0);

  private final EventBus eventBus;


  /**
   * Constructor.
   *
   * @since 2.0.0
   */
  @Inject
  public Config(final EventBus eventBus) {
    this.eventBus = eventBus;
  }


  /**
   * @param file the file to set
   * @since 2.0.0
   */
  public void setFile(final Path file) {
    this.file = file;
    eventBus.post(new OpenedFileChangedEvent());
  }

  public Path getFile() {
    return file;
  }
}
