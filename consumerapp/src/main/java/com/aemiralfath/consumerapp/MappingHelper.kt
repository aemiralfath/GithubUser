package com.aemiralfath.consumerapp

import android.database.Cursor
import com.aemiralfath.consumerapp.MainActivity.Companion.AVATAR
import com.aemiralfath.consumerapp.MainActivity.Companion.LINK
import com.aemiralfath.consumerapp.MainActivity.Companion.USERNAME

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<FavoriteUser> {
        val usersItem = ArrayList<FavoriteUser>()
        cursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val link = getString(getColumnIndexOrThrow(LINK))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                usersItem.add(FavoriteUser(username, link, avatar))
            }

        }
        return usersItem
    }
}