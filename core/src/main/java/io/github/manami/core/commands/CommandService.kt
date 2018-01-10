package io.github.manami.core.commands


interface CommandService {

    fun isUnsaved(): Boolean

    fun setUnsaved(value: Boolean)

    fun executeCommand(command: ReversibleCommand)

    fun undo()

    fun redo()

    fun clearAll()

    fun resetDirtyFlag()

    fun isEmptyDoneCommands(): Boolean

    fun isEmptyUndoneCommands(): Boolean
}