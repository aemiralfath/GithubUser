package com.aemiralfath.githubuser.model.db

import androidx.annotation.WorkerThread
import com.aemiralfath.githubuser.model.entity.FavoriteUser
import kotlinx.coroutines.flow.Flow

class FavoriteUserRepository(private val favoriteUserDao: FavoriteUserDao) {

    lateinit var favoriteUser: Flow<List<FavoriteUser>>

    lateinit var favoriteUserByUsername: Flow<FavoriteUser>

    @WorkerThread
    fun setFavoriteUser() {
        favoriteUser = favoriteUserDao.getAll()
    }

    @WorkerThread
    fun setFavoriteUserByUsername(username: String) {
        favoriteUserByUsername = favoriteUserDao.findByUsername(username)
    }

    @WorkerThread
    suspend fun delete(user: FavoriteUser) {
        favoriteUserDao.delete(user)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: FavoriteUser) {
        favoriteUserDao.insert(user)
    }
}