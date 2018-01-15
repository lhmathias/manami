package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.core.Manami
import io.github.manamiproject.manami.core.commands.CmdChangeType
import io.github.manamiproject.manami.dto.AnimeType
import io.github.manamiproject.manami.dto.entities.Anime

class TypeDifferEvent(
        private var animeEntry: Anime,
        private var newValue: AnimeType,
        private var app: Manami
) : AbstractEvent(animeEntry), ReversibleCommandEvent {

    private val command: CmdChangeType = CmdChangeType(animeEntry, newValue, app)


    override fun getCommand(): CmdChangeType {
        return command
    }
}
