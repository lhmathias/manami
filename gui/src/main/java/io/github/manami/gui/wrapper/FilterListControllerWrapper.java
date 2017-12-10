package io.github.manami.gui.wrapper;

import static io.github.manami.gui.utility.DialogLibrary.showExceptionDialog;

import com.google.common.eventbus.Subscribe;
import io.github.manami.dto.events.OpenedFileChangedEvent;
import io.github.manami.gui.controller.FilterListController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

@Named
public class FilterListControllerWrapper {

  private static final Logger log = LoggerFactory.getLogger(FilterListControllerWrapper.class);
  private Tab filterTab;
  private FilterListController filterListController;


  private void init() {
    filterTab = new Tab(FilterListController.FILTER_LIST_TITLE);
    filterTab.setOnSelectionChanged(event -> {
      if (filterTab.isSelected()) {
        filterListController.showEntries();
      }
    });

    Parent pane;
    try {
      final FXMLLoader fxmlLoader = new FXMLLoader(new ClassPathResource("io/github/manami/gui/controller/filter_list_tab.fxml").getURL());
      pane = (Pane) fxmlLoader.load();
      filterListController = fxmlLoader.getController();
      filterTab.setContent(pane);
      filterListController.setTab(filterTab);
    } catch (final Exception e) {
      log.error("An error occurred while trying to initialize filter list tab: ", e);
      showExceptionDialog(e);
    }

  }


  @Subscribe
  public void changeEvent(final OpenedFileChangedEvent event) {
    filterListController.clear();
  }


  public void startRecommendedFilterEntrySearch() {
    filterListController.startRecommendedFilterEntrySearch();
  }


  public Tab getFilterTab() {
    if (filterTab == null) {
      init();
    }

    return filterTab;
  }
}
