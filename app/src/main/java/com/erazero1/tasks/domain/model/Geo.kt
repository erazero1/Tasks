package com.erazero1.tasks.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Geo(
    val lat: Float = 0F,
    val lng: Float = 0F,
): Parcelable
