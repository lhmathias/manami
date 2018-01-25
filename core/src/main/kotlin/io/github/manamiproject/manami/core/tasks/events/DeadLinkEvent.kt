package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.core.commands.CmdDeleteDeadFilterListEntry
import io.github.manamiproject.manami.core.commands.CmdDeleteDeadWatchListEntry
import io.github.manamiproject.manami.core.commands.ReversibleCommand
import io.github.manamiproject.manami.dto.entities.FilterListEntry
import io.github.manamiproject.manami.dto.entities.MinimalEntry
import io.github.manamiproject.manami.dto.entities.WatchListEntry
import io.github.manamiproject.manami.persistence.Persistence


internal class DeadLinkEvent(
        private val entry: MinimalEntry,
        private val persistence: Persistence
) : AbstractEvent(), ReversibleCommandEvent {

    private var command: ReversibleCommand = when (entry) {
        is WatchListEntry -> CmdDeleteDeadWatchListEntry(entry, persistence)
        is FilterListEntry -> CmdDeleteDeadFilterListEntry(entry, persistence)
        //noop command
        else -> object: ReversibleCommand {
            override fun execute() = false
            override fun undo() {}
            override fun redo() {}
            override fun isLastSaved() = false
            override fun setLastSaved(value: Boolean) {}
        }
    }


    override fun getCommand(): ReversibleCommand {
        return command
    }
}
