package io.github.manamiproject.manami.core.events.relatedanime

import io.github.manamiproject.manami.entities.InfoLink

data class RelatedAnimeIdentifiedEvent(val relatedAnime: Set<InfoLink>)