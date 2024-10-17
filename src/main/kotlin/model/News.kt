package model

import serializer.LocalDateUnixSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import java.time.LocalDate
import kotlin.math.exp

@Serializable
data class News(
    @JsonNames("id")
    val id: Long? = null,
    @JsonNames("publication_date")
    @Serializable(with = LocalDateUnixSerializer::class)
    val publicationDate: LocalDate? = null,
    @JsonNames("title")
    val title: String? = null,
    @JsonNames("slug")
    val slug: String? = null,
    @JsonNames("place")
    var place: Place? = null,
    @JsonNames("description")
    val description: String? = null,
    @JsonNames("site_url")
    val siteUrl: String? = null,
    @JsonNames("favorites_count")
    val favoritesCount: Int? = null,
    @JsonNames("comments_count")
    val commentsCount: Int? = null
) {
    val rating: Double by lazy {
        calculateRating(favoritesCount, commentsCount)
    }

    private fun calculateRating(favoritesCount: Int?, commentsCount: Int?): Double {
        return if (favoritesCount != null && commentsCount != null) {
            1 / (1 + exp(-(favoritesCount.toDouble() / (commentsCount + 1))))
        } else {
            0.0
        }
    }
}
