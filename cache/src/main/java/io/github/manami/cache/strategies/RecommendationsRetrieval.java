package io.github.manami.cache.strategies;

import io.github.manami.dto.entities.InfoLink;
import io.github.manami.dto.entities.RecommendationList;

public interface RecommendationsRetrieval {

    /**
     * Fetches all recommendations for a specific anime.
     *
     * @param infoLink
     * @return A {@link Map} of all recommendations or an empty {@link Map}, but
     *         never null.
     *         The KEY represents the URL of the recommended anime.
     *         The VALUE contains the amount of recommendations made.
     */
    RecommendationList fetchRecommendations(InfoLink infoLink);
}
