package com.jchunga.musicapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class MusicModel(
    val name: String,
    val path: String,
    val author: String?,
    val duration: Long
): Parcelable