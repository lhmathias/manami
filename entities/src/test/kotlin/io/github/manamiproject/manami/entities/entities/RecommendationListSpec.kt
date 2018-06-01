package io.github.manamiproject.manami.entities.entities

import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.NormalizedAnimeBaseUrls
import io.github.manamiproject.manami.entities.Recommendation
import io.github.manamiproject.manami.entities.RecommendationList
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class RecommendationListSpec : Spek({

    given("a newly created RecommendationList") {
        var recommendationList = RecommendationList()

        beforeEachTest {
            recommendationList = RecommendationList()
        }

        on("checking if the list is empty") {
            val isEmpty = recommendationList.isEmpty()
            val isNotEmpty = recommendationList.isNotEmpty()

            it("must return true for isEmpty") {
                assertThat(isEmpty).isTrue()
            }

            it("must return false for isNotEmpty") {
                assertThat(isNotEmpty).isFalse()
            }

            it("must have a size of 0") {
                assertThat(recommendationList).hasSize(0)
            }
        }

        on("adding a new recommendation") {
            val infoLink = InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535")
            val recommendation = Recommendation(infoLink, 103)

            recommendationList.addRecommendation(recommendation)

            it("must return false for isEmpty") {
                assertThat(recommendationList.isEmpty()).isFalse()
            }

            it("must return true for isNotEmpty") {
                assertThat(recommendationList.isNotEmpty()).isTrue()
            }

            it("must contain the inserted infoLink") {
                assertThat(recommendationList.contains(infoLink)).isTrue()
            }

            it("must be returned upon get() call") {
                assertThat(recommendationList.get(infoLink)).isEqualTo(recommendation)
            }
        }

        on("checking if the list contains a Recommendation") {
            val result = recommendationList.contains(
                    Recommendation(
                            InfoLink("${NormalizedAnimeBaseUrls.MAL.value}1535"),
                            231
                    )
            )

            it("must return false") {
                assertThat(result).isFalse()
            }
        }

        on("retrieving iterator") {
            val result = recommendationList.iterator()

            it("must not have any new items") {
                assertThat(result.hasNext()).isFalse()
            }
        }
    }

    given("a recommendation list containing one recommendation") {
        val infoLinkUrl = "${NormalizedAnimeBaseUrls.MAL.value}1535"
        val initialValue = 5
        var recommendationList = RecommendationList()


        beforeEachTest {
            recommendationList = RecommendationList().apply {
                addRecommendation(
                        Recommendation(
                                InfoLink(infoLinkUrl),
                                initialValue
                        )
                )
            }
        }


        on("adding a new instance of the same infoLinkUrl with a different amount") {
            val newValue = 15
            recommendationList.addRecommendation(
                    Recommendation(
                            InfoLink(infoLinkUrl),
                            newValue
                    )
            )

            it("must contain only one entry") {
                assertThat(recommendationList).hasSize(1)
            }

            it("must contain the infoLink with the sum of both values as amount") {
                assertThat(recommendationList.get(InfoLink(infoLinkUrl))?.amount).isEqualTo(initialValue + newValue)
            }
        }

        on("checking if the list contains the Recommendation (different instance)") {
            val result = recommendationList.contains(
                    Recommendation(
                            InfoLink(infoLinkUrl),
                            initialValue
                    )
            )

            it("must return true") {
                assertThat(result).isTrue()
            }
        }

        on("checking if the list contains the recommendations (different instance) using containsAll") {
            val result = recommendationList.containsAll(
                    listOf(
                            Recommendation(
                                    InfoLink(infoLinkUrl),
                                    initialValue
                            )
                    )
            )

            it("must return true") {
                assertThat(result).isTrue()
            }
        }

        on("checking if the list containsAll of recommendations which reside in the list and some which don't") {
            val result = recommendationList.containsAll(
                    listOf(
                            Recommendation(
                                    InfoLink(infoLinkUrl),
                                    initialValue
                            ),
                            Recommendation(
                                    InfoLink("${NormalizedAnimeBaseUrls.MAL.value}35180"),
                                    5
                            )
                    )
            )

            it("must return false") {
                assertThat(result).isFalse()
            }
        }

        on("retrieving iterator") {
            val result = recommendationList.iterator()

            it("must not have new items") {
                assertThat(result.hasNext()).isTrue()
            }
        }
    }
})