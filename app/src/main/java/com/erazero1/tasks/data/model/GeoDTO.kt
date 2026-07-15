package com.erazero1.tasks.data.model

import com.erazero1.tasks.domain.model.Geo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoDTO(
    @SerialName("lat")
    val lat: String?,
    @SerialName("lng")
    val lng: String?,
)

fun GeoDTO.toDomain() = Geo(
    lat = this.lat?.toFloat() ?: 0F,
    lng = this.lng?.toFloat() ?: 0F,
)