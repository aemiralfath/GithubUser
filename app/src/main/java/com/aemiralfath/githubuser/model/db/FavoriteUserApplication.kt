package com.aemiralfath.githubuser.model.db

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class FavoriteUserApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { FavoriteUserRepository(database.favoriteUserDao()) }
}