package com.wahyuhw.githubuserapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.wahyuhw.githubuserapp.data.shared.User
import com.wahyuhw.githubuserapp.data.shared.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val repository = MainRepository(application)

    fun getThemeSetting() = repository.getThemeSetting()

    fun saveThemeSetting(isDarkModeActive: Boolean) = viewModelScope.launch {
        repository.saveThemeSetting(isDarkModeActive)
    }

    fun searchUser(query: String) = repository.searchUser(query)

    fun getListFollowers(username: String) = repository.getUserFollowers(username)

    fun getListFollowing(username: String) = repository.getUserFollowing(username)

    suspend fun getUserDetail(username: String) = repository.getUserDetail(username)

    suspend fun getListFavoriteUser() = repository.getListFavoriteUser()

    fun insertFavoriteUser(user: User) = viewModelScope.launch {
        repository.insertFavoriteUser(user)
    }

    fun deleteFavoriteUser(user: User) = viewModelScope.launch {
        repository.deleteFavoriteUser(user)
    }

    suspend fun clearFavoriteUser() = viewModelScope.launch {
        repository.clearFavoriteUser()
    }
}