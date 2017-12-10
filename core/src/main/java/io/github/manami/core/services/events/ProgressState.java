package io.github.manami.core.services.events;


/**
 * Used by services to notify observers of the progress.
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
