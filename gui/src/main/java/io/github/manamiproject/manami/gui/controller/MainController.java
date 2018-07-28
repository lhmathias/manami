package io.github.manamiproject.manami.gui.controller;


//public class MainController implements Observer {
//
//  /**
//   * Initializes the table view for the anime list. Including column mapping an so on.
//   */
//  public void initialize() {
//    // Only show button for deletion if the animelist is focused
//    tabAnimeList.setOnSelectionChanged(event -> miDeleteEntry.setDisable(!tabAnimeList.isSelected()));
//
//
//    // COLUMN: InfoLink
//    colAnimeListLink.setOnEditCommit(event -> {
//      final Anime selectedAnime = tvAnimeList.getItems().get(event.getTablePosition().getRow());
//      final Anime oldValue = copyAnime(selectedAnime);
//      final InfoLink newValue = event.getNewValue();
//      executeCommand(new CmdChangeInfoLink(oldValue, newValue, app));
//      selectedAnime.setInfoLink(newValue);
//    });
//
//    // COLUMN: Location
//    colAnimeListLocation.setOnEditStart(event -> {
//      final Path folder = showBrowseForFolderDialog(mainControllerWrapper.getMainStage());
//      final String newLocation = PathResolver.buildRelativizedPath(folder.toString(), config.getFile().getParent());
//
//      if (isNotBlank(newLocation)) {
//        final Anime selectedAnime = tvAnimeList.getItems().get(event.getTablePosition().getRow());
//        final Anime oldValue = copyAnime(selectedAnime);
//        executeCommand(new CmdChangeLocation(oldValue, newLocation, app));
//        selectedAnime.setLocation(newLocation);
//      }
//    });
//
//    btnSearch.setOnAction(event -> search());
//    txtSearchString.setOnAction(event -> search());
//  }
//
//  private void search() {
//    Platform.runLater(() -> {
//      txtSearchString.setText(EMPTY);
//    });
//    app.search(txtSearchString.getText());
//    showSearchResultTab();
//  }
//
//
//  /**
//   * Deletes a specific entry.
//   */
//  public void deleteEntry() {
//    final Anime entry = tvAnimeList.getSelectionModel().getSelectedItem();
//
//    if (entry != null) {
//      cmdService.executeCommand(new CmdDeleteAnime(entry, app));
//      Platform.runLater(() -> {
//        tvAnimeList.getSelectionModel().clearSelection();
//        tvAnimeList.getItems().remove(entry);
//      });
//    }
//  }
//
//
//  /**
//   * Creates a new, empty list.
//   */
//  public void newList() {
//    safelyExecuteMethod(file -> {
//      focusActiveTab(tabAnimeList);
//      Platform.runLater(() -> {
//        tabPane.getTabs().remove(filterTab);
//        tabPane.getTabs().remove(relatedAnimeTab);
//        tabPane.getTabs().remove(recommendationsTab);
//        tabPane.getTabs().remove(checkListTab);
//      });
//      cancelAndResetBackgroundServices();
//      app.newList();
//    }, null);
//  }
//
//
//
//  /**
//   * Whenever a user clicks on a notification.
//   */
//  private void onClickOnNotification(final Tab tab) {
//    if (tab != null) {
//      mainControllerWrapper.getMainStage().toFront();
//      mainControllerWrapper.getMainStage().requestFocus();
//      focusActiveTab(tab);
//    }
//  }
//
//  /**
//   * An event which is handled whenever a notification for recommended filter list entries is being clicked.
//   */
//  class RecommendedFilterListEntryNotificationEventHandler implements EventHandler<ActionEvent> {
//
//    @Override
//    public void handle(final ActionEvent event) {
//      onClickOnNotification(filterTab);
//    }
//  }
//
//  /**
//   * An event which is handled whenever a notification for related anime is being clicked.
//   */
//  class RelatedAnimeNotificationEventHandler implements EventHandler<ActionEvent> {
//
//    @Override
//    public void handle(final ActionEvent event) {
//      onClickOnNotification(relatedAnimeTab);
//    }
//  }
//
//  /**
//   * An event which is handled whenever a notification for recommendations is being clicked.
//   */
//  class RecommendationsNotificationEventHandler implements EventHandler<ActionEvent> {
//
//    @Override
//    public void handle(final ActionEvent event) {
//      onClickOnNotification(recommendationsTab);
//    }
//  }
//
//  /**
//   * An event which is handled whenever a notification for recommendations is being clicked.
//   */
//  class CheckListNotificationEventHandler implements EventHandler<ActionEvent> {
//
//    @Override
//    public void handle(final ActionEvent event) {
//      onClickOnNotification(checkListTab);
//    }
//  }
//
//
//  public void showTagListTab() {
//    if (tagListTab == null) {
//      tagListTab = Main.CONTEXT.getBean(TagListControllerWrapper.class).getTagListTab();
//    }
//
//    focusActiveTab(tagListTab);
//  }
//}