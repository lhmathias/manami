package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.common.EventBus
import io.github.manamiproject.manami.core.events.FileSavedStatusChangedEvent
import java.util.*


internal object CommandServiceImpl : CommandService {

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


    override fun executeCommand(command: Command): Boolean {
        val isSuccessfulExecuted = command.execute()

        if (isSuccessfulExecuted && command is ReversibleCommand) {
            done.add(command)
            setUnsaved(true)
        }

        return isSuccessfulExecuted
    }


    override fun undo() {
        if (!done.empty()) {
            done.pop()?.let { cmd ->
                undone.add(cmd)
                checkDirtyFlag()
                cmd.undo()
            }
        }
    }


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
        setUnsaved(!(done.empty() || (!done.empty() && done.peek().isLastSaved())))
    }


    override fun clearAll() {
        done.clear()
        undone.clear()
        setUnsaved(false)
    }


    private fun resetLastSavepoint() {
        done.forEach { it.setLastSaved(false) }
        undone.forEach { it.setLastSaved(false) }

        if (done.size > 0) {
            done.peek().setLastSaved(true)
        }
    }


    override fun isEmptyDoneCommands() = done.isEmpty()

    override fun isEmptyUndoneCommands() = undone.isEmpty()

    override fun isUnsaved() = isUnsaved

    override fun setUnsaved(value: Boolean) {
        isUnsaved = value

        if(!value) {
            resetLastSavepoint()
        }

        EventBus.publish(FileSavedStatusChangedEvent)
    }
}
