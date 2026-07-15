package com.erazero1.tasks.data.model

import com.erazero1.tasks.domain.model.Company
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompanyDTO(
    @SerialName("name")
    val name: String?,
    @SerialName("catchPhrase")
    val catchPhrase: String?,
    @SerialName("bs")
    val bs: String?,
)

fun CompanyDTO.toDomain() = Company(
    name = this.name.orEmpty(),
    catchPhrase = this.catchPhrase.orEmpty(),
    bs = this.bs.orEmpty(),
)