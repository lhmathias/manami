package io.github.manamiproject.manami.cache.remoteretrieval.extractor.relatedanime.mal

import io.github.manamiproject.manami.cache.remoteretrieval.extractor.relatedanime.RelatedAnimeExtractor
import io.github.manamiproject.manami.dto.entities.DOMAINS
import io.github.manamiproject.manami.dto.entities.InfoLink
import org.cyberneko.html.parsers.DOMParser
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

internal class MalRelatedAnimeExtractor : RelatedAnimeExtractor {

    private val xPath = XPathFactory.newInstance().newXPath()
    private val parser = DOMParser().apply {
        setFeature("http://xml.org/sax/features/namespaces", false)
    }

    override fun extractRelatedAnime(html: String): MutableSet<InfoLink> {
        val relatedAnime: MutableSet<InfoLink> = mutableSetOf()

        parser.parse(InputSource(StringReader(html)))

        val doc = parser.document
        val relatedAnimeLinks: NodeList = xPath.evaluate("//TABLE[@class='anime_detail_related_anime']//TR[./TD/text()!='Adaptation:']//A/@href", doc.documentElement, XPathConstants.NODESET) as NodeList

        if(relatedAnimeLinks.length == 0) {
            return relatedAnime
        }

        (0 until relatedAnimeLinks.length)
                .map { relatedAnimeLinks.item(it) }
                .forEach {
                    Regex("\\/\\d+\\/").find(it.textContent)?.let { matchResult ->
                        relatedAnime.add(InfoLink("https://${DOMAINS.MAL.value}/${matchResult.value}"))
                    }
                }

        return relatedAnime
    }

    override fun isResponsible(infoLink: InfoLink) = infoLink.toString().contains(DOMAINS.MAL.value)
}