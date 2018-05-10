package io.github.manamiproject.manami.entities

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


    fun isNotEmpty(): Boolean = !recommendations.isEmpty()


    override fun isEmpty(): Boolean = recommendations.isEmpty()


    fun contains(infoLink: InfoLink): Boolean = recommendations.containsKey(infoLink)


    fun get(infoLink: InfoLink): Recommendation? = recommendations[infoLink]


    override fun iterator(): Iterator<Recommendation> {
        return recommendations.values.toList().iterator()
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
