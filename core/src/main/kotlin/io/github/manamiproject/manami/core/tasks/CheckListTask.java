package io.github.manamiproject.manami.core.tasks;


/*
public class CheckListTask extends AbstractTask<Void> {

  private static final Logger log = LoggerFactory.getLogger(CheckListTask.class);
  private static final String MSG_DEAD_INFOLINK = "The infoLink seems to not exist anymore for %s.";
  private final CacheI cache;
  private final List<Anime> list;
  private final CheckListConfig config;
  private Path currentWorkingDir = null;
  private int currentProgress = 0;
  private int progressMax = 0;
  private final Manami app;


  public CheckListTask(final CheckListConfig config, final Path file, final CacheI cache, final Manami app, final Observer observer) {
    this.config = config;
    this.cache = cache;
    this.app = app;
    list = app.fetchAnimeList();
    addObserver(observer);

    if (file != null) {
      currentWorkingDir = file.getParent();
    }
  }


  @Override
  public Void execute() {
    notNull(list, "List of anime cannot be null");

    countProgressMax();

    if (config.getCheckLocations()) {
      checkLocations();
    }

    if (config.getCheckCrc()) {
      checkCrc();
    }

    if (config.getCheckMetaData()) {
      checkMetaData();
    }

    if (config.getCheckDeadEntries()) {
      checkDeadEntries();
    }

    return null;
  }


  private void countProgressMax() {
    if (config.getCheckLocations()) {
      progressMax += list.size();
    }

    if (config.getCheckCrc()) {
      for (final Anime entry : list) {
        final String location = entry.getLocation();

        if (isNotBlank(location)) {
          final Optional<Path> optDir = PathResolver.buildPath(location, currentWorkingDir);

          if (!optDir.isPresent()) {
            continue;
          }

          long amount = 0L;
          try {
            amount = Files.list(optDir.get()).filter(p -> isRegularFile(p)).count();
          } catch (final IOException e) {
            log.error("An error occurred detecting the amount of files for {}: ", entry.getTitle(), e);
          }

          progressMax += (int) amount;
        }
      }
    }

    if (config.getCheckMetaData()) {
      progressMax += list.size();
    }

    if (config.getCheckDeadEntries()) {
      progressMax += app.fetchWatchList().size();
      progressMax += app.fetchFilterList().size();
    }
  }


  //Checks every entry. A location must be set, exist and contain at least one file.
  private void checkLocations() {
    try {
      for (int index = 0; index < list.size() && !isInterrupt(); index++) {
        updateProgress();
        final Anime anime = list.get(index);
        log.debug("Checking location of {}", anime.getTitle());

        // 01 - Is location set?
        if (isBlank(anime.getLocation())) {
          fireNoLocationEvent(anime);
          continue;
        }

        // 02 - Does location exist?
        final Optional<Path> optDir = PathResolver.buildPath(anime.getLocation(), currentWorkingDir);

        if (!optDir.isPresent()) {
          fireLocationNotFoundEvent(anime);
          continue;
        }

        // 03 - Contains at least one file / the exact same amount files
        // as episodes
        try (DirectoryStream<Path> dirStream = newDirectoryStream(optDir.get())) {
          int counter = 0;
          for (final Path curPath : dirStream) {
            if (isRegularFile(curPath)) {
              counter++;
            }
          }

          if (counter == 0) {
            fireLocationEmptyEvent(anime);
          } else if (counter != anime.getEpisodes()) {
            fireDifferentAmountOfEpisodesEvent(anime);
          }
        } catch (final Exception e) {
          log.error("An error occurred during file check: ", e);
        }

        //04 conversion to relative path possible? At this point we
        //know the directory exists. If wen can access it directly it's
        //an absolute path.
        final Path dir = Paths.get(anime.getLocation());
        if (Files.exists(dir) && Files.isDirectory(dir)) {
          fireRelativizePathEvent(anime);
        }
      }

    } catch (final Exception e) {
      e.printStackTrace();
    }
  }


  private void updateProgress() {
    currentProgress++;
    setChanged();
    notifyObservers(new ProgressState(currentProgress, progressMax));
  }


  private void checkMetaData() {
    for (int index = 0; index < list.size() && !isInterrupt(); index++) {
      updateProgress();
      final Anime anime = list.get(index);

      if (!anime.getInfoLink().isValid()) {
        continue;
      }

      final Optional<Anime> optCachedEntry = cache.fetchAnime(anime.getInfoLink());

      if (!optCachedEntry.isPresent()) {
        continue;
      }

      final Anime cachedEntry = optCachedEntry.get();

      if (!anime.getTitle().equals(cachedEntry.getTitle())) {
        fireTitleDiffersEvent(anime, cachedEntry.getTitle());
        continue;
      }

      if (anime.getEpisodes() != cachedEntry.getEpisodes()) {
        fireEpisodesDiffersEvent(anime, cachedEntry.getEpisodes());
        continue;
      }

      if (anime.getType() != cachedEntry.getType()) {
        fireTypeDiffersEvent(anime, cachedEntry.getType());
      }
    }
  }


  private void fireTypeDiffersEvent(final Anime element, final AnimeType newValue) {
    final TypeDifferEvent event = new TypeDifferEvent(element, newValue, app);
    event.setType(EventType.WARNING);
    event.setMessage(String.format("The local type is \"%s\"\nand the type from the info link is \"%s\"", element.getTypeAsString(), newValue));
    fire(event);
  }


  private void fireEpisodesDiffersEvent(final Anime element, final int newValue) {
    final EpisodesDifferEvent event = new EpisodesDifferEvent(element, newValue, app);
    event.setType(EventType.WARNING);
    event.setMessage(
        String.format("The local number of episodes is \"%s\"\nand the amount from the info link is \"%s\"", element.getEpisodes(), newValue));
    fire(event);
  }


  private void fireTitleDiffersEvent(final Anime element, final String newValue) {
    final TitleDifferEvent event = new TitleDifferEvent(element, newValue, app);
    event.setType(EventType.WARNING);
    event.setMessage(String.format("The local title is \"%s\"\nand title from the info link is \"%s\"", element.getTitle(), newValue));
    fire(event);
  }


  private void checkCrc() {
    for (int index = 0; index < list.size() && !isInterrupt(); index++) {
      final Anime anime = list.get(index);
      if (isBlank(anime.getLocation())) {
        continue;
      }

      log.debug("Checking CRC32 sum of {}", anime.getTitle());

      final Optional<Path> optDir = PathResolver.buildPath(anime.getLocation(), currentWorkingDir);

      if (!optDir.isPresent()) {
        continue;
      }

      try (DirectoryStream<Path> dirStream = newDirectoryStream(optDir.get())) {
        for (final Path path : dirStream) {
          updateProgress();
          if (!isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {
            continue;
          }

          int cnt;
          final Checksum crc = new CRC32();

          try (final InputStream inputStream = new BufferedInputStream(new FileInputStream(path.toString()))) {
            while ((cnt = inputStream.read()) != -1) {
              crc.update(cnt);
            }
          }

          final String crcSum = Long.toHexString(crc.getValue());
          final Pattern pattern = Pattern.compile("\\[.{8}\\]");
          final Matcher matcher = pattern.matcher(path.getFileName().toString());

          if (matcher.find()) {
            final String titleCrc = matcher.group().replace("[", EMPTY).replace("]", EMPTY);

            if (!titleCrc.equalsIgnoreCase(crcSum)) {
              fireCrcSumsDifferEvent(path);
            }
          } else {
            fireNoCrcSumEvent(path, crcSum);
          }
        }
      } catch (final Exception e) {
        log.error("An error occurred during CRC sum check: ", e);
      }
    }
  }


  private void fireNoCrcSumEvent(final Path path, final String crcSum) {
    final CrcEvent event = new CrcEvent();
    event.setType(EventType.WARNING);
    event.setTitle(path.toAbsolutePath().toString());
    event.setMessage("File has no CRC sum.");
    event.setPath(path);
    event.setCrcSum(crcSum);
    fire(event);
  }


  private void fireCrcSumsDifferEvent(final Path path) {
    final CrcEvent event = new CrcEvent();
    event.setType(EventType.ERROR);
    event.setTitle(path.toAbsolutePath().toString());
    event.setMessage("CRC sums differ!");
    fire(event);
  }


  private void fireNoLocationEvent(final Anime anime) {
    final SimpleLocationEvent event = createErrorEvent(anime);
    event.setMessage("Location is not set.");
    fire(event);
  }


  private void fireDifferentAmountOfEpisodesEvent(final Anime anime) {
    final SimpleLocationEvent event = new SimpleLocationEvent(anime);
    event.setType(EventType.WARNING);
    event.setMessage("Amount of files differs from amount of episodes.");
    fire(event);
  }


  private void fireLocationEmptyEvent(final Anime anime) {
    final SimpleLocationEvent event = createErrorEvent(anime);
    event.setMessage("Location is empty.");
    fire(event);
  }


  private void fireLocationNotFoundEvent(final Anime anime) {
    final SimpleLocationEvent event = createErrorEvent(anime);
    event.setMessage("Location does not exist.");
    fire(event);
  }


  private SimpleLocationEvent createErrorEvent(final Anime anime) {
    final SimpleLocationEvent event = new SimpleLocationEvent(anime);
    event.setType(EventType.ERROR);
    return event;
  }


  private void fireRelativizePathEvent(final Anime anime) {
    final String newValue = PathResolver.buildRelativizedPath(anime.getLocation(), currentWorkingDir);

    final RelativizeLocationEvent event = new RelativizeLocationEvent(anime, newValue, app);
    event.setType(EventType.INFO);
    event.setMessage("This path can be converted to a relative path.");
    fire(event);
  }


  private void fire(final Object event) {
    if (!isInterrupt()) {
      setChanged();
      notifyObservers(event);
    }
  }


  private void checkDeadEntries() {
    final List<MinimalEntry> immutableWatchList = ImmutableList.copyOf(app.fetchWatchList());

    for (int index = 0; index < immutableWatchList.size() && !isInterrupt(); index++) {
      final MinimalEntry currentEntry = immutableWatchList.get(index);
      checkEntryForDeadLink(currentEntry);
    }

    final List<MinimalEntry> immutableFilterList = ImmutableList.copyOf(app.fetchFilterList());

    for (int index = 0; index < immutableFilterList.size() && !isInterrupt(); index++) {
      final MinimalEntry currentEntry = immutableFilterList.get(index);
      checkEntryForDeadLink(currentEntry);
    }
  }


  private void checkEntryForDeadLink(final MinimalEntry currentEntry) {
    updateProgress();

    if (currentEntry == null || currentEntry.getInfoLink() == null || !currentEntry.getInfoLink().isValid()) {
      return;
    }

    final Optional<Anime> cacheEntry = cache.fetchAnime(currentEntry.getInfoLink());

    if (!cacheEntry.isPresent()) {
      fireDeadLinkEvent(currentEntry);
    }
  }


  private void fireDeadLinkEvent(final MinimalEntry currentEntry) {
    final DeadLinkEvent event = new DeadLinkEvent(currentEntry, app);
    event.setType(EventType.ERROR);
    event.setMessage(String.format(MSG_DEAD_INFOLINK, currentEntry.getTitle()));
    fire(event);
  }
}*/