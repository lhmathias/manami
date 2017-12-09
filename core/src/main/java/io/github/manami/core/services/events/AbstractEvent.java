package io.github.manami.core.services.events;

import io.github.manami.dto.entities.Anime;

/**
 * @author manami-project
 * @since 2.6.0
 */
public class AbstractEvent implements Event {

  private EventType type;

  private Anime anime;

  private String title;

  private String message;

  public enum EventType {
    ERROR, WARNING, INFO
  }


  public AbstractEvent(final Anime anime) {
    type = EventType.INFO;
    this.anime = anime;
    title = anime.getTitle();
  }


  public AbstractEvent() {
    type = EventType.INFO;
  }

  @Override
  public EventType getType() {
    return type;
  }

  @Override
  public void setType(EventType type) {
    this.type = type;
  }

  @Override
  public Anime getAnime() {
    return anime;
  }

  @Override
  public void setAnime(Anime anime) {
    this.anime = anime;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public void setMessage(String message) {
    this.message = message;
  }
}
