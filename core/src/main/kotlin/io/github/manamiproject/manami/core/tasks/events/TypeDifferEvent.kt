package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.core.commands.CmdChangeType
import io.github.manamiproject.manami.core.commands.ReversibleCommand
import io.github.manamiproject.manami.dto.AnimeType
import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.persistence.Persistence

class TypeDifferEvent(
        private val animeEntry: Anime,
        private val newValue: AnimeType,
        private val persistence: Persistence
) : AbstractEvent(animeEntry), ReversibleCommandEvent {

    private val command: ReversibleCommand = CmdChangeType(animeEntry, newValue, persistence)


    override fun getCommand(): ReversibleCommand {
        return command
    }
}
