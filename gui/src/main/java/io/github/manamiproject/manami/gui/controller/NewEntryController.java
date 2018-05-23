package io.github.manamiproject.manami.gui.controller;

///**
// * Shows the window in which a new entry can be created.
// */
//public class NewEntryController implements Observer {
//
//  /**
//   * Called on Construction
//   */
//  public void initialize() {
//    txtInfoLink.focusedProperty().addListener((currentValue, valueBefore, valueAfter) -> {
//      if (valueBefore && !valueAfter) {
//        autoFillForm();
//      }
//    });
//  }
//
//
//  /**
//   * Adds a new entry to the list.
//   */
//  public void add() {
//    final String title = txtTitle.getText().trim();
//    final Integer episodes = Integer.valueOf(txtEpisodes.getText().trim());
//    final InfoLink infoLink = new InfoLink(txtInfoLink.getText().trim());
//    final String location = txtLocation.getText().trim();
//    final String type = txtType.getText().trim();
//    if (validationSupport.getValidationResult().getErrors().size() == 0) {
//      cmdService.executeCommand(new CmdAddAnime(new Anime(title, infoLink).type(AnimeType.findByName(type)).episodes(episodes).location(location),
//          Main.CONTEXT.getBean(Manami.class)));
//      close();
//    }
//  }
//
//  /**
//   * Checks the currently given value of the textfield and tries to automatically fill out the form.
//   */
//  private void autoFillForm() {
//    AnimeRetrievalTask retrievalService;
//
//    convertUrlIfNecessary();
//
//    final InfoLink infoLink = new InfoLink(txtInfoLink.getText().trim());
//
//    if (infoLink.isValid()) {
//      setDisableAutoCompleteWidgets(true);
//
//      retrievalService = new AnimeRetrievalTask(Main.CONTEXT.getBean(CacheI.class), infoLink);
//      retrievalService.addObserver(this);
//      serviceRepo.startService(retrievalService);
//
//    }
//  }
//
//
//  /**
//   * Enables or disables all widgets on the scene which are filled by autocomplete.
//   *
//   * @param value Disables the component if the value is true and enables them if the value is false.
//   */
//  private void setDisableAutoCompleteWidgets(final boolean value) {
//    Platform.runLater(() -> {
//      txtTitle.setDisable(value);
//      txtEpisodes.setDisable(value);
//      txtInfoLink.setDisable(value);
//      btnAdd.setDisable(value);
//    });
//
//    if (value) {
//      Platform.runLater(() -> {
//        btnTypeUp.setDisable(true);
//        btnTypeDown.setDisable(true);
//        btnEpisodeUp.setDisable(true);
//        btnEpisodeDown.setDisable(true);
//      });
//    } else {
//      checkTypeArrowButtons();
//      checkEpisodeArrowButtons();
//    }
//  }
//
//
//  public void browse() {
//    final Path directory = showBrowseForFolderDialog(Main.CONTEXT.getBean(MainControllerWrapper.class).getMainStage());
//    String location;
//
//    if (directory != null) {
//      if (config.getFile() == null) {
//        location = directory.toAbsolutePath().toString();
//      } else {
//        try {
//          location = config.getFile().getParent().relativize(directory).toString().replace("\\", "/");
//        } catch (final IllegalArgumentException e) {
//          location = directory.toAbsolutePath().toString();
//        }
//      }
//
//      txtLocation.setText(location);
//    }
//  }
//
//
//  @Override
//  public void update(final Observable observable, final Object object) {
//    if (observable == null || object == null) {
//      return;
//    }
//
//    if (observable instanceof AnimeRetrievalTask && object instanceof Anime) {
//      final Anime anime = (Anime) object;
//
//      if (anime != null) {
//        Platform.runLater(() -> {
//          txtTitle.setText(anime.getTitle());
//          txtEpisodes.setText(String.valueOf(anime.getEpisodes()));
//          txtInfoLink.setText(anime.getInfoLink().getUrl());
//          setTextfieldType(anime.getTypeAsString());
//          checkEpisodeArrowButtons();
//        });
//      }
//      setDisableAutoCompleteWidgets(false);
//    }
//  }
//}