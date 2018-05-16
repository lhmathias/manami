package io.github.manamiproject.manami.gui.wrapper;

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
