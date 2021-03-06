package com.aemiralfath.githubuser.model.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "favorite_user")
data class FavoriteUser(
    @PrimaryKey(autoGenerate = false)
    val username: String,
    val link: String?,
    val avatar: String?
) : Parcelable