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
internal class CommandServiceImpl @Inject constructor(
        private val eventBus: EventBus
) : CommandService {

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
     * @return true if the command was executed successful
     */
    override fun executeCommand(command: ReversibleCommand): Boolean {
        val executionResult = command.execute()

        if (executionResult) {
            done.add(command)
            isUnsaved = true
            eventBus.post(AnimeListChangedEvent) //FIXME: This can't be happening here anymore
        }

        return executionResult
    }


    /**
     * Undoes the last reversible action.
     */
    override fun undo() {
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
    override fun redo() {
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
    override fun clearAll() {
        done.clear()
        undone.clear()
        isUnsaved = false
    }


    /**
     * Sets the last executed command anew.
     */
    override fun resetDirtyFlag() {
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
    override fun isEmptyDoneCommands() = done.isEmpty()

    /**
     * Checks whether the stack for undone commands is empty or not.
     *
     * @return True if no {@link Command} has been made undone.
     */
    override fun isEmptyUndoneCommands() = undone.isEmpty()

    override fun isUnsaved() = isUnsaved

    override fun setUnsaved(value: Boolean) {
        isUnsaved = value
    }
}
