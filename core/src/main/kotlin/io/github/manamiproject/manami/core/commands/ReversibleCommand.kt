package io.github.manamiproject.manami.core.commands

/**
 * Interface for reversible commands.
 */
interface ReversibleCommand : Command {

    /**
     * Undoes the last reversible command.
     */
    fun undo()


    /**
     * Redoes the last reversible command which was undone before.
     */
    fun redo()


    /**
     * @return True if this command is the last which was executed before saving.
     */
    fun isLastSaved(): Boolean


    /**
     * @param value True if this command is the last which was executed before saving.
     */
    fun setLastSaved(value: Boolean)
}
