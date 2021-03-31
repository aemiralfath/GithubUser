package com.aemiralfath.githubuser.model.db

data class FavoriteUser(
    val username: String?,
    val link: String?,
    val avatar: String?,
) {
    companion object {
        const val TABLE_FAVORITE: String = "FAVORITE_USER"
        const val USERNAME: String = "USERNAME"
        const val USER_LINK: String = "USER_LINK"
        const val USER_AVATAR: String = "USER_AVATAR"
    }
}