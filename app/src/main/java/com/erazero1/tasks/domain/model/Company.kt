package com.erazero1.tasks.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Company(
    val name: String = "",
    val catchPhrase: String = "",
    val bs: String = "",
): Parcelable
