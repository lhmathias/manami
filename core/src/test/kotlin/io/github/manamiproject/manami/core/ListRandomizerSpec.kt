package io.github.manamiproject.manami.core

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class ListRandomizerSpec : Spek({

    given("a mutable list") {
        val list : List<Int> = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

        on("applying randomizer") {
            val result : List<Int> = ListRandomizer.randomizeOrder(list)

            it("must not have the same order sa before") {
                assertThat(result).isNotEqualTo(list)
            }
        }
    }
})