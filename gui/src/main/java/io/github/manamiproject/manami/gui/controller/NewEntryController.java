package io.github.manamiproject.manami.gui.controller;

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
//