package io.github.manamiproject.manami.common

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object ListExtensionFunctionsSpec : Spek({

    given("a mutable list") {
        val list : List<Int> = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

        on("applying randomizer") {
            val result : List<Int> = list.randomizeOrder()

            it("must not have the same order sa before") {
                assertThat(result).isNotEqualTo(list)
            }
        }
    }
})