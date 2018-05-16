package io.github.manamiproject.manami.gui.wrapper;

//public class RelatedAnimeControllerWrapper {
//
//  private static final Logger log = LoggerFactory.getLogger(RelatedAnimeControllerWrapper.class);
//  private Tab relatedAnimeTab;
//  private RelatedAnimeController relatedAnimeController;
//
//
//  private void init() {
//    relatedAnimeTab = new Tab(RELATED_ANIME_TAB_TITLE);
//    Parent pane;
//    try {
//      final FXMLLoader fxmlLoader = new FXMLLoader(new ClassPathResource("io/github/manami/gui/controller/related_anime_tab.fxml").getURL());
//      pane = (Pane) fxmlLoader.load();
//      relatedAnimeTab.setContent(pane);
//      relatedAnimeController = fxmlLoader.getController();
//      relatedAnimeController.setTab(relatedAnimeTab);
//    } catch (final Exception e) {
//      log.error("An error occurred while trying to initialize related anime tab: ", e);
//      showExceptionDialog(e);
//    }
//  }
//
//
//  /**
//   * @return the relatedAnimeTab
//   */
//  public Tab getRelatedAnimeTab() {
//    if (relatedAnimeTab == null) {
//      init();
//    }
//
//    return relatedAnimeTab;
//  }
//
//
//  /**
//   * @param event ChecklistEvent which is fired when a file is opened.
//   */
//  @Subscribe
//  public void changeEvent(final OpenedFileChangedEvent event) {
//    if (relatedAnimeController == null) {
//      init();
//    }
//
//    relatedAnimeController.clear();
//  }
//}
