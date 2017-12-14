package io.github.manami.dto.entities

class RecommendationList : Collection<Recommendation> {

    private val recommendations: MutableMap<InfoLink, Recommendation> = mutableMapOf()

    fun addRecommendation(recommendation: Recommendation): Recommendation? {
        var value: Recommendation = recommendation

        if (contains(recommendation.infoLink)) {
            var amountInList = 0
            recommendations[recommendation.infoLink]?.let { amountInList = it.amount }

            val newAmount: Int = value.amount
            value = Recommendation(recommendation.infoLink, amountInList + newAmount)
        }

        return recommendations.put(recommendation.infoLink, value)
    }

    fun isNotEmpty() = !recommendations.isEmpty()


    fun contains(infoLink: InfoLink) = recommendations.containsKey(infoLink)


    fun get(infoLink: InfoLink) = recommendations[infoLink]


    override fun isEmpty() = recommendations.isEmpty()


    override fun iterator(): Iterator<Recommendation> {
        return recommendations.values.toMutableList().iterator()
    }


    override val size: Int
        get() = recommendations.size


    override fun contains(element: Recommendation): Boolean {
        return recommendations.values.contains(element)
    }


    override fun containsAll(elements: Collection<Recommendation>): Boolean {
        return recommendations.values.containsAll(elements)
    }
}
