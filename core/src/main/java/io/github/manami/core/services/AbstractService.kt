package io.github.manami.core.services

import java.util.Observable
import javafx.concurrent.Service
import javafx.concurrent.Task
import javafx.concurrent.WorkerStateEvent
import javafx.event.EventHandler

abstract class AbstractService<E> : Observable(), BackgroundService {

  protected var service: Service<E>
  private var interrupt = false


  override fun cancel() {
    if (service != null && service.isRunning) {
      service.cancel()
    }
  }


  override fun isRunning(): Boolean {
    if (service == null) {
      return false
    }

    return service.isRunning
  }


  /**
   * @return the failureEvent
   */
  protected EventHandler<WorkerStateEvent> getSuccessEvent() {
    return event -> {
      interrupt = true
      setChanged()
      notifyObservers(false)
    }
  }


  /**
   * @return the failureEvent
   */
  protected EventHandler<WorkerStateEvent> getFailureEvent() {
    return event -> {
      interrupt = true
      setChanged()
      notifyObservers(false)
    }
  }


  override fun reset() {
    interrupt = false
  }


  fun isInterrupt(): Boolean {
    return interrupt
  }


  override fun start() {
    reset()

    service = Service<E>() {

      @Override
      protected Task<E> createTask() {
        return new Task<E>() {

          @Override
          protected E call() throws Exception {
            return execute()
          }
        }
      }
    }

    service.setOnCancelled(getFailureEvent())
    service.setOnFailed(getFailureEvent())
    service.setOnSucceeded(getSuccessEvent())
    service.start()
x  }


  abstract fun execute(): E
}
