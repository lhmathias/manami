package io.github.manami

import io.github.manami.gui.events.ApplicationContextStartedEvent
import io.github.manamiproject.manami.common.EventBus
import io.github.manamiproject.manami.core.Manami
import javafx.application.Application
import javafx.stage.Stage


/**
 * Entry point of the application.
 */
class Main : Application() {

  /**
   * @param args Command line arguments.
   */
  fun main(args: Array<String>) {
    Manami.init()
    launch(*args)
  }

  override fun start(primaryStage: Stage?) {
    primaryStage?.let {
      EventBus.publish(ApplicationContextStartedEvent(primaryStage))
    }
  }
}
