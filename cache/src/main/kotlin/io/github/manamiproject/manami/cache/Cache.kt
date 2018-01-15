package io.github.manamiproject.manami.cache

import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.InfoLink
import io.github.manamiproject.manami.dto.entities.RecommendationList
import java.util.*


/**
 * The cache is supposed to save raw html files from which the information can be extracted at any time.
 */
interface Cache {

  fun fetchAnime(infoLink: InfoLink): Optional<Anime>


  fun fetchRelatedAnime(infoLink: InfoLink): Set<InfoLink>


  fun fetchRecommendations(infoLink: InfoLink): RecommendationList
}
