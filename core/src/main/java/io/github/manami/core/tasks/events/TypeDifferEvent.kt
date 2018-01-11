package io.github.manami.core.tasks.events

import io.github.manami.core.ManamiImpl
import io.github.manami.core.commands.CmdChangeType
import io.github.manami.dto.AnimeType
import io.github.manami.dto.entities.Anime

class TypeDifferEvent(
        private var animeEntry: Anime,
        private var newValue: AnimeType,
        private var app: ManamiImpl
) : AbstractEvent(animeEntry), ReversibleCommandEvent {

    private val command: CmdChangeType = CmdChangeType(animeEntry, newValue, app)


    override fun getCommand(): CmdChangeType {
        return command
    }
}
