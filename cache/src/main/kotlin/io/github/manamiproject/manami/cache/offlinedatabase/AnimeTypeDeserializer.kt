package io.github.manamiproject.manami.cache.offlinedatabase

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import io.github.manamiproject.manami.dto.AnimeType
import java.lang.reflect.Type

class AnimeTypeDeserializer : JsonDeserializer<AnimeType> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): AnimeType? {
        AnimeType.values().forEach {
            val jsonValue = json?.asString ?: ""

            if (it.value.equals(jsonValue, ignoreCase = true)) {
                return it
            }
        }

        return null
    }
}