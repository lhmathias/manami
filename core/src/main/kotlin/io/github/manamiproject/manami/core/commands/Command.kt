package io.github.manamiproject.manami.core.commands

/**
 * Interface for commands.
 */
interface Command {

    /**
     * Executes a command.
     * @return True if the command has been executed successfully.
     */
    fun execute(): Boolean
}
