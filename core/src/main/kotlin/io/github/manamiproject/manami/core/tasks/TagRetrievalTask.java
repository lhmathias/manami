package io.github.manamiproject.manami.core.tasks;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Finds related anime in info site links. Always start {@link BackgroundTask}s using the {@link ServiceRepository}!
 */
/*
public class TagRetrievalTask extends AbstractTask<Void> {
  private static final Logger log = LoggerFactory.getLogger(TagRetrievalTask.class);
  private static final String LOG_MSG_LAST_PAGE = "Last page. No more entries for this genre.";


  private final CacheI cache;


  private final Manami app;

  private final String tagUrl;

  private final JavaUrlConnection javaUrlConnection;

  private final Set<InfoLink> foundAll;
  private Set<InfoLink> foundPerPage;
  private Set<InfoLink> foundPreviousPage;
  private final Pattern pattern;


  public TagRetrievalTask(final CacheI cache, final Manami app, final String tagUrl, final Observer observer) {
    this.app = app;
    this.cache = cache;
    this.tagUrl = tagUrl;
    addObserver(observer);
    javaUrlConnection = new JavaUrlConnection();
    foundAll = newHashSet();
    foundPerPage = newHashSet();
    foundPreviousPage = newHashSet();
    pattern = Pattern.compile("https://myanimelist\\.net/anime/[0-9]+");
  }


  @Override
  public Void execute() {
    log.info("############################################ START ############################################");
    final InfoLink genre = new InfoLink(tagUrl);
    int pageCounter = 1;

    while (pageCounter > 0) {
      log.info("-------------------------------------------- Page {} --------------------------------------------", pageCounter);
      final InfoLink genrePage = new InfoLink(String.format("%s?page=%s", genre.getUrl(), pageCounter));
      String pageAsString = javaUrlConnection.pageAsString(genrePage);
      pageAsString = pageAsString.substring(0, pageAsString.indexOf("<div class=\"footer-ranking\">"));

      if (!pageAsString.contains("404 Not Found - MyAnimeList.net")) {
        extractAnimes(pageAsString);

        if (pageCounter == 1 || symmetricDifference(foundPerPage, foundPreviousPage).size() > 0) {
          pageCounter++;
        } else {
          pageCounter = -1;
          log.info(LOG_MSG_LAST_PAGE);
        }
      } else {
        pageCounter = -1;
        log.info(LOG_MSG_LAST_PAGE);
      }
    }

    log.info("############################################ STOP ############################################");

    setChanged();
    notifyObservers(Boolean.TRUE);

    return null;
  }


  private void extractAnimes(final String pageAsString) {
    foundPreviousPage = ImmutableSet.copyOf(foundPerPage);
    foundPerPage = newHashSet();
    final Matcher matcher = pattern.matcher(pageAsString);

    while (matcher.find()) {
      foundPerPage.add(new InfoLink(matcher.group()));
    }

    foundPerPage.forEach(e -> log.info("{}", e));

    foundPerPage.stream().filter(e -> e.isValid()).filter(e -> !foundAll.contains(e)).filter(e -> !app.animeEntryExists(e))
        .filter(e -> !app.watchListEntryExists(e)).filter(e -> !app.filterListEntryExists(e)).forEach(e -> {
      final Optional<Anime> anime = cache.fetchAnime(e);
      if (anime.isPresent()) {
        setChanged();
        notifyObservers(anime.get());
      }
    });

    foundAll.addAll(foundPerPage);
  }


  @Override
  public void reset() {
    cancel();
    super.reset();
  }
}*/
