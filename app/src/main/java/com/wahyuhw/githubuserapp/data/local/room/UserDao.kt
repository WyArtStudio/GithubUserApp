package com.wahyuhw.githubuserapp.data.local.room

import androidx.room.*
import com.wahyuhw.githubuserapp.data.shared.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * from user_table WHERE username= :username")
    suspend fun getUser(username: String): User?

    @Query("SELECT * from user_table ORDER BY username ASC")
    suspend fun getListUserFavorite(): List<User>

    @Query("DELETE from user_table")
    suspend fun clearUser()
}