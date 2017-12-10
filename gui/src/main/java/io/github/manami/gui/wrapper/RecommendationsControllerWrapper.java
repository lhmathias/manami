package io.github.manami.gui.wrapper;

import static io.github.manami.gui.controller.RecommendationsController.RECOMMENDATIONS_TAB_TITLE;
import static io.github.manami.gui.utility.DialogLibrary.showExceptionDialog;

import com.google.common.eventbus.Subscribe;
import io.github.manami.dto.events.OpenedFileChangedEvent;
import io.github.manami.gui.controller.RecommendationsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

@Named
public class RecommendationsControllerWrapper {

  private static final Logger log = LoggerFactory.getLogger(RecommendationsControllerWrapper.class);
  private Tab recommendationsTab;
  private RecommendationsController recommendationsController;


  private void init() {
    recommendationsTab = new Tab(RECOMMENDATIONS_TAB_TITLE);
    Parent pane;
    try {
      final FXMLLoader fxmlLoader = new FXMLLoader(new ClassPathResource("io/github/manami/gui/controller/recommendations_tab.fxml").getURL());
      pane = (Pane) fxmlLoader.load();
      recommendationsTab.setContent(pane);
      recommendationsController = fxmlLoader.getController();
      recommendationsController.setTab(recommendationsTab);
    } catch (final Exception e) {
      log.error("An error occurred while trying to initialize recommendations tab: ", e);
      showExceptionDialog(e);
    }
  }


  public Tab getRecommendationsTab() {
    if (recommendationsTab == null) {
      init();
    }

    return recommendationsTab;
  }


  /**
   * @param event Event which is fired when a file is opened.
   */
  @Subscribe
  public void changeEvent(final OpenedFileChangedEvent event) {
    if (recommendationsController == null) {
      init();
    }

    recommendationsController.clear();
  }
}
