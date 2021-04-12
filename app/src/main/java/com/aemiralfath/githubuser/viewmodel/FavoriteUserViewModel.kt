package com.aemiralfath.githubuser.viewmodel

import androidx.lifecycle.*
import com.aemiralfath.githubuser.model.db.FavoriteUserRepository
import com.aemiralfath.githubuser.model.entity.FavoriteUser
import kotlinx.coroutines.launch

class FavoriteUserViewModel(private val repository: FavoriteUserRepository) : ViewModel() {

    private lateinit var dataUser: LiveData<FavoriteUser>
    private lateinit var dataUsers: LiveData<List<FavoriteUser>>

    fun setUser() {
        viewModelScope.launch {
            repository.setFavoriteUser()
            dataUsers = repository.favoriteUser.asLiveData()
        }
    }

    fun setUserByUsername(username: String) {
        viewModelScope.launch {
            repository.setFavoriteUserByUsername(username)
            dataUser = repository.favoriteUserByUsername.asLiveData()
        }
    }

    fun addToFavoriteUser(user: FavoriteUser) {
        viewModelScope.launch { repository.insert(user) }
    }

    fun removeFromFavoriteUser(user: FavoriteUser) {
        viewModelScope.launch { repository.delete(user) }
    }

    fun getDataUser() = dataUser

    fun getDataUsers() = dataUsers
}

class FavoriteUserViewModelFactory(private val repository: FavoriteUserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteUserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteUserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}