package io.github.manamiproject.manami.gui.controller;

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