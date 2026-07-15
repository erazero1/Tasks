package com.erazero1.tasks.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddressDTO(
    @SerialName("street")
    val street: String?,
    @SerialName("suite")
    val suite: String?,
    @SerialName("city")
    val city: String?,
    @SerialName("zipcode")
    val zipcode: String?,
    @SerialName("geo")
    val geo: GeoDTO?,
)