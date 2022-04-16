package com.wahyuhw.githubuserapp.data.shared

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user_table")
data class User(
	@field:SerializedName("id")
	@ColumnInfo(name = "id")
	@PrimaryKey
	val id: Int? = 0,

	@field:SerializedName("bio")
	@ColumnInfo(name = "bio")
	val bio: String? = null,

	@field:SerializedName("login")
	@ColumnInfo(name = "username")
	val username: String? = null,

	@field:SerializedName("company")
	@ColumnInfo(name = "company")
	val company: String? = null,

	@field:SerializedName("public_repos")
	@ColumnInfo(name = "publicRepos")
	val publicRepos: Int? = null,

	@field:SerializedName("followers")
	@ColumnInfo(name = "followers")
	val followers: Int? = null,

	@field:SerializedName("avatar_url")
	@ColumnInfo(name = "avatarUrl")
	val avatarUrl: String? = null,

	@field:SerializedName("following")
	@ColumnInfo(name = "following")
	val following: Int? = null,

	@field:SerializedName("name")
	@ColumnInfo(name = "name")
	val name: String? = null,

	@field:SerializedName("location")
	@ColumnInfo(name = "location")
	val location: String? = null,

	@ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean? = false
): Parcelable
