package com.aemiralfath.githubuser.model.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class MyDatabaseOpenHelper(context: Context) :
    ManagedSQLiteOpenHelper(context, "FavoriteUser.db", null, 1) {

    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            if (instance == null) {
                instance = MyDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as MyDatabaseOpenHelper
        }
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.createTable(
            FavoriteUser.TABLE_FAVORITE, true,
            FavoriteUser.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            FavoriteUser.USERNAME to TEXT + UNIQUE,
            FavoriteUser.USER_NAME to TEXT,
            FavoriteUser.USER_COMPANY to TEXT,
            FavoriteUser.USER_AVATAR to INTEGER
        )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.dropTable(FavoriteUser.TABLE_FAVORITE, true)
    }

}

val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(applicationContext)