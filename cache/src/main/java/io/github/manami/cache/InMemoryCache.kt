package io.github.manami.cache

import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.InfoLink
import io.github.manami.dto.entities.RecommendationList
import java.util.*

object InMemoryCache: Cache {

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