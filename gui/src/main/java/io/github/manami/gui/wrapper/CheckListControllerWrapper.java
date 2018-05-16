package io.github.manami.gui.wrapper;

import static io.github.manami.gui.utility.DialogLibrary.showExceptionDialog;

import com.google.common.eventbus.Subscribe;
import io.github.manamiproject.manami.core.events.OpenedFileChangedEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//public class CheckListControllerWrapper {
//
//  private static final Logger log = LoggerFactory.getLogger(CheckListControllerWrapper.class);
//  private Tab checkListTab;
//  private CheckListController checkListController;
//
//
//  private void init() {
//    checkListTab = new Tab(CHECK_LIST_TAB_TITLE);
//    Parent pane;
//    try {
//      final FXMLLoader fxmlLoader = new FXMLLoader(new ClassPathResource("io/github/manami/gui/controller/check_list_tab.fxml").getURL());
//      pane = (Pane) fxmlLoader.load();
//      checkListTab.setContent(pane);
//      checkListController = fxmlLoader.getController();
//      checkListController.setTab(checkListTab);
//    } catch (final Exception e) {
//      log.error("An error occurred while trying to initialize check list tab: ", e);
//      showExceptionDialog(e);
//    }
//  }
//
//
//  /**
//   * @return the checkListTab
//   */
//  public Tab getCheckListTab() {
//    if (checkListTab == null) {
//      init();
//    }
//
//    return checkListTab;
//  }
//
//
//  /**
//   * @param event ChecklistEvent which is fired when a file is opened.
//   */
//  @Subscribe
//  public void changeEvent(final OpenedFileChangedEvent event) {
//    if (checkListController == null) {
//      init();
//    }
//
//    checkListController.clear();
//  }
//}
