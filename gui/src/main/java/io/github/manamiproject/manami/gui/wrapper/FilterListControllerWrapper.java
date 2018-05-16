package io.github.manamiproject.manami.gui.wrapper;

//public class FilterListControllerWrapper {
//
//  private static final Logger log = LoggerFactory.getLogger(FilterListControllerWrapper.class);
//  private Tab filterTab;
//  private FilterListController filterListController;
//
//
//  private void init() {
//    filterTab = new Tab(FilterListController.FILTER_LIST_TITLE);
//    filterTab.setOnSelectionChanged(event -> {
//      if (filterTab.isSelected()) {
//        filterListController.showEntries();
//      }
//    });
//
//    Parent pane;
//    try {
//      final FXMLLoader fxmlLoader = new FXMLLoader(new ClassPathResource("io/github/manami/gui/controller/filter_list_tab.fxml").getURL());
//      pane = (Pane) fxmlLoader.load();
//      filterListController = fxmlLoader.getController();
//      filterTab.setContent(pane);
//      filterListController.setTab(filterTab);
//    } catch (final Exception e) {
//      log.error("An error occurred while trying to initialize filter list tab: ", e);
//      showExceptionDialog(e);
//    }
//
//  }
//
//
//  @Subscribe
//  public void changeEvent(final OpenedFileChangedEvent event) {
//    filterListController.clear();
//  }
//
//
//  public void startRecommendedFilterEntrySearch() {
//    filterListController.startRecommendedFilterEntrySearch();
//  }
//
//
//  public Tab getFilterTab() {
//    if (filterTab == null) {
//      init();
//    }
//
//    return filterTab;
//  }
//}
