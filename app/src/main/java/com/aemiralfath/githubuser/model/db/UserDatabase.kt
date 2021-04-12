package com.aemiralfath.githubuser.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aemiralfath.githubuser.model.entity.FavoriteUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [FavoriteUser::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun favoriteUserDao(): FavoriteUserDao

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val favoriteUserDao = database.favoriteUserDao()
                    favoriteUserDao.deleteAll()
                    favoriteUserDao.insert(
                        FavoriteUser(
                            "aemiralfath",
                            "https://github.com/aemiralfath",
                            "https://avatars.githubusercontent.com/u/30483373?v=4"
                        )
                    )
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "FavoriteUser"
                ).addCallback(AppDatabaseCallback(scope)).build()

                INSTANCE = instance
                instance
            }
        }
    }
}