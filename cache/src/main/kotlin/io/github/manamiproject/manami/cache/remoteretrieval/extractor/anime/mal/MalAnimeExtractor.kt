package io.github.manamiproject.manami.cache.remoteretrieval.extractor.anime.mal

import io.github.manamiproject.manami.cache.remoteretrieval.extractor.anime.AnimeExtractor
import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.dto.entities.DOMAINS
import io.github.manamiproject.manami.dto.entities.InfoLink

class MalAnimeExtractor : AnimeExtractor {

    override fun extractAnime(html: String): Anime? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isResponsible(infoLink: InfoLink) = infoLink.toString().contains(DOMAINS.MAL.value)
}