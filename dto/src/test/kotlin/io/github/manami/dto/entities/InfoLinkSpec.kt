package io.github.manami.dto.entities

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.net.URL

@RunWith(JUnitPlatform::class)
class InfoLinkSpec : Spek({

    given("a valid InfoLink") {
        val url = "https://myanimelist.net/anime/1535"
        val infoLink = InfoLink(url)

        on("checking if it is present") {
            val result = infoLink.isPresent()

            it("must return true") {
                assertThat(result).isTrue()
            }
        }


        on("checking if it is valid") {
            val result = infoLink.isValid()

            it("must return true") {
                assertThat(result).isTrue()
            }
        }


        on("checking equality on the same instance") {
            val result = infoLink == infoLink

            it("must return true") {
                assertThat(result).isTrue()
            }
        }


        on("checking equality on the same url") {
            val result = infoLink == InfoLink(url)

            it("must return true") {
                assertThat(result).isTrue()
            }
        }


        on("using toString") {
            val result = infoLink.toString()

            it("must be equal to the URL") {
                assertThat(result).isEqualTo(url)
            }
        }


        on("getting URL") {
            val result = infoLink.url

            it("must be equal to the corresponding URL object") {
                assertThat(result).isEqualTo(URL(url))
            }
        }
    }


    given("a blank InfoLink") {
        val infoLink = InfoLink("   ")

        on("checking if it is present") {
            val result = infoLink.isPresent()

            it("must return false") {
                assertThat(result).isFalse()
            }
        }


        on("checking if it is valid") {
            val result = infoLink.isValid()

            it("must return false") {
                assertThat(result).isFalse()
            }
        }
    }


    given("two distinct and valid InfoLinks") {
        val infoLinkUrlA = InfoLink("https://myanimelist.net/anime/1535")
        val infoLinkUrlB = InfoLink("https://myanimelist.net/anime/15")

        on("checking for equality") {
            val result = infoLinkUrlA == infoLinkUrlB

            it("must return false") {
                assertThat(result).isFalse()
            }
        }
    }


    given("a valid InfoLink and an invalid link") {
        val infoLinkUrlA = InfoLink("https://myanimelist.net/anime/1535")
        val infoLinkUrlB = InfoLink("    ")

        on("checking for equality") {
            val result = infoLinkUrlA == infoLinkUrlB

            it("must return false") {
                assertThat(result).isFalse()
            }
        }
    }
})