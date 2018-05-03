package io.github.manamiproject.manami.cache.remoteretrieval.headlessbrowser

import io.github.manamiproject.manami.cache.remoteretrieval.headlessbrowser.strategies.SimpleUrlConnectionStrategy
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.NORMALIZED_ANIME_DOMAIN
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class HeadlessBrowserStrategiesSpec : Spek({

    given("a mal infolink") {
        val infoLink = InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535")

        on("getting a headless browser for this infolink") {
            val result: HeadlessBrowser? = HeadlessBrowserStrategies.getHeadlessBrowserFor(infoLink)

            it("must not return null") {
                assertThat(result).isNotNull()
            }

            it("must not return null") {
                assertThat(result).isInstanceOfAny(SimpleUrlConnectionStrategy::class.java)
            }
        }
    }

    given("a anidb infolink") {
        val infoLink = InfoLink("${NORMALIZED_ANIME_DOMAIN.ANIDB.value}13248")

        on("getting a headless browser for this infolink") {
            val result: HeadlessBrowser? = HeadlessBrowserStrategies.getHeadlessBrowserFor(infoLink)

            it("must not return null") {
                assertThat(result).isNotNull()
            }

            it("must not return null") {
                assertThat(result).isInstanceOfAny(SimpleUrlConnectionStrategy::class.java)
            }
        }
    }

    given("an unknown infolink") {
        val infoLink = InfoLink("https://www.animenewsnetwork.com/encyclopedia/anime.php?id=6592")

        on("getting a headless browser for this infolink") {
            val result: HeadlessBrowser? = HeadlessBrowserStrategies.getHeadlessBrowserFor(infoLink)

            it("must not return null") {
                assertThat(result).isNull()
            }
        }
    }
})