package io.github.manamiproject.manami.core.commands


/**
 * Command service keeps track of actions and is responsible for knowing if a file is dirty or not.
 */
internal interface CommandService {

    fun isUnsaved(): Boolean

    fun setUnsaved(value: Boolean)

    /**
     * Executes a specific command.
     *
     * @param command {@link Command} to migrate.
     * @return true if the command was executed successful
     */
    fun executeCommand(command: ReversibleCommand): Boolean

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
     * Sets the last executed command anew.
     */
    fun resetDirtyFlag() //FIXME: This should be internal part of setUnsaved()

    /**
     * Checks whether the stack for executed commands is empty or not.
     *
     * @return True if no {@link Command} has been executed.
     */
    fun isEmptyDoneCommands(): Boolean

    /**
     * Checks whether the stack for undone commands is empty or not.
     *
     * @return True if no {@link Command} has been made undone.
     */
    fun isEmptyUndoneCommands(): Boolean
}