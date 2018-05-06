package io.github.manamiproject.manami.entities.entities

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.net.URL

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


    given("a valid MAL url with query parameter") {
        val url = "https://myanimelist.net/anime.php?id=1535"

        on("creating an InfoLink using this URL") {
            val result = InfoLink(url)

            it("must be normalized") {
                assertThat(result.toString()).isEqualTo("https://myanimelist.net/anime/1535")
            }
        }
    }


    given("a valid MAL url with search query") {
        val url = "https://myanimelist.net/anime/1535/Death_Note?q=death%20note"

        on("creating an InfoLink using this URL") {
            val result = InfoLink(url)

            it("must be normalized") {
                assertThat(result.toString()).isEqualTo("https://myanimelist.net/anime/1535")
            }
        }
    }


    given("a valid default MAL url with SEO title in it") {
        val url = "https://myanimelist.net/anime/1535/Death_Note"

        on("creating an InfoLink using this URL") {
            val result = InfoLink(url)

            it("must be normalized") {
                assertThat(result.toString()).isEqualTo("https://myanimelist.net/anime/1535")
            }
        }
    }


    given("a valid MAL url which has already been normalized") {
        val url = "https://myanimelist.net/anime/1535"

        on("creating an InfoLink using this URL") {
            val result = InfoLink(url)

            it("must be normalized") {
                assertThat(result.toString()).isEqualTo("https://myanimelist.net/anime/1535")
            }
        }
    }


    given("a valid MAL url which has already been normalized, but wuth http instead of https") {
        val url = "http://myanimelist.net/anime/1535"

        on("creating an InfoLink using this URL") {
            val result = InfoLink(url)

            it("must be normalized") {
                assertThat(result.toString()).isEqualTo("https://myanimelist.net/anime/1535")
            }
        }
    }


    given("not a valid MAL InfoLink URL") {
        val url = "https://myanimelist.net/news?_location=mal_h_m"

        on("creating an InfoLink using this URL") {
            val result = InfoLink(url)

            it("must be returned as is") {
                assertThat(result.toString()).isEqualTo(url)
            }
        }
    }


    given("a valid default MAL url with SEO title in it and blanks at the beginning and at the end") {
        val url = "https://myanimelist.net/anime/1535/Death_Note"

        on("creating an InfoLink using this URL") {
            val result = InfoLink(url)

            it("must be normalized") {
                assertThat(result.toString()).isEqualTo("https://myanimelist.net/anime/1535")
            }
        }
    }
})