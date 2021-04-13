package com.aemiralfath.githubuser.model.db

import android.app.Application

class FavoriteUserApplication : Application() {
    private val database by lazy { UserDatabase.getDatabase(this) }
    val repository by lazy { FavoriteUserRepository(database.favoriteUserDao()) }
}