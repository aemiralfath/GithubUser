package com.aemiralfath.githubuser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aemiralfath.githubuser.model.db.FavoriteUserRepository

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