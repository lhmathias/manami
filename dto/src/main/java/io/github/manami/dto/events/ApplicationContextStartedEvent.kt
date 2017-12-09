package io.github.manami.dto.events;

import javafx.stage.Stage;

/**
 * @author manami-project
 * @since 2.7.2
 */
public class ApplicationContextStartedEvent {

  private final Stage stage;


  public ApplicationContextStartedEvent(final Stage stage) {
    if (stage == null) {
      throw new IllegalArgumentException("Stage cannot be null in ApplicationContextStartedEvent");
    }

    this.stage = stage;
  }

  public Stage getStage() {
    return stage;
  }
}
