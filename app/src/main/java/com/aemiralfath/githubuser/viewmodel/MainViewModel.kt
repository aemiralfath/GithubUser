package com.aemiralfath.githubuser.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aemiralfath.githubuser.model.UserData
import com.aemiralfath.githubuser.model.entity.User

class MainViewModel : ViewModel() {
    private var dataUser: MutableLiveData<ArrayList<User>> = MutableLiveData()

    fun setUser() = dataUser.postValue(UserData.getData())

    fun setUser(query: String?) = dataUser.postValue(UserData.getData(query ?: ""))

    fun getDataUser() = dataUser
}