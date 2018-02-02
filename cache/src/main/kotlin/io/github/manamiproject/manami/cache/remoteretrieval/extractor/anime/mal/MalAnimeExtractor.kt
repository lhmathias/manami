package io.github.manamiproject.manami.cache.remoteretrieval.extractor.anime.mal

import io.github.manamiproject.manami.cache.remoteretrieval.extractor.anime.AnimeExtractor
import io.github.manamiproject.manami.dto.AnimeType
import io.github.manamiproject.manami.dto.entities.*
import org.cyberneko.html.parsers.DOMParser
import org.xml.sax.InputSource
import java.io.StringReader
import org.w3c.dom.NodeList
import java.net.URL
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory


class MalAnimeExtractor : AnimeExtractor {

    private val xPath = XPathFactory.newInstance().newXPath()
    private val parser = DOMParser().apply {
        setFeature("http://xml.org/sax/features/namespaces", false)
    }

    override fun extractAnime(html: String): Anime? {
        parser.parse(InputSource(StringReader(html)))

        val anime = Anime(
                extractTitle(),
                extractInfoLink(),
                extractEpisodes(),
                extractType()
        )

        extractThumbnail()?.let {
            anime.thumbnail = it
        }

        extractPicture()?.let {
            anime.picture = it
        }

        return if(anime.isValid()) anime else null
    }


    override fun isResponsible(infoLink: InfoLink) = infoLink.toString().startsWith(NORMALIZED_ANIME_DOMAIN.MAL.value)


    private fun extractTitle(): String {
        val doc = parser.document
        val nodes: NodeList = xPath.evaluate("//H1/SPAN", doc.documentElement, XPathConstants.NODESET) as NodeList

        return nodes.item(0).textContent.trim()
    }


    private fun extractInfoLink(): InfoLink {
        val doc = parser.document
        val nodes: NodeList = xPath.evaluate("//META[@property='og:url']/@content", doc.documentElement, XPathConstants.NODESET) as NodeList

        if(nodes.length == 1) {
            return InfoLink(nodes.item(0).textContent.trim())
        }

        return InfoLink("")
    }


    private fun extractEpisodes(): Int {
        val doc = parser.document
        val nodes: NodeList = xPath.evaluate("//SPAN[@class='dark_text' and text()='Episodes:']", doc.documentElement, XPathConstants.NODESET) as NodeList

        nodes.item(0)?.parentNode?.textContent?.let {
            val splitParts = it.split(":")

            if(splitParts.size == 2) {
                return splitParts[1].trim().toInt()
            }
        }

        return 0
    }


    private fun extractType(): AnimeType {
        val doc = parser.document
        val nodes: NodeList = xPath.evaluate("//A[contains(@href, '?type=') and not(contains(@href, 'airing'))]", doc.documentElement, XPathConstants.NODESET) as NodeList

        if(nodes.length == 1) {
            return AnimeType.valueOf(nodes.item(0).textContent)
        }

        return AnimeType.TV
    }


    private fun extractPicture(): URL? {
        val doc = parser.document
        val nodes: NodeList = xPath.evaluate("//META[@property='og:image']/@content", doc.documentElement, XPathConstants.NODESET) as NodeList

        if(nodes.length == 1) {
            val pictureUrl: String = nodes.item(0).textContent.trim()

            if("https://myanimelist.cdn-dena.com/img/sp/icon/apple-touch-icon-256.png".equals(pictureUrl, ignoreCase = true)) {
                return null
            }

            return URL(pictureUrl)
        }

        return null
    }


    private fun extractThumbnail(): URL? {
        extractPicture()?.let { pictureUrl ->
            if(MinimalEntry.NO_IMG != pictureUrl) {
                return URL(pictureUrl.toString().replace(".jpg", "t.jpg"))
            }
        }

        return null
    }
}
