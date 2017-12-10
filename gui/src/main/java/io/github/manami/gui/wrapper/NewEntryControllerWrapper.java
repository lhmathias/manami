package io.github.manami.gui.wrapper;

import static io.github.manami.gui.utility.DialogLibrary.showExceptionDialog;
import static io.github.manami.gui.wrapper.MainControllerWrapper.APPNAME;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

@Named
public class NewEntryControllerWrapper {

  private static final Logger log = LoggerFactory.getLogger(NewEntryControllerWrapper.class);

  public void showNewEntryStage() {
    final Stage newEntryStage = new Stage(StageStyle.UTILITY);
    newEntryStage.setResizable(false);
    newEntryStage.initModality(Modality.APPLICATION_MODAL);
    newEntryStage.centerOnScreen();
    newEntryStage.initStyle(StageStyle.UTILITY);
    newEntryStage.setTitle(APPNAME + " - Add new entry");

    try {
      final FXMLLoader fxmlLoader = new FXMLLoader(new ClassPathResource("io/github/manami/gui/controller/new_entry.fxml").getURL());
      final Pane pane = fxmlLoader.load();
      newEntryStage.setScene(new Scene(pane));
      newEntryStage.sizeToScene();
    } catch (final Exception e) {
      log.error("An error occurred while trying to initialize new entry controller: ", e);
      showExceptionDialog(e);
    }

    newEntryStage.show();
  }
}
