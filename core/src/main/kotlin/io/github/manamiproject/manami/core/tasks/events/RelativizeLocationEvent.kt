package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.core.Manami
import io.github.manamiproject.manami.core.commands.CmdChangeLocation
import io.github.manamiproject.manami.dto.entities.Anime

class RelativizeLocationEvent(
        private val animeEntry: Anime,
        private val newValue: String,
        private val app: Manami
) : AbstractEvent(animeEntry), ReversibleCommandEvent {

    private val command: CmdChangeLocation = CmdChangeLocation(animeEntry, newValue, app)


    override fun getCommand(): CmdChangeLocation {
        return command
    }
}
