package io.github.manami.core.tasks;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

import com.sun.javafx.collections.ObservableSetWrapper;
import io.github.manami.cache.Cache;
import io.github.manami.core.ManamiImpl;
import io.github.manami.core.tasks.events.ProgressState;
import io.github.manami.dto.entities.Anime;
import io.github.manami.dto.entities.InfoLink;
import io.github.manami.dto.entities.MinimalEntry;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Optional;
import java.util.Stack;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Finds related anime in info site links. Always start {@link BackgroundTask}s using the {@link ServiceRepository}!
 */
public class RelatedAnimeFinderTask extends AbstractTask<Map<InfoLink, Anime>> {

  private static final Logger log = LoggerFactory.getLogger(RelatedAnimeFinderTask.class);
  /**
   * Contains all anime which are already in the anime list.
   */
  private final List<InfoLink> myAnime;

  /**
   * Stack of anime which need to be checked.
   */
  private Stack<InfoLink> animeToCheck;

  /**
   * List of all related anime. This output is being shown to the user.
   */
  private final Map<InfoLink, Anime> relatedAnime;

  /**
   * Anime which have already been checked.
   */
  private final ObservableSet<InfoLink> checkedAnime;

  /**
   * Instance of the cache.
   */
  private final Cache cache;

  /**
   * Core application.
   */
  private final ManamiImpl app;

  private final List<? extends MinimalEntry> list;


  /**
   * @param cache Instance of the cache.
   * @param app Instance of the manami app.
   * @param list List which being checked for related anime.
   * @param observer Observer which is being notified about the progress. It also gets the result as a list through notification.
   */
  public RelatedAnimeFinderTask(final Cache cache, final ManamiImpl app, final List<? extends MinimalEntry> list, final Observer observer) {
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
