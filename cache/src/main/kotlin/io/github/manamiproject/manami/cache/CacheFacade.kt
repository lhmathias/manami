package io.github.manamiproject.manami.cache

import io.github.manamiproject.manami.cache.Cache
import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.RecommendationList
import java.util.*

object CacheFacade : Cache {

    override fun fetchAnime(infoLink: InfoLink?): Optional<Anime> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fetchRelatedAnime(infoLink: InfoLink?): MutableSet<InfoLink> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fetchRecommendations(infoLink: InfoLink?): RecommendationList {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}