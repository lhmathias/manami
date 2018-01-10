package io.github.manami.core.commands

import com.google.common.eventbus.EventBus
import io.github.manami.persistence.events.AnimeListChangedEvent
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Command service keeps track of actions and is responsible for knowing if a file is dirty or not.
 */
@Named
class CommandService @Inject constructor(
        private val eventBus: EventBus
) {

    /**
     * Indicates whether the document is dirty or not.
     */
    private var isUnsaved: Boolean = false

    /**
     * Stick with all undoable commands which have been made.
     */
    private val done: Stack<ReversibleCommand> = Stack()

    /**
     * Stack with all commands which were made undone.
     */
    private val undone: Stack<ReversibleCommand> = Stack()


    /**
     * Executes a specific command.
     *
     * @param command {@link Command} to process.
     */
    fun executeCommand(command: ReversibleCommand) {
        if (command.execute()) {
            done.add(command)
            isUnsaved = true
            eventBus.post(AnimeListChangedEvent)
        }
    }


    /**
     * Undoes the last reversible action.
     */
    fun undo() {
        if (!done.empty()) {
            done.pop()?.let { cmd ->
                undone.add(cmd)
                checkDirtyFlag()
                cmd.undo()
            }
        }
    }


    /**
     * Redoes the last reversible action.
     */
    fun redo() {
        if (!undone.empty()) {
            undone.pop()?.let { cmd ->
                done.add(cmd)
                checkDirtyFlag()
                cmd.redo()
            }
        }
    }


    /**
     * Check if the last executed command was the last one before saving.
     */
    private fun checkDirtyFlag() {
        isUnsaved = !(done.empty() || (!done.empty() && done.peek().isLastSaved()))
    }


    /**
     * Clears the stack of done and undone commands.
     */
    fun clearAll() {
        done.clear()
        undone.clear()
        isUnsaved = false
    }


    /**
     * Sets the last executed command anew.
     */
    fun resetDirtyFlag() {
        done.forEach { it.setLastSaved(false) }
        undone.forEach { it.setLastSaved(false) }

        if (done.size > 0) {
            done.peek().setLastSaved(true)
        }
    }


    /**
     * Checks whether the stack for executed commands is empty or not.
     *
     * @return True if no {@link Command} has been executed.
     */
    fun isEmptyDoneCommands() = done.isEmpty()

    /**
     * Checks whether the stack for undone commands is empty or not.
     *
     * @return True if no {@link Command} has been made undone.
     */
    fun isEmptyUndoneCommands() = undone.isEmpty()
}
