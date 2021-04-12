package com.aemiralfath.consumerapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteUser(
    var username: String?,
    var link: String?,
    var avatar: String?
) : Parcelable