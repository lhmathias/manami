package io.github.manamiproject.manami.core.events

import io.github.manamiproject.manami.entities.InfoLink

data class RecommendationListEvent(val recommendationList: List<InfoLink>)