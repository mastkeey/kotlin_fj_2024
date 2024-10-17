package model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class NewsResponse(
    @JsonNames("results")
    val results: List<News>? = null
)