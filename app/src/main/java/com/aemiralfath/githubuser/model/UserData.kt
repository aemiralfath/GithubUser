package com.aemiralfath.githubuser.model

import android.content.res.TypedArray
import com.aemiralfath.githubuser.model.entity.User
import java.util.*
import kotlin.collections.ArrayList

object UserData {
    private val listData: ArrayList<User> = arrayListOf()

    fun setData(
        username: Array<String>,
        name: Array<String>,
        location: Array<String>,
        company: Array<String>,
        repository: Array<String>,
        followers: Array<String>,
        following: Array<String>,
        avatar: TypedArray
    ) {
        listData.clear()
        for (position in username.indices) {
            listData.add(
                User(
                    username[position],
                    name[position],
                    company[position],
                    location[position],
                    repository[position],
                    followers[position],
                    following[position],
                    avatar.getResourceId(position, -1),
                )
            )
        }
    }

    fun getData(): ArrayList<User> = listData

    fun getData(query: String): ArrayList<User> {
        val list = arrayListOf<User>()
        listData.forEach {
            if (it.username?.toLowerCase(Locale.ROOT)
                    ?.contains(query) == true || it.name?.toLowerCase(Locale.ROOT)
                    ?.contains(query) == true
            ) {
                list.add(it)
            }
        }
        return list
    }

    fun getDataUsername(username: String): User {
        listData.forEach {
            if (it.username?.equals(username) == true) {
                return it
            }
        }
        return User()
    }

}