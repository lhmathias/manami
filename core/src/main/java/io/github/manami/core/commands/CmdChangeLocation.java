package io.github.manami.core.commands;

import io.github.manami.core.Manami;
import io.github.manami.dto.entities.Anime;

/**
 * Command for changing the location.
 *
 * @author manami-project
 * @since 2.0.0
 */
public class CmdChangeLocation extends AbstractReversibleCommand {

  /**
   * Constructor.
   *
   * @param anime Anime to change.
   * @param newValue The new value.
   * @param application Instance of the application which reveals access to the persistence functionality.
   * @since 2.6.0
   */
  public CmdChangeLocation(final Anime anime, final String newValue, final Manami application) {
    app = application;
    oldAnime = anime;
    newAnime = new Anime(
        oldAnime.getTitle(),
        oldAnime.getInfoLink(),
        oldAnime.getEpisodes(),
        oldAnime.getType(),
        oldAnime.getLocation(),
        oldAnime.getThumbnail(),
        oldAnime.getPicture(),
        oldAnime.getId()
    );
    newAnime.setLocation(newValue);
  }
}
