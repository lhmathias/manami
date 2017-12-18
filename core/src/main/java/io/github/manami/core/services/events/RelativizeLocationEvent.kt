package io.github.manami.core.services.events

import io.github.manami.core.Manami
import io.github.manami.core.commands.CmdChangeLocation
import io.github.manami.dto.entities.Anime

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
