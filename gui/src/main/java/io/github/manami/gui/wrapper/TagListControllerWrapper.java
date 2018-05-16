package io.github.manami.gui.wrapper;

import static io.github.manami.gui.utility.DialogLibrary.showExceptionDialog;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//public class TagListControllerWrapper {
//
//  private static final Logger log = LoggerFactory.getLogger(TagListControllerWrapper.class);
//  private Tab tagListTab;
//  private TagListController tagListController;
//
//
//  private void init() {
//    tagListTab = new Tab(TAG_LIST_TITLE);
//    tagListTab.setOnSelectionChanged(event -> {
//      if (tagListTab.isSelected()) {
//        tagListController.showEntries();
//      }
//    });
//
//    Parent pane;
//    try {
//      final FXMLLoader fxmlLoader = new FXMLLoader(new ClassPathResource("io/github/manami/gui/controller/tag_list_tab.fxml").getURL());
//      pane = (Pane) fxmlLoader.load();
//      tagListController = fxmlLoader.getController();
//      tagListController.setTab(tagListTab);
//      tagListTab.setContent(pane);
//    } catch (final Exception e) {
//      log.error("An error occurred while trying to initialize watch list tab: ", e);
//      showExceptionDialog(e);
//    }
//  }
//
//
//  @Subscribe
//  public void changeEvent(final OpenedFileChangedEvent event) {
//    if (tagListController == null) {
//      init();
//    }
//
//    tagListController.clear();
//  }
//
//
//  @Subscribe
//  @AllowConcurrentEvents
//  public void changeEvent(final AnimeListChangedEvent event) {
//    if (tagListController == null) {
//      init();
//    }
//
//    if (tagListTab.isSelected()) {
//      tagListController.showEntries();
//    }
//  }
//
//
//  /**
//   * @return the filterTab
//   */
//  public Tab getTagListTab() {
//    if (tagListTab == null) {
//      init();
//    }
//
//    return tagListTab;
//  }
//}
