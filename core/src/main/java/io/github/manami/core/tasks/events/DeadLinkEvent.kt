package io.github.manami.core.tasks.events

import io.github.manami.core.Manami
import io.github.manami.core.commands.CmdDeleteDeadFilterListEntry
import io.github.manami.core.commands.CmdDeleteDeadWatchListEntry
import io.github.manami.core.commands.ReversibleCommand
import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.dto.entities.MinimalEntry
import io.github.manami.dto.entities.WatchListEntry


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
