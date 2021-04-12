package com.aemiralfath.githubuser.model.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "favorite_user")
data class FavoriteUser(
    @PrimaryKey val username: String,
    @ColumnInfo(name = "link") val link: String?,
    @ColumnInfo(name = "avatar") val avatar: String?
) : Parcelable