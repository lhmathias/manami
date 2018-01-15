package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.core.Manami
import io.github.manamiproject.manami.core.commands.CmdDeleteDeadFilterListEntry
import io.github.manamiproject.manami.core.commands.CmdDeleteDeadWatchListEntry
import io.github.manamiproject.manami.core.commands.ReversibleCommand
import io.github.manamiproject.manami.dto.entities.FilterListEntry
import io.github.manamiproject.manami.dto.entities.MinimalEntry
import io.github.manamiproject.manami.dto.entities.WatchListEntry


class DeadLinkEvent(
        private val entry: MinimalEntry,
        private val app: Manami
) : AbstractEvent(), ReversibleCommandEvent {

    private var command: ReversibleCommand? = null

    init {
        command = when (entry) {
            is WatchListEntry -> CmdDeleteDeadWatchListEntry(entry, app)
            is FilterListEntry -> CmdDeleteDeadFilterListEntry(entry, app)
            else -> ReversibleCommand {
                private var isLastSaved = false
                override fun undo() {}
                override fun redo() {}
                override fun isLastSaved() = isLastSaved
                override fun setLastSaved(value: Boolean) {
                    isLastSaved = value
                }
            }
        }
    }


    override fun getCommand(): ReversibleCommand {
        return command
    }
}
