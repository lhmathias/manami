package io.github.manamiproject.manami.cache.remoteretrieval.extractor.relatedanime.mal

import io.github.manamiproject.manami.cache.remoteretrieval.extractor.relatedanime.RelatedAnimeExtractor
import io.github.manamiproject.manami.dto.entities.DOMAINS
import io.github.manamiproject.manami.dto.entities.InfoLink

internal class MalRelatedAnimeExtractor : RelatedAnimeExtractor {
    override fun extractRelatedAnime(html: String): MutableSet<InfoLink> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isResponsible(infoLink: InfoLink) = infoLink.toString().contains(DOMAINS.MAL.value)
}