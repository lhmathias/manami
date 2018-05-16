package io.github.manamiproject.manami.gui.wrapper;

//public class NewEntryControllerWrapper {
//
//  private static final Logger log = LoggerFactory.getLogger(NewEntryControllerWrapper.class);
//
//  public void showNewEntryStage() {
//    final Stage newEntryStage = new Stage(StageStyle.UTILITY);
//    newEntryStage.setResizable(false);
//    newEntryStage.initModality(Modality.APPLICATION_MODAL);
//    newEntryStage.centerOnScreen();
//    newEntryStage.initStyle(StageStyle.UTILITY);
//    newEntryStage.setTitle(APPNAME + " - Add new entry");
//
//    try {
//      final FXMLLoader fxmlLoader = new FXMLLoader(new ClassPathResource("io/github/manami/gui/controller/new_entry.fxml").getURL());
//      final Pane pane = fxmlLoader.load();
//      newEntryStage.setScene(new Scene(pane));
//      newEntryStage.sizeToScene();
//    } catch (final Exception e) {
//      log.error("An error occurred while trying to initialize new entry controller: ", e);
//      showExceptionDialog(e);
//    }
//
//    newEntryStage.show();
//  }
//}
