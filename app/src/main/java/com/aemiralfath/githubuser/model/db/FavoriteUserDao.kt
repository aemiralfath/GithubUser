package com.aemiralfath.githubuser.model.db

import android.database.Cursor
import androidx.room.*
import com.aemiralfath.githubuser.model.entity.FavoriteUser
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteUserDao {
    @Query("SELECT * FROM favorite_user")
    fun getAll(): Flow<List<FavoriteUser>>

    @Query("SELECT * FROM favorite_user WHERE username LIKE :username LIMIT 1")
    fun findByUsername(username: String): Flow<FavoriteUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg user: FavoriteUser)

    @Delete
    suspend fun delete(user: FavoriteUser)

    @Query("DELETE FROM favorite_user")
    suspend fun deleteAll()

    @Query("SELECT * FROM favorite_user")
    fun getAllFavorite(): Cursor
}