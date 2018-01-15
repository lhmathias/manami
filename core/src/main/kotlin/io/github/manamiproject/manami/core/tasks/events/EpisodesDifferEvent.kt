package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.core.Manami
import io.github.manamiproject.manami.core.commands.CmdChangeEpisodes
import io.github.manamiproject.manami.core.commands.ReversibleCommand
import io.github.manamiproject.manami.dto.entities.Anime

class EpisodesDifferEvent(
        private val animeEntry: Anime,
        private val newValue: Int,
        private val app: Manami
) : AbstractEvent(animeEntry), ReversibleCommandEvent {

    private val cmd: CmdChangeEpisodes = CmdChangeEpisodes(animeEntry, newValue, app)


    override fun getCommand(): ReversibleCommand {
        return cmd
    }
}
