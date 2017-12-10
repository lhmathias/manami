package io.github.manami.core.commands;

import io.github.manami.core.Manami;
import io.github.manami.dto.entities.Anime;
import io.github.manami.dto.entities.InfoLink;

/**
 * Command for changing the info link.
 */
public class CmdChangeInfoLink extends AbstractReversibleCommand {

  /**
   * Constructor
   *
   * @param anime Anime to change.
   * @param newValue The new value.
   * @param application Instance of the application which reveals access to the persistence functionality.
   */
  public CmdChangeInfoLink(final Anime anime, final InfoLink newValue, final Manami application) {
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
    newAnime.setInfoLink(newValue);
  }
}
