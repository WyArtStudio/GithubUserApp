package com.wahyuhw.githubuserapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.wahyuhw.githubuserapp.data.shared.User

import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResponse(
    @field:SerializedName("items")
    val users: List<User>
) : Parcelable
