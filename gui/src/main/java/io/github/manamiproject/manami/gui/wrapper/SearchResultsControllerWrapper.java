package io.github.manamiproject.manami.gui.wrapper;

//public class SearchResultsControllerWrapper {
//
//  private static final Logger log = LoggerFactory.getLogger(SearchResultsControllerWrapper.class);
//  private Tab searchResultTab;
//  private SearchResultsController searchResultController;
//  private boolean isFirstInvocation = true;
//
//
//  private void init() {
//    searchResultTab = new Tab(SEARCH_RESULTS_TAB_TITLE);
//    Parent pane;
//    try {
//      final FXMLLoader fxmlLoader = new FXMLLoader(new ClassPathResource("io/github/manami/gui/controller/search_results_tab.fxml").getURL());
//      pane = (Pane) fxmlLoader.load();
//      searchResultTab.setContent(pane);
//      searchResultController = fxmlLoader.getController();
//    } catch (final Exception e) {
//      log.error("An error occurred while trying to initialize search result tab: ", e);
//      showExceptionDialog(e);
//    }
//  }
//
//
//  /**
//   * @return the search result tab
//   */
//  public Tab getSearchResultsTab() {
//    if (searchResultTab == null) {
//      init();
//    }
//
//    return searchResultTab;
//  }
//
//
//  /**
//   * @param event ChecklistEvent which is fired when a file is opened.
//   */
//  @Subscribe
//  public void searchResultEvent(final SearchResultEvent event) throws InterruptedException {
//    Platform.runLater(() -> {
//      if (searchResultController == null) {
//        init();
//      }
//
//      searchResultController.showResults(event);
//    });
//
//    if (isFirstInvocation) {
//      reexecuteAfterWaitingTime(event);
//    }
//  }
//
//
//  /**
//   * This dirty hack is needed, because otherwise the title panes won't expand on first invocation.
//   */
//  private void reexecuteAfterWaitingTime(final SearchResultEvent event) throws InterruptedException {
//    isFirstInvocation = false;
//    Thread.sleep(500L);
//    searchResultEvent(event);
//  }
//}