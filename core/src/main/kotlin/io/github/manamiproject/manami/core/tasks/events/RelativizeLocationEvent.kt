package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.core.commands.CmdChangeLocation
import io.github.manamiproject.manami.core.commands.ReversibleCommand
import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.persistence.Persistence

class RelativizeLocationEvent(
        private val animeEntry: Anime,
        private val newValue: String,
        private val persistence: Persistence
) : AbstractEvent(animeEntry), ReversibleCommandEvent {

    private val command: ReversibleCommand = CmdChangeLocation(animeEntry, newValue, persistence)


    override fun getCommand(): ReversibleCommand {
        return command
    }
}
