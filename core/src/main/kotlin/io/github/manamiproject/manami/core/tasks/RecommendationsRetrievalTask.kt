package io.github.manamiproject.manami.core.tasks

import io.github.manamiproject.manami.cache.Cache
import io.github.manamiproject.manami.common.EventBus
import io.github.manamiproject.manami.common.LoggerDelegate
import io.github.manamiproject.manami.common.randomizeOrder
import io.github.manamiproject.manami.core.events.RecommendationListEvent
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.Recommendation
import io.github.manamiproject.manami.persistence.Persistence
import org.slf4j.Logger
import java.util.*


/** Max percentage rate which the shown recommendations can make out of all entries. */
private const val MAX_PERCENTAGE: Int = 80

/** Max number of entries of which a recommendations list can consist. */
private const val MAX_NUMBER_OF_ENTRIES: Int = 100

/**
 * Extracts and counts recommendations for a list of anime.
 * Always start {@link BackgroundTask}s using the {@link ServiceRepository}!
 */
internal class RecommendationsRetrievalTask(
         private val animeList: List<Anime>,
         private val cache: Cache,
        private val persistence: Persistence
 ) : AbstractTask() {

    private val log: Logger by LoggerDelegate()
    private val recommendations: MutableMap<InfoLink, Int> = mutableMapOf()

    override fun execute() {
        animeList.randomizeOrder()
                .filter { it.isValid() }
                .map { it.infoLink }
                .map(cache::fetchRecommendations)
                .forEach{ it.forEach(this::addRecom) }

        EventBus.publish(RecommendationListEvent(finalizeRecommendationSelection()))
    }

    private fun addRecom(recommendation: Recommendation) {
        val infoLink = recommendation.infoLink
        val notInAnimeList = !persistence.animeEntryExists(infoLink)
        val notInFilterList = !persistence.filterListEntryExists(infoLink)
        val notInWatchList = !persistence.watchListEntryExists(infoLink)

        if (notInAnimeList && notInFilterList && notInWatchList) {
            if (recommendations.containsKey(infoLink)) {
                recommendations[infoLink]?.let {
                    recommendations[infoLink] = it.plus(recommendation.amount)
                }
            } else {
                recommendations[infoLink] = recommendation.amount
            }
        }
    }

    private fun finalizeRecommendationSelection(): List<InfoLink> {
        val sumAll = recommendations.values.sum()
        val allRecommendationsSorted = sortMapByValue()
        val userRecommendationsResult: MutableList<InfoLink> = mutableListOf()
        var percentage = 0
        var curSum = 0

        allRecommendationsSorted.entries.forEach {
            if (percentage < MAX_PERCENTAGE && userRecommendationsResult.size < MAX_NUMBER_OF_ENTRIES) {
                userRecommendationsResult.add(it.key)
                curSum += it.value
                percentage = (curSum * 100) / sumAll
            }
        }

        return userRecommendationsResult.toList()
    }

    private fun sortMapByValue(): Map<InfoLink, Int>  {
        val list: List<Map.Entry<InfoLink, Int>> = recommendations.entries.toMutableList()
        val sortedMap: MutableMap<InfoLink, Int> = mutableMapOf()

        list.sortedWith(Comparator { o1, o2 ->
            o1.value.compareTo(o2.value)
        })
            .associateByTo(sortedMap, {it.key}, {it.value})

        return sortedMap
    }
}