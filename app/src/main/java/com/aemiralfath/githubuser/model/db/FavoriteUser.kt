package com.aemiralfath.githubuser.model.db

data class FavoriteUser(
    val id: Int?,
    val username: String?,
    val name: String?,
    val company: String?,
    val avatar: Int?,
) {
    companion object {
        const val TABLE_FAVORITE: String = "TABLE_FAVORITE_USER"
        const val ID: String = "ID_"
        const val USERNAME: String = "USERNAME"
        const val USER_NAME: String = "USER_NAME"
        const val USER_COMPANY: String = "USER_COMPANY"
        const val USER_AVATAR: String = "USER_AVATAR"
    }
}