package io.github.manami.core.commands;

import com.google.common.eventbus.EventBus;
import io.github.manami.dto.events.AnimeListChangedEvent;
import java.util.Stack;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Command service keeps track of actions and is responsible for knowing if a file is dirty or not.
 *
 * @author manami-project
 * @since 2.0.0
 */
@Named
public class CommandService {

  /**
   * Indicates whether the document is dirty or not.
   */
  private boolean isUnsaved = false;

  /**
   * Stick with all undoable commands which have been made.
   */
  private Stack<ReversibleCommand> done = null;

  /**
   * Stack with all commands which were made undone.
   */
  private Stack<ReversibleCommand> undone = null;

  /**
   * EventBus to inform GUI.
   */
  private final EventBus eventBus;


  /**
   * Creates a new instance.
   *
   * @since 2.0.0
   */
  @Inject
  public CommandService(final EventBus eventBus) {
    this.eventBus = eventBus;
    done = new Stack<>();
    undone = new Stack<>();
  }


  /**
   * Executes a specific command.
   *
   * @param command {@link Command} to execute.
   * @since 2.0.0
   */
  public void executeCommand(final ReversibleCommand command) {
    if (command.execute()) {
      done.add(command);
      setUnsaved(true);
      eventBus.post(new AnimeListChangedEvent());
    }
  }


  /**
   * Undoes the last reversible action.
   *
   * @since 2.0.0
   */
  public void undo() {
    if (!done.empty()) {
      final ReversibleCommand cmd = done.pop();
      undone.add(cmd);
      checkDirtyFlag();
      cmd.undo();
    }
  }


  /**
   * Redoes the last reversible action.
   *
   * @since 2.0.0
   */
  public void redo() {
    if (!undone.empty()) {
      final ReversibleCommand cmd = undone.pop();
      done.add(cmd);
      checkDirtyFlag();
      cmd.redo();
    }
  }


  /**
   * Check if the last executed command was the last one before saving.
   *
   * @since 2.0.0
   */
  private void checkDirtyFlag() {
    isUnsaved = !(done.empty() || (!done.empty() && done.peek().isLastSaved()));
  }


  /**
   * Clears the stack of done and undone commands.
   *
   * @since 2.0.0
   */
  public void clearAll() {
    done.clear();
    undone.clear();
    isUnsaved = false;
  }


  /**
   * Sets the last executed command anew.
   *
   * @since 2.0.0
   */
  public void resetDirtyFlag() {
    for (final ReversibleCommand cmd : done) {
      cmd.setLastSaved(false);
    }

    for (final ReversibleCommand cmd : undone) {
      cmd.setLastSaved(false);
    }

    if (done.size() > 0) {
      done.peek().setLastSaved(true);
    }
  }


  /**
   * Checks whether the stack for executed commands is empty or not.
   *
   * @return True if no {@link Command} has been executed.
   * @since 2.0.0
   */
  public boolean isEmptyDoneCommands() {
    return done.isEmpty();
  }


  /**
   * Checks whether the stack for undone commands is empty or not.
   *
   * @return True if no {@link Command} has been made undone.
   * @since 2.0.0
   */
  public boolean isEmptyUndoneCommands() {
    return undone.isEmpty();
  }

  public boolean isUnsaved() {
    return isUnsaved;
  }

  public void setUnsaved(boolean unsaved) {
    isUnsaved = unsaved;
  }
}
