package io.github.manamiproject.manami.gui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class CheckListEntry {

  /**
   * Image of the anime.
   */
  private ImageView pictureComponent;

  /**
   * Link containing the title and the info link.
   */
  private Node titleComponent;

  private Label messageComponent;

  /**
   * HBox containing additional Buttons
   */
  private final HBox additionalButtons;

  private Button removeButton;


  public CheckListEntry() {
    additionalButtons = new HBox(10);
    additionalButtons.setAlignment(Pos.CENTER);
  }


  public void addAdditionalButtons(final Button additionalButton) {
    if (additionalButton != null) {
      additionalButtons.getChildren().add(additionalButton);
      HBox.setMargin(additionalButtons, new Insets(40.0, 0.0, 0.0, 10.0));
    }
  }

  public ImageView getPictureComponent() {
    return pictureComponent;
  }

  public void setPictureComponent(ImageView pictureComponent) {
    this.pictureComponent = pictureComponent;
  }

  public Node getTitleComponent() {
    return titleComponent;
  }

  public void setTitleComponent(Node titleComponent) {
    this.titleComponent = titleComponent;
  }

  public Label getMessageComponent() {
    return messageComponent;
  }

  public void setMessageComponent(Label messageComponent) {
    this.messageComponent = messageComponent;
  }

  public Button getRemoveButton() {
    return removeButton;
  }

  public void setRemoveButton(Button removeButton) {
    this.removeButton = removeButton;
  }

  public HBox getAdditionalButtons() {
    return additionalButtons;
  }
}
