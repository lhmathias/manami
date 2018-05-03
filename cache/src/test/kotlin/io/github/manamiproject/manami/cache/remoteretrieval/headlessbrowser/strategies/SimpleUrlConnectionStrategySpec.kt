package io.github.manamiproject.manami.cache.remoteretrieval.headlessbrowser.strategies

import io.github.manamiproject.manami.entities.InfoLink
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith


@RunWith(JUnitPlatform::class)
class SimpleUrlConnectionStrategySpec : Spek({

    val simpleUrlConnectionStrategySpec = SimpleUrlConnectionStrategy()

    given("an invalid infolink") {
        val infoLink = InfoLink("abcdefgh")

        on("attempting to downloading that site") {
            val result: String = simpleUrlConnectionStrategySpec.downloadSite(infoLink)

            it("must return an empty string, because the link was not valid and therefore no outgoing request has been made") {
                assertThat(result).isEmpty()
            }
        }
    }
})