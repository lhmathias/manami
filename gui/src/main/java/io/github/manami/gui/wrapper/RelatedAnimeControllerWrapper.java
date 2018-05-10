package io.github.manami.gui.wrapper;

import static io.github.manami.gui.controller.RelatedAnimeController.RELATED_ANIME_TAB_TITLE;
import static io.github.manami.gui.utility.DialogLibrary.showExceptionDialog;

import com.google.common.eventbus.Subscribe;
import io.github.manami.dto.events.OpenedFileChangedEvent;
import io.github.manami.gui.controller.RelatedAnimeController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

@Named
public class RelatedAnimeControllerWrapper {

  private static final Logger log = LoggerFactory.getLogger(RelatedAnimeControllerWrapper.class);
  private Tab relatedAnimeTab;
  private RelatedAnimeController relatedAnimeController;


  private void init() {
    relatedAnimeTab = new Tab(RELATED_ANIME_TAB_TITLE);
    Parent pane;
    try {
      final FXMLLoader fxmlLoader = new FXMLLoader(new ClassPathResource("io/github/manami/gui/controller/related_anime_tab.fxml").getURL());
      pane = (Pane) fxmlLoader.load();
      relatedAnimeTab.setContent(pane);
      relatedAnimeController = fxmlLoader.getController();
      relatedAnimeController.setTab(relatedAnimeTab);
    } catch (final Exception e) {
      log.error("An error occurred while trying to initialize related anime tab: ", e);
      showExceptionDialog(e);
    }
  }


  /**
   * @return the relatedAnimeTab
   */
  public Tab getRelatedAnimeTab() {
    if (relatedAnimeTab == null) {
      init();
    }

    return relatedAnimeTab;
  }


  /**
   * @param event ChecklistEvent which is fired when a file is opened.
   */
  @Subscribe
  public void changeEvent(final OpenedFileChangedEvent event) {
    if (relatedAnimeController == null) {
      init();
    }

    relatedAnimeController.clear();
  }
}
