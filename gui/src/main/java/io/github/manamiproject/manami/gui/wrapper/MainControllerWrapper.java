package io.github.manamiproject.manami.gui.wrapper;

//public class MainControllerWrapper {
//
//  private static final Logger log = LoggerFactory.getLogger(MainControllerWrapper.class);
//
//  /**
//   * The window's title.
//   */
//  public static final String APPNAME = "Manami";
//
//  private MainController mainController;
//
//  private final Config config;
//
//  private Stage mainStage;
//
//
//  @Inject
//  public MainControllerWrapper(final Config config) {
//    this.config = config;
//  }
//
//
//  private void init() {
//    mainStage.setMaximized(true);
//    mainStage.setTitle(APPNAME);
//
//    try {
//      final FXMLLoader fxmlLoader = new FXMLLoader(
//          new ClassPathResource("io/github/manami/gui/controller/main.fxml").getURL());
//      final Parent pane = (Pane) fxmlLoader.load();
//      mainStage.setScene(new Scene(pane));
//      mainController = fxmlLoader.getController();
//    } catch (final Exception e) {
//      log.error("An error occurred while trying to initialize main controller: ", e);
//      showExceptionDialog(e);
//    }
//
//    mainStage.show();
//  }
//
//
//  @Subscribe
//  public void changeEvent(final ApplicationContextStartedEvent event) {
//    mainStage = event.getStage();
//    init();
//  }
//
//
//  @Subscribe
//  @AllowConcurrentEvents
//  public void changeEvent(final AnimeListChangedEvent event) {
//    mainController.refreshEntriesInGui();
//    mainController.checkGui();
//  }
//
//
//  @Subscribe
//  public void changeEvent(final OpenedFileChangedEvent event) {
//    if (config.getFile() != null) {
//      Platform.runLater(() -> mainStage.setTitle(APPNAME + " - " + config.getFile().toString()));
//    } else {
//      Platform.runLater(() -> mainStage.setTitle(APPNAME));
//    }
//    mainController.checkGui();
//  }
//
//
//  public void setDirty(final boolean isDirty) {
//    if (isDirty) {
//      if (config.getFile() != null) {
//        mainStage.setTitle(String.format("%s - %s *", APPNAME, config.getFile().toString()));
//      } else {
//        mainStage.setTitle(String.format("%s *", APPNAME));
//      }
//    } else {
//      if (config.getFile() != null) {
//        mainStage.setTitle(String.format("%s - %s", APPNAME, config.getFile().toString()));
//      } else {
//        mainStage.setTitle(String.format("%s", APPNAME));
//      }
//    }
//  }
//
//  public MainController getMainController() {
//    return mainController;
//  }
//
//  public Stage getMainStage() {
//    return mainStage;
//  }
//}
