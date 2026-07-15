package com.erazero1.tasks.data.model

import com.erazero1.tasks.domain.model.Address
import com.erazero1.tasks.domain.model.Geo
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

fun AddressDTO.toDomain() = Address(
    street = this.street.orEmpty(),
    suite = this.suite.orEmpty(),
    city = this.city.orEmpty(),
    zipcode = this.zipcode.orEmpty(),
    geo = this.geo?.toDomain() ?: Geo()
)