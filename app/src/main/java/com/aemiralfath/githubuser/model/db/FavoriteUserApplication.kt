package com.aemiralfath.githubuser.model.db

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class FavoriteUserApplication : Application() {
    private val database by lazy { UserDatabase.getDatabase(this) }
    val repository by lazy { FavoriteUserRepository(database.favoriteUserDao()) }
}