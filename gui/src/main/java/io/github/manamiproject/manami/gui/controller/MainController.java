package io.github.manamiproject.manami.gui.controller;


///**
// * Controller for the main stage.
// */
//public class MainController implements Observer {
//
//  private static final Logger log = LoggerFactory.getLogger(MainController.class);
//
//  /**
//   * Instance of the main application.
//   */
//  final private Manami app = Main.CONTEXT.getBean(Manami.class);
//
//  /**
//   * Instance of the main application.
//   */
//  final private CommandService cmdService = Main.CONTEXT.getBean(CommandService.class);
//
//  /**
//   * Instance of the gui configuration.
//   */
//  final private MainControllerWrapper mainControllerWrapper = Main.CONTEXT.getBean(MainControllerWrapper.class);
//
//  /**
//   * Instance of the application configuration.
//   */
//  final private Config config = Main.CONTEXT.getBean(Config.class);
//
//  /**
//   * Tab for the filter list.
//   */
//  private Tab filterTab;
//
//  /**
//   * Tab for the related anime.
//   */
//  private Tab relatedAnimeTab;
//
//  /**
//   * Tab for the recommendations.
//   */
//  private Tab recommendationsTab;
//
//  /**
//   * Tab for the check list.
//   */
//  private Tab checkListTab;
//
//  /**
//   * Tab for search results.
//   */
//  private Tab searchResultTab;
//
//  /**
//   * Tab for tag list.
//   */
//  private Tab tagListTab;
//
//  private FilterListControllerWrapper controllerWrapper;
//
//  /**
//   * Tab for the watch list.
//   */
//  private Tab watchListTab;
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
//    // Callbacks
//    final Callback<TableColumn<Anime, String>, TableCell<Anime, String>> defaultCallback = new DefaultCallback();
//
//    // COLUMN: Number
//    colAnimeListNumber.setCellValueFactory(value -> new ReadOnlyObjectWrapper<>(value.getValue()));
//    colAnimeListNumber.setCellFactory(new RowCountCallback());
//
//    // COLUMN: Title
//    colAnimeListTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
//    colAnimeListTitle.setComparator(String::compareToIgnoreCase);
//    colAnimeListTitle.setCellFactory(defaultCallback);
//    colAnimeListTitle.setOnEditCommit(event -> {
//      final Anime selectedAnime = tvAnimeList.getItems().get(event.getTablePosition().getRow());
//      final Anime oldValue = copyAnime(selectedAnime);
//      final String newTitle = event.getNewValue().trim();
//      executeCommand(new CmdChangeTitle(oldValue, newTitle, app));
//      selectedAnime.setTitle(newTitle);
//    });
//
//    // COLUMN: Type
//    colAnimeListType.setCellValueFactory(new PropertyValueFactory<>("typeAsString"));
//    colAnimeListType.setCellFactory(new AnimeTypeCallback());
//    colAnimeListType.setOnEditCommit(event -> {
//      final Anime selectedAnime = tvAnimeList.getItems().get(event.getTablePosition().getRow());
//      final Anime oldValue = copyAnime(selectedAnime);
//      executeCommand(new CmdChangeType(oldValue, AnimeType.findByName(event.getNewValue()), app));
//      selectedAnime.setType(AnimeType.findByName(event.getNewValue()));
//    });
//
//    // COLUMN: Episodes
//    colAnimeListEpisodes.setCellValueFactory(new PropertyValueFactory<>("episodes"));
//    colAnimeListEpisodes.setCellFactory(new AnimeEpisodesCallback());
//    colAnimeListEpisodes.setOnEditCommit(event -> {
//      final Anime selectedAnime = tvAnimeList.getItems().get(event.getTablePosition().getRow());
//      final Anime oldValue = copyAnime(selectedAnime);
//      executeCommand(new CmdChangeEpisodes(oldValue, event.getNewValue(), app));
//      selectedAnime.setEpisodes(event.getNewValue());
//    });
//
//    // COLUMN: InfoLink
//    colAnimeListLink.setCellValueFactory(new PropertyValueFactory<>("infoLink"));
//    colAnimeListLink.setCellFactory(new AnimeInfoLinkCallback());
//    colAnimeListLink.setOnEditCommit(event -> {
//      final Anime selectedAnime = tvAnimeList.getItems().get(event.getTablePosition().getRow());
//      final Anime oldValue = copyAnime(selectedAnime);
//      final InfoLink newValue = event.getNewValue();
//      executeCommand(new CmdChangeInfoLink(oldValue, newValue, app));
//      selectedAnime.setInfoLink(newValue);
//    });
//
//    // COLUMN: Location
//    colAnimeListLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
//    colAnimeListLocation.setCellFactory(defaultCallback);
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
//
//  /**
//   * Undoes the last command.
//   */
//  public void undo() {
//    cmdService.undo();
//    refreshEntriesInGui();
//  }
//
//
//  /**
//   * Redoes the last undone command.
//   */
//  public void redo() {
//    cmdService.redo();
//    refreshEntriesInGui();
//  }
//
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
//  /**
//   * Interface for operations that need a secure execution context.
//   */
//  private interface ExecutionContext {
//
//    void execute(Path file);
//  }
//
//
//  /**
//   * Checks whether the current state is dirty and only executes the given Method if it's not.
//   *
//   * @param execCtx Lambda function trigger
//   * @param file File.
//   */
//  private void safelyExecuteMethod(final ExecutionContext execCtx, final Path file) {
//    final int userSelection = (cmdService.isUnsaved()) ? showUnsavedChangesDialog() : 0;
//
//    switch (userSelection) {
//      case 1:
//        save();
//      case 0:
//        tabPane.getSelectionModel().select(tabAnimeList);
//        execCtx.execute(file);
//        break;
//      default:
//        break;
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
//  private void executeCommand(final ReversibleCommand command) {
//    cmdService.executeCommand(command);
//    checkGui();
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
//
//  public void showAbout() {
//    Main.CONTEXT.getBean(AboutController.class).showAbout();
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