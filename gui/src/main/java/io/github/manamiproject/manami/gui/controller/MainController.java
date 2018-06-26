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
//  /**
//   * Opens a new file.
//   */
//  public void open() {
//    final Path selectedFile = showOpenFileDialog(mainControllerWrapper.getMainStage());
//
//    if (selectedFile != null && Files.exists(selectedFile)) {
//      safelyExecuteMethod(file -> {
//        cancelAndResetBackgroundServices();
//        try {
//          app.newList();
//          app.open(file);
//          controllerWrapper.startRecommendedFilterEntrySearch();
//          updateAutoCompletion();
//          refreshEntriesInGui();
//        } catch (final Exception e) {
//          log.error("An error occurred while trying to open {}: ", file, e);
//          showExceptionDialog(e);
//        }
//      }, selectedFile);
//    }
//  }
//
//  /**
//   * Show a save as dialog and then saves the data to the file.
//   */
//  public void saveAs() {
//    Path file = showSaveAsFileDialog(mainControllerWrapper.getMainStage());
//    final String extension = ".xml";
//
//    if (file != null) {
//      final String filename = file.toString();
//
//      if (!filename.endsWith(extension)) {
//        file = Paths.get(filename + extension);
//      }
//
//      config.setFile(file);
//      save();
//    }
//  }
//
//
//  /**
//   * Sets the focus of the {@link TabPane} to the given {@link Tab}.
//   *
//   * @param activeTab {@link Tab} which will gain focus.
//   */
//  private void focusActiveTab(final Tab activeTab) {
//    Platform.runLater(() -> {
//      if (!tabPane.getTabs().contains(activeTab)) {
//        tabPane.getTabs().add(activeTab);
//      }
//
//      tabPane.getSelectionModel().select(activeTab);
//    });
//  }
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
//  public void showWatchListTab() {
//    if (watchListTab == null) {
//      watchListTab = Main.CONTEXT.getBean(WatchListControllerWrapper.class).getWatchListTab();
//    }
//
//    focusActiveTab(watchListTab);
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
//
//
//  /**
//   * Resizes a {@link TableView} so that the column size automatically fits it's content.
//   */
//  private void autoSizeTableViewColumns() {
//    final List<TableColumn<Anime, ?>> colList = tvAnimeList.getColumns();
//
//    for (final TableColumn<Anime, ?> tableColumn : colList) {
//      final String longestText = determineLongestText(tableColumn);
//      final Text text = new Text(longestText);
//      final double textWidth = text.getLayoutBounds().getWidth();
//      double newWidth = (textWidth < tableColumn.getMinWidth()) ? tableColumn.getMinWidth() : textWidth;
//      // add a little spacer otherwise it's too narrow
//      newWidth += 20.0;
//      tableColumn.setPrefWidth(newWidth);
//    }
//  }
//
//
//  /**
//   * Determines the longest string within a column and returns it.
//   *
//   * @return The longest string by chars of this column.
//   */
//  private String determineLongestText(final TableColumn<Anime, ?> tableColumn) {
//    String ret = tableColumn.getText(); // init with header
//    final Callback cellFactory = tableColumn.getCellFactory();
//    final TableCell cell = (TableCell) cellFactory.call(tableColumn);
//    cell.updateTableView(tableColumn.getTableView());
//    cell.updateTableColumn(tableColumn);
//
//    for (int index = 0; index < tableColumn.getTableView().getItems().size(); index++) {
//      cell.updateIndex(index);
//
//      final String cellContent = cell.getText();
//      if (cellContent.length() > ret.length()) {
//        ret = cellContent;
//      }
//    }
//
//    return ret;
//  }
//}