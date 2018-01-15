package io.github.manamiproject.manami.core.commands

/**
 * Interface for commands.
 */
internal interface Command {

    /**
     * Executes a command.
     */
    fun execute(): Boolean
}
