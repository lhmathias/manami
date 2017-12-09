package io.github.manami.core.services.events;


/**
 * Used by services to notify observers of the progress.
 *
 * @author manami-project
 * @since 2.3.0
 */
public class ProgressState {

  /**
   * Number of items which have already been processed.
   */
  private final int done;

  /**
   * Number of items which still need to be processed.
   */
  private final int todo;


  /**
   * Constructor
   *
   * @param done Number of processed items.
   * @param todo Number of items which still need to be processed.
   * @since 2.3.0
   */
  public ProgressState(final int done, final int todo) {
    this.done = done;
    this.todo = todo;
  }

  public int getDone() {
    return done;
  }

  public int getTodo() {
    return todo;
  }
}
