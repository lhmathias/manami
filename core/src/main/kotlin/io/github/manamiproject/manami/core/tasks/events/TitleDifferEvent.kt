package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.core.commands.CmdChangeTitle
import io.github.manamiproject.manami.core.commands.ReversibleCommand
import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.persistence.Persistence

class TitleDifferEvent(
        private val animeEntry: Anime,
        private val newValue: String,
        private val persistence: Persistence
) : AbstractEvent(animeEntry), ReversibleCommandEvent {

    private val command: ReversibleCommand = CmdChangeTitle(animeEntry, newValue, persistence)


    override fun getCommand(): ReversibleCommand {
        return command
    }
}
