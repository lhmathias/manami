package io.github.manami.core.services;

import static com.google.common.collect.Lists.newArrayList;

import com.sun.javafx.collections.ObservableListWrapper;
import java.util.Observable;
import java.util.Observer;
import javafx.collections.ObservableList;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class ServiceRepository implements Observer {

  private static final Logger log = LoggerFactory.getLogger(ServiceRepository.class);
  private final ObservableList<BackgroundService> runningServices = new ObservableListWrapper<>(newArrayList());


  public void startService(final BackgroundService service) {
    if (service == null) {
      return;
    }

    synchronized (runningServices) {
      if (!runningServices.isEmpty()) {
        for (int index = 0; index < runningServices.size(); index++) {
          final BackgroundService curService = runningServices.get(index);

          final boolean isAlreadyRunning = !(service instanceof RelatedAnimeFinderService) && service.getClass().equals(curService.getClass());

          if (isAlreadyRunning) {
            curService.cancel();
          }

          final boolean isManuallyStartedService = runningServices.size() >= 3;

          if (curService instanceof CacheInitializationService && isManuallyStartedService) {
            log.info("Stopping cache init service.");
            curService.cancel();
          }
        }
      }

      safelyStartService(service);
    }
  }


  public void cancelAllServices() {
    synchronized (runningServices) {
      while (!runningServices.isEmpty()) {
        runningServices.get(0).cancel();
      }
    }
  }


  private void safelyStartService(final BackgroundService service) {
    service.addObserver(this);
    runningServices.add(service);
    service.start();
  }


  @Override
  public void update(final Observable observable, final Object obj) {
    // we only want to know if a service succeeded or failed
    if (obj != null && observable != null && obj instanceof Boolean) {
      synchronized (runningServices) {
        runningServices.remove(observable);
      }
    }
  }
}
