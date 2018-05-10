package io.github.manamiproject.manami.cache.remoteretrieval.headlessbrowser.strategies

import io.github.manamiproject.manami.cache.remoteretrieval.headlessbrowser.HeadlessBrowser
import io.github.manamiproject.manami.entities.InfoLink
import khttp.get
import java.util.*

private const val ACCEPT = "Accept"
private const val USER_AGENT = "User-Agent"

internal class SimpleUrlConnectionStrategy : HeadlessBrowser {

    override fun downloadSite(infoLink: InfoLink): String {
        if(!infoLink.isValid()) {
            return ""
        }

        val header: Map<String, String> = createHeader(infoLink)
        
        return get(infoLink.toString(), header).text
    }

    
    private fun createHeader(infoLink: InfoLink): Map<String, String> {
        val host = infoLink.url?.host ?: ""

        val languages: List<String> = listOf(
            "de,en-US;q=0.7,en;q=0.3",
            "en-US,en;q=0.5",
            "en-US,en;q=0.8"
        )

        val headers: MutableMap<String, String> = mutableMapOf(
            Pair("Host", host),
            Pair("Connection","keep-alive"),
            Pair("Upgrade-Insecure-Requests", "1"),
            Pair("Accept-Language", languages[Random().nextInt(languages.size)])
        )

        val browser = BROWSER.getRandom()

        headers.putAll(when(browser) {
            BROWSER.FIREFOX -> createFireFoxHeader()
            BROWSER.SAFARI -> createSafariHeader()
            BROWSER.CHROME -> createChromeHeader()
        })

        return headers
    }


    private fun createFireFoxHeader(): Map<String, String> {
        val userAgents: List<String> = listOf(
                "Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:56.0) Gecko/20100101 Firefox/58.0",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:56.0) Gecko/20100101 Firefox/58.0",
                "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0"
        )

        return mapOf(
                Pair(ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
                Pair("DNT", "1"),
                Pair("Upgrade-Insecure-Requests", "1"),
                Pair(USER_AGENT, userAgents[Random().nextInt(userAgents.size)])
        )
    }


    private fun createSafariHeader(): Map<String, String> {
        return mapOf(
                Pair(ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
                Pair(USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/604.5.6 (KHTML, like Gecko) Version/11.0.3 Safari/604.5.6")
        )
    }


    private fun createChromeHeader(): Map<String, String> {
        val acceptHeaders: List<String> = listOf(
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
        )

        val userAgents: List<String> = listOf(
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2850.0 Iron Safari/537.36"
        )

        return mapOf(
                Pair(ACCEPT, acceptHeaders[Random().nextInt(acceptHeaders.size)]),
                Pair(USER_AGENT, userAgents[Random().nextInt(userAgents.size)])
        )
    }
}


private enum class BROWSER {
    FIREFOX,
    SAFARI,
    CHROME;

    companion object {
        fun getRandom(): BROWSER {
            return values()[Random().nextInt(values().size)]
        }
    }
}