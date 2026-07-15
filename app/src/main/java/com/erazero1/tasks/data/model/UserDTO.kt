package com.erazero1.tasks.data.model

import com.erazero1.tasks.domain.model.Address
import com.erazero1.tasks.domain.model.Company
import com.erazero1.tasks.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    @SerialName("name")
    val name: String?,
    @SerialName("username")
    val username: String?,
    @SerialName("email")
    val email: String?,
    @SerialName("address")
    val address: AddressDTO?,
    @SerialName("phone")
    val phone: String?,
    @SerialName("website")
    val website: String?,
    @SerialName("company")
    val company: CompanyDTO?,
)

fun UserDTO.toDomain() = User(
    name = this.name.orEmpty(),
    username = this.username.orEmpty(),
    email = this.email.orEmpty(),
    address = this.address?.toDomain() ?: Address(),
    phone = this.phone.orEmpty(),
    website = this.website.orEmpty(),
    company = this.company?.toDomain() ?: Company(),
)