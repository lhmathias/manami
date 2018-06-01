package io.github.manamiproject.manami.entities

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object UrlValidatorTest : Spek({

    given("a valid url with http scheme") {
        val url = "http://github.com"

        on("checking if is valid") {
            val result = UrlValidator.isValid(url)

            it("returns true, because the url is valid") {
                assertThat(result).isTrue()
            }
        }

        on("checking if is not valid") {
            val result = UrlValidator.isNotValid(url)

            it("returns false, because the url is valid") {
                assertThat(result).isFalse()
            }
        }
    }

    given("a valid url with https scheme") {
        val url = "https://github.com"

        on("checking if is valid") {
            val result = UrlValidator.isValid(url)

            it("returns true, because the url is valid") {
                assertThat(result).isTrue()
            }
        }

        on("checking if is not valid") {
            val result = UrlValidator.isNotValid(url)

            it("returns false, because the url is valid") {
                assertThat(result).isFalse()
            }
        }
    }

    given("a string which is not a valid url") {
        val url = "value.more"

        on("checking if is valid") {
            val result = UrlValidator.isValid(url)

            it("returns false, because the string is not a valid url") {
                assertThat(result).isFalse()
            }
        }

        on("checking if is not valid") {
            val result = UrlValidator.isNotValid(url)

            it("returns true, because the string is not a valid url") {
                assertThat(result).isTrue()
            }
        }
    }

    given("a valid url with a non valid scheme") {
        val url = "ftp://127.0.0.1"

        on("checking if is valid") {
            val result = UrlValidator.isValid(url)

            it("returns false, because the scheme is not allowed") {
                assertThat(result).isFalse()
            }
        }

        on("checking if is not valid") {
            val result = UrlValidator.isNotValid(url)

            it("returns true, because the scheme is not allowed") {
                assertThat(result).isTrue()
            }
        }
    }
})