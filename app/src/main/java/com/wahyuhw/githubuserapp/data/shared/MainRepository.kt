package com.wahyuhw.githubuserapp.data.shared

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.wahyuhw.githubuserapp.data.local.room.UserDao
import com.wahyuhw.githubuserapp.data.local.room.UserDatabase
import com.wahyuhw.githubuserapp.data.prefs.SettingPreferences
import com.wahyuhw.githubuserapp.data.remote.response.SearchResponse
import com.wahyuhw.githubuserapp.data.remote.network.ApiInterface
import com.wahyuhw.githubuserapp.data.remote.network.ResponseResource
import com.wahyuhw.githubuserapp.data.remote.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository(application: Application) {
    private val retrofit: ApiInterface = RetrofitClient.apiInterface
    private val userDao: UserDao
    private val prefs: SettingPreferences

    init {
        val database: UserDatabase = UserDatabase.getDatabase(application)
        userDao = database.userDao()
        prefs = SettingPreferences.getInstance(application)
    }

    fun searchUser(query: String): LiveData<ResponseResource<List<User>>> {
        val users = MutableLiveData<ResponseResource<List<User>>>()

        users.postValue(ResponseResource.Loading())
        retrofit.searchUsers(query).enqueue(object: Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>) {

                val listUser = response.body()?.users
                if (listUser.isNullOrEmpty()) {
                    users.postValue(ResponseResource.Error(null))
                } else {
                    users.postValue(ResponseResource.Success(listUser))
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                users.postValue(ResponseResource.Error(t.message))
            }

        })

        return users
    }

    suspend fun getUserDetail(username: String): LiveData<ResponseResource<User>> {
        val user = MutableLiveData<ResponseResource<User>>()

        user.postValue(ResponseResource.Loading())

        if (userDao.getUser(username) != null) {
            user.postValue(ResponseResource.Success(userDao.getUser(username)))
        } else {
            retrofit.getUser(username).enqueue(object: Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val userData = response.body()
                    user.postValue(ResponseResource.Success(userData))
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    user.postValue(ResponseResource.Error(t.message))
                }
            })
        }

        return user
    }

    fun getUserFollowers(username: String?): LiveData<ResponseResource<List<User>>> {
        val users = MutableLiveData<ResponseResource<List<User>>>()

        users.postValue(ResponseResource.Loading())
        retrofit.getListFollowers(username).enqueue(object: Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>) {
                val listUser = response.body()
                if (listUser.isNullOrEmpty()) {
                    users.postValue(ResponseResource.Error(null))
                } else {
                    users.postValue(ResponseResource.Success(listUser))
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                users.postValue(ResponseResource.Error(t.message))
            }

        })

        return users
    }

    fun getUserFollowing(username: String?): LiveData<ResponseResource<List<User>>> {
        val users = MutableLiveData<ResponseResource<List<User>>>()

        users.postValue(ResponseResource.Loading())
        retrofit.getListFollowing(username).enqueue(object: Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>) {
                val listUser = response.body()
                if (listUser.isNullOrEmpty()) {
                    users.postValue(ResponseResource.Error(null))
                } else {
                    users.postValue(ResponseResource.Success(listUser))
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                users.postValue(ResponseResource.Error(t.message))
            }

        })

        return users
    }

    suspend fun getListFavoriteUser(): LiveData<ResponseResource<List<User>>> {
        val users = MutableLiveData<ResponseResource<List<User>>>()
        users.postValue(ResponseResource.Loading())

        if (userDao.getListUserFavorite().isNullOrEmpty()) {
            users.postValue(ResponseResource.Error(null))
        } else {
            users.postValue(ResponseResource.Success(userDao.getListUserFavorite()))
        }

        return users
    }

    fun getThemeSetting(): LiveData<Boolean> {
        return prefs.getThemeSetting().asLiveData(Dispatchers.IO)
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        prefs.saveThemeSetting(isDarkModeActive)
    }

    suspend fun insertFavoriteUser(user: User) = userDao.insert(user)

    suspend fun deleteFavoriteUser(user: User) = userDao.delete(user)

    suspend fun clearFavoriteUser() = userDao.clearUser()
}