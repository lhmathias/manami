package io.github.manamiproject.manami.core.commands


/**
 * Command service keeps track of actions and is responsible for knowing if a file is dirty or not.
 */
internal interface CommandService {

    /**
     * @return True if the file has unsaved changes.
     */
    fun isUnsaved(): Boolean

    /**
     * Changes status of the opened file.
     * @param value True if the file has unsaved changes.
     */
    fun setUnsaved(value: Boolean)

    /**
     * Executes a specific command.
     * @param command Command to execute.
     * @return True if the command has been executed successfully.
     */
    fun executeCommand(command: Command): Boolean

    /**
     * Undoes the last reversible action.
     */
    fun undo()

    /**
     * Redoes the last reversible action that had been undone.
     */
    fun redo()

    /**
     * Clears the stack of done and undone commands.
     */
    fun clearAll()

    /**
     * Checks whether the stack for executed commands is empty or not.
     * @return True if no Command has been executed.
     */
    fun isEmptyDoneCommands(): Boolean

    /**
     * Checks whether the stack for undone commands is empty or not.
     * @return True if no Command has been made undone.
     */
    fun isEmptyUndoneCommands(): Boolean
}