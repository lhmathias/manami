package io.github.manamiproject.manami.gui.utility;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

/**
 * Contains pre configured {@link FileChooser} dialogs.
 */
public final class DialogLibrary {

  /**
   * Shows the dialog in which the user has to choose between saving the changes, not saving the changes and canceling the action.
   *
   * @return A number which indicates whether to save or not. -1="cancel" 0="don't save" 1="save"
   */
  public static int showUnsavedChangesDialog() {
    int ret = -1;
    final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Unsaved Changes");
    alert.setHeaderText("Your changes will be lost if you don't save them.");
    alert.setContentText("Do you want to save your changes?");

    final ButtonType btnYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
    final ButtonType btnNo = new ButtonType("No", ButtonBar.ButtonData.NO);
    final ButtonType btnCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

    alert.getButtonTypes().setAll(btnYes, btnNo, btnCancel);

    final Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == btnYes) {
      ret = 1;
    } else if (result.get() == btnNo) {
      ret = 0;
    }

    return ret;
  }
}
