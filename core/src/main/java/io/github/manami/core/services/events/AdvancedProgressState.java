package io.github.manami.core.services.events;

import io.github.manami.dto.entities.Anime;

/**
 * Contains the state as well as payload.
 */
public class AdvancedProgressState extends ProgressState {

  private Anime anime;


  public AdvancedProgressState(final int done, final int todo, final Anime anime) {
    super(done, todo);
    this.anime = anime;
  }

  public Anime getAnime() {
    return anime;
  }

  public void setAnime(Anime anime) {
    this.anime = anime;
  }
}
