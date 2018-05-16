package io.github.manamiproject.manami.gui.wrapper;

//public class RecommendationsControllerWrapper {
//
//  private static final Logger log = LoggerFactory.getLogger(RecommendationsControllerWrapper.class);
//  private Tab recommendationsTab;
//  private RecommendationsController recommendationsController;
//
//
//  private void init() {
//    recommendationsTab = new Tab(RECOMMENDATIONS_TAB_TITLE);
//    Parent pane;
//    try {
//      final FXMLLoader fxmlLoader = new FXMLLoader(new ClassPathResource("io/github/manami/gui/controller/recommendations_tab.fxml").getURL());
//      pane = (Pane) fxmlLoader.load();
//      recommendationsTab.setContent(pane);
//      recommendationsController = fxmlLoader.getController();
//      recommendationsController.setTab(recommendationsTab);
//    } catch (final Exception e) {
//      log.error("An error occurred while trying to initialize recommendations tab: ", e);
//      showExceptionDialog(e);
//    }
//  }
//
//
//  public Tab getRecommendationsTab() {
//    if (recommendationsTab == null) {
//      init();
//    }
//
//    return recommendationsTab;
//  }
//
//
//  /**
//   * @param event ChecklistEvent which is fired when a file is opened.
//   */
//  @Subscribe
//  public void changeEvent(final OpenedFileChangedEvent event) {
//    if (recommendationsController == null) {
//      init();
//    }
//
//    recommendationsController.clear();
//  }
//}
