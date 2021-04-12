package com.aemiralfath.githubuser.model.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.aemiralfath.githubuser.model.db.FavoriteUserDao
import com.aemiralfath.githubuser.model.db.UserDatabase

class FavoriteUserProvider : ContentProvider() {

    private lateinit var favoriteUserDao: FavoriteUserDao

    companion object {
        private const val AUTHORITY = "com.aemiralfath.githubuser"
        private const val TABLE_NAME = "favorite_user"
        private const val SCHEME = "content"
        private const val USER = 1

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()

        init {
            uriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun onCreate(): Boolean {
        favoriteUserDao = context?.let { UserDatabase.getDatabase(it).favoriteUserDao() }!!
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when(uriMatcher.match(uri)){
            USER -> favoriteUserDao.getAllFavorite()
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}