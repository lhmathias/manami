package io.github.manamiproject.manami.entities

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class AnimeTypeSpec : Spek({

    given("anime type string 'Tv'") {
        val typeTv = "Tv"

        on("finding AnimeType for'Tv'") {
            val result = AnimeType.findByName(typeTv)

            it("must return the corresponding AnimeType") {
                assertThat(result).isEqualTo(AnimeType.TV)
            }
        }

        on("finding AnimeType for uppercase string 'TV'") {
            val result = AnimeType.findByName(typeTv.toUpperCase())

            it("must return the corresponding AnimeType") {
                assertThat(result).isEqualTo(AnimeType.TV)
            }
        }

        on("finding AnimeType for lowercase string 'tv'") {
            val result = AnimeType.findByName(typeTv.toLowerCase())

            it("must return the corresponding AnimeType") {
                assertThat(result).isEqualTo(AnimeType.TV)
            }
        }
    }


    given("anime type string 'Movie'") {
        val typeMovie = "Movie"

        on("finding AnimeType for'Movie'") {
            val result = AnimeType.findByName(typeMovie)

            it("must return the corresponding AnimeType") {
                assertThat(result).isEqualTo(AnimeType.MOVIE)
            }
        }

        on("finding AnimeType for uppercase string 'MOVIE'") {
            val result = AnimeType.findByName(typeMovie.toUpperCase())

            it("must return the corresponding AnimeType") {
                assertThat(result).isEqualTo(AnimeType.MOVIE)
            }
        }

        on("finding AnimeType for lowercase string 'movie'") {
            val result = AnimeType.findByName(typeMovie.toLowerCase())

            it("must return the corresponding AnimeType") {
                assertThat(result).isEqualTo(AnimeType.MOVIE)
            }
        }
    }

    given("anime type string 'Ova'") {
        val typeOva = "Ova"

        on("finding AnimeType for'Ova'") {
            val result = AnimeType.findByName(typeOva)

            it("must return the corresponding AnimeType") {
                assertThat(result).isEqualTo(AnimeType.OVA)
            }
        }

        on("finding AnimeType for uppercase string 'OVA'") {
            val result = AnimeType.findByName(typeOva.toUpperCase())

            it("must return the corresponding AnimeType") {
                assertThat(result).isEqualTo(AnimeType.OVA)
            }
        }

        on("finding AnimeType for lowercase string 'ova'") {
            val result = AnimeType.findByName(typeOva.toLowerCase())

            it("must return the corresponding AnimeType") {
                assertThat(result).isEqualTo(AnimeType.OVA)
            }
        }
    }

    given("anime type string 'Ona'") {
        val typeOna = "Ona"

        on("finding AnimeType for'Ona'") {
            val result = AnimeType.findByName(typeOna)

            it("must return the corresponding AnimeType") {
                assertThat(result).isEqualTo(AnimeType.ONA)
            }
        }

        on("finding AnimeType for uppercase string 'ONA'") {
            val result = AnimeType.findByName(typeOna.toUpperCase())

            it("must return the corresponding AnimeType") {
                assertThat(result).isEqualTo(AnimeType.ONA)
            }
        }

        on("finding AnimeType for lowercase string 'ona'") {
            val result = AnimeType.findByName(typeOna.toLowerCase())

            it("must return the corresponding AnimeType") {
                assertThat(result).isEqualTo(AnimeType.ONA)
            }
        }
    }


    given("anime type string 'Special'") {
        val typeSpecial = "Special"

        on("finding AnimeType for'Special'") {
            val result = AnimeType.findByName(typeSpecial)

            it("must return the corresponding AnimeType") {
                assertThat(result).isEqualTo(AnimeType.SPECIAL)
            }
        }

        on("finding AnimeType for uppercase string 'SPECIAL'") {
            val result = AnimeType.findByName(typeSpecial.toUpperCase())

            it("must return the corresponding AnimeType") {
                assertThat(result).isEqualTo(AnimeType.SPECIAL)
            }
        }

        on("finding AnimeType for lowercase string 'special'") {
            val result = AnimeType.findByName(typeSpecial.toLowerCase())

            it("must return the corresponding AnimeType") {
                assertThat(result).isEqualTo(AnimeType.SPECIAL)
            }
        }
    }


    given("anime type string 'Music'") {
        val typeMusic = "Music"

        on("finding AnimeType for'Music'") {
            val result = AnimeType.findByName(typeMusic)

            it("must return the corresponding AnimeType") {
                assertThat(result).isEqualTo(AnimeType.MUSIC)
            }
        }

        on("finding AnimeType for uppercase string 'MUSIC'") {
            val result = AnimeType.findByName(typeMusic.toUpperCase())

            it("must return the corresponding AnimeType") {
                assertThat(result).isEqualTo(AnimeType.MUSIC)
            }
        }

        on("finding AnimeType for lowercase string 'music'") {
            val result = AnimeType.findByName(typeMusic.toLowerCase())

            it("must return the corresponding AnimeType") {
                assertThat(result).isEqualTo(AnimeType.MUSIC)
            }
        }
    }

    given("an invalid anime type") {
        val unknownType = "akjbfJKdsd"

        on("finding AnimeType by unknown string") {
            val result = AnimeType.findByName(unknownType)

            it("must return null") {
                assertThat(result).isNull()
            }
        }

        on("finding AnimeType by unknown uppercase string") {
            val result = AnimeType.findByName(unknownType.toUpperCase())

            it("must return the corresponding AnimeType") {
                assertThat(result).isNull()
            }
        }

        on("finding AnimeType by unknown lowercase string") {
            val result = AnimeType.findByName(unknownType.toLowerCase())

            it("must return the corresponding AnimeType") {
                assertThat(result).isNull()
            }
        }
    }
})
