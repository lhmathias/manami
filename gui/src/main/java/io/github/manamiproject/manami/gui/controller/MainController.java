package io.github.manamiproject.manami.gui.controller;


//public class MainController implements Observer {
//
//
//  private AutoCompletionBinding<String> autoCompletionBinding;
//
//  /**
//   * Initializes the table view for the anime list. Including column mapping an so on.
//   */
//  public void initialize() {
//    initFilterTab();
//
//    tabAnimeList.setOnSelectionChanged(event -> {
//      if (tabAnimeList.isSelected()) {
//        refreshEntriesInGui();
//        checkGui();
//      }
//    });
//
//
//    // Quicker access the list.
//    tvAnimeList.getItems().addListener((ListChangeListener<Anime>) event -> {
//
//      while (event.next()) {
//        if (!event.wasPermutated()) {
//          checkGui();
//        }
//      }
//    });
//
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
//
//
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
//   * Enables or disables those menu items which depend on the existence of entries.
//   */
//  private void checkEntryRelevantMenuItems() {
//    final boolean isAnimeListEmpty = app.fetchAnimeList().size() == 0;
//    Platform.runLater(() -> {
//      miCheckList.setDisable(isAnimeListEmpty);
//      miRelatedAnime.setDisable(isAnimeListEmpty);
//      miRecommendations.setDisable(isAnimeListEmpty);
//      miDeleteEntry.setDisable(isAnimeListEmpty);
//      cmiDeleteEntry.setDisable(isAnimeListEmpty);
//    });
//
//    final boolean allListsEmpty = app.fetchAnimeList().size() == 0 && app.fetchWatchList().size() == 0 && app.fetchFilterList().size() == 0;
//    Platform.runLater(() -> {
//      miSave.setDisable(allListsEmpty);
//      miSaveAs.setDisable(allListsEmpty);
//      miExport.setDisable(allListsEmpty);
//    });
//  }
//
//
//  /**
//   * Checks whether to set the dirty flag or not.
//   */
//  private void checkDirtyFlagAnimeListTab() {
//    Platform.runLater(() -> mainControllerWrapper.setDirty(cmdService.isUnsaved()));
//  }
//
//
//  /**
//   * Consists of different aspects to check. It's possible that the GUI needs to react to changing circumstances. This method sums up all the checks.
//   */
//  public void checkGui() {
//    checkEntryRelevantMenuItems();
//    checkCommandMenuItems();
//    checkDirtyFlagAnimeListTab();
//    FXCollections.sort(tvAnimeList.getItems(), new MinimalEntryCompByTitleAsc());
//    refreshTableView();
//    updateAutoCompletion();
//  }
//
//
//  /**
//   * This method check if the command stacks are empty and either dis- or enables the corresponding menu items.
//   */
//  private void checkCommandMenuItems() {
//    Platform.runLater(() -> {
//      miUndo.setDisable(cmdService.isEmptyDoneCommands());
//      miRedo.setDisable(cmdService.isEmptyUndoneCommands());
//    });
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
//  /**
//   * Exports the current list.
//   */
//  public void export() {
//    final Path file = showExportDialog(mainControllerWrapper.getMainStage());
//
//    if (file != null) {
//      app.export(file);
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
//
//  private void updateAutoCompletion() {
//    if (autoCompletionBinding != null) {
//      autoCompletionBinding.dispose();
//    }
//    final List<String> suggestions = newArrayList();
//    app.fetchAnimeList().forEach(e -> suggestions.add(e.getTitle()));
//    app.fetchFilterList().forEach(e -> suggestions.add(e.getTitle()));
//    app.fetchWatchList().forEach(e -> suggestions.add(e.getTitle()));
//    autoCompletionBinding = TextFields.bindAutoCompletion(txtSearchString, suggestions);
//  }
//
//
//  /**
//   * Imports a file.
//   */
//  public void importFile() {
//    final Path selectedFile = showImportFileDialog(mainControllerWrapper.getMainStage());
//
//    if (selectedFile != null && Files.exists(selectedFile)) {
//      safelyExecuteMethod(file -> {
//        app.importFile(file);
//        refreshEntriesInGui();
//      }, selectedFile);
//    }
//  }
//
//
//  /**
//   * Saves an already opened file or shows the save as dialog if the file does not exist yet.
//   */
//  public void save() {
//    final Path file = config.getFile();
//
//    if (file != null) {
//      app.save();
//    } else {
//      saveAs();
//    }
//
//    checkGui();
//  }
//
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
//   * Workaround to refresh the table view. This especially comes in handy whenever you update an existing item.
//   */
//  private void refreshTableView() {
//    Platform.runLater(() -> {
//      for (int i = 0; i < tvAnimeList.getColumns().size(); i++) {
//        tvAnimeList.getColumns().get(i).setVisible(false);
//        tvAnimeList.getColumns().get(i).setVisible(true);
//      }
//    });
//  }
//
//
//  /**
//   * Terminates the application.
//   */
//  public void exit() {
//    safelyExecuteMethod(file -> {
//      app.exit();
//      Platform.exit();
//    }, null);
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
//
//  /**
//   * Initializes the filter tab, as well as starts the filter list recommendation search.
//   */
//  private void initFilterTab() {
//    controllerWrapper = Main.CONTEXT.getBean(FilterListControllerWrapper.class);
//
//    if (filterTab == null) {
//      filterTab = controllerWrapper.getFilterTab();
//    }
//  }
//
//
//  /**
//   * Opens the filter list tab.
//   */
//  public void showFilterTab() {
//    focusActiveTab(filterTab);
//  }
//
//
//  /**
//   * Cancels and resets the related anime finder.
//   */
//  private void cancelAndResetBackgroundServices() {
//    Main.CONTEXT.getBean(ServiceRepository.class).cancelAllServices();
//  }
//
//
//  /**
//   * Opens the related anime tab.
//   */
//  public void showRelatedAnimeTab() {
//    if (relatedAnimeTab == null) {
//      relatedAnimeTab = Main.CONTEXT.getBean(RelatedAnimeControllerWrapper.class).getRelatedAnimeTab();
//    }
//
//    focusActiveTab(relatedAnimeTab);
//  }
//
//
//  public void showRecommendationsTab() {
//    if (recommendationsTab == null) {
//      recommendationsTab = Main.CONTEXT.getBean(RecommendationsControllerWrapper.class).getRecommendationsTab();
//    }
//
//    focusActiveTab(recommendationsTab);
//  }
//
//
//  public void showCheckListTab() {
//    if (checkListTab == null) {
//      checkListTab = Main.CONTEXT.getBean(CheckListControllerWrapper.class).getCheckListTab();
//    }
//
//    focusActiveTab(checkListTab);
//  }
//
//
//  public void showNewEntry() {
//    Main.CONTEXT.getBean(NewEntryControllerWrapper.class).showNewEntryStage();
//  }
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
//  @Override
//  public void update(final Observable o, final Object object) {
//    if (object == null) {
//      checkGui();
//      return;
//    }
//
//    if (object instanceof ReversibleCommand) {
//      executeCommand((ReversibleCommand) object);
//    }
//  }
//
//
//  public void refreshEntriesInGui() {
//    Platform.runLater(() -> {
//      tvAnimeList.getItems().clear();
//      tvAnimeList.setItems(FXCollections.observableArrayList(app.fetchAnimeList()));
//      autoSizeTableViewColumns();
//    });
//    checkGui();
//  }
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
//   * @return the tabAnimeList
//   */
//  public Tab getTabAnimeList() {
//    return tabAnimeList;
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
//
//
//  /**
//   * Opens the related anime tab.
//   */
//  public void showSearchResultTab() {
//    if (searchResultTab == null) {
//      searchResultTab = Main.CONTEXT.getBean(SearchResultsControllerWrapper.class).getSearchResultsTab();
//    }
//
//    focusActiveTab(searchResultTab);
//  }
//}