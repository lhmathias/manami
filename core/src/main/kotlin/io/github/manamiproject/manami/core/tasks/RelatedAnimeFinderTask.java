package io.github.manamiproject.manami.core.tasks;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Finds related anime in info site links. Always start {@link BackgroundTask}s using the {@link ServiceRepository}!
 */
/*
public class RelatedAnimeFinderTask extends AbstractTask<Map<InfoLink, Anime>> {

  private static final Logger log = LoggerFactory.getLogger(RelatedAnimeFinderTask.class);
    //Contains all anime which are already in the anime list.
  private final List<InfoLink> myAnime;

   // Stack of anime which need to be checked.
  private Stack<InfoLink> animeToCheck;

    //List of all related anime. This output is being shown to the user.
  private final Map<InfoLink, Anime> relatedAnime;

   // Anime which have already been checked.
  private final ObservableSet<InfoLink> checkedAnime;

  private final CacheI cache;

  private final Manami app;

  private final List<? extends MinimalEntry> list;


  public RelatedAnimeFinderTask(final CacheI cache, final Manami app, final List<? extends MinimalEntry> list, final Observer observer) {
    this.app = app;
    this.cache = cache;
    this.list = list;
    addObserver(observer);
    myAnime = newArrayList();
    relatedAnime = newHashMap();
    animeToCheck = new Stack<>();
    checkedAnime = new ObservableSetWrapper<>(newHashSet());
    checkedAnime.addListener((SetChangeListener<InfoLink>) event -> {
      setChanged();
      notifyObservers(new ProgressState(checkedAnime.size() + 1, animeToCheck.size()));
    });
  }


  @Override
  public Map<InfoLink, Anime> execute() {
    list.forEach(entry -> {
      final InfoLink infoLink = entry.getInfoLink();
      if (infoLink.isValid()) {
        myAnime.add(infoLink);
        animeToCheck.push(infoLink);
      }
    });

    // Sort stack
    final Stack<InfoLink> sortedStack = new Stack<>();
    while (!animeToCheck.isEmpty()) {
      sortedStack.push(animeToCheck.pop());
    }
    animeToCheck = sortedStack;

    while (!animeToCheck.empty() && !isInterrupt()) {
      final InfoLink entry = animeToCheck.pop();

      if (!checkedAnime.contains(entry)) {
        MDC.put("infoLink", entry.getUrl());
        log.debug("Checking for related anime.");
        checkAnime(entry);
      }
    }

    return relatedAnime;
  }


  @Override
  public void reset() {
    cancel();
    myAnime.clear();
    animeToCheck.clear();
    relatedAnime.clear();
    checkedAnime.clear();
    super.reset();
  }


  private void checkAnime(final InfoLink infoLink) {
    final List<Anime> showAnimeList = newArrayList();
    final Optional<Anime> optCachedAnime = cache.fetchAnime(infoLink);

    if (!optCachedAnime.isPresent()) {
      return;
    }

    final Anime cachedAnime = optCachedAnime.get();

    final List<InfoLink> relatedAnimeList = newArrayList();
    cache.fetchRelatedAnime(cachedAnime.getInfoLink()).forEach(relatedAnimeList::add);

    if (log.isTraceEnabled()) {
      relatedAnimeList.forEach(e -> log.trace("{}", e));
    }

    relatedAnimeList.stream().filter(e -> e.isValid()).filter(e -> !animeToCheck.contains(e)).filter(e -> !checkedAnime.contains(e))
        .filter(e -> !app.filterListEntryExists(e)).forEach(animeToCheck::push);
    relatedAnimeList.stream().filter(e -> e.isValid()).filter(e -> !relatedAnime.containsKey(e)).filter(e -> !app.animeEntryExists(e))
        .filter(e -> !app.watchListEntryExists(e)).filter(e -> !app.filterListEntryExists(e)).forEach(e -> {
      final Anime curAnime = cache.fetchAnime(e).get();
      relatedAnime.put(e, curAnime);
      showAnimeList.add(curAnime);
    });

    setChanged();
    notifyObservers(showAnimeList);
    checkedAnime.add(infoLink);
  }
}
*/