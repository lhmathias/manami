package io.github.manami.core.commands

/**
 * Interface for commands.
 */
interface Command {

    /**
     * Executes a command.
     */
    fun execute(): Boolean
}
