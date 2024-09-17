package model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class Place(
    @JsonNames("id")
    var id: Long? = null
)
