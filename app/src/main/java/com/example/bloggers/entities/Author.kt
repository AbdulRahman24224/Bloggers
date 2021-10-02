package com.example.bloggers.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Entity
@Parcelize
data class Author(
    @field:PrimaryKey
    @SerializedName("id") val id: Int=0,
    @SerializedName("name") val name: String="",
    @SerializedName("userName") val userName: String="",
    @SerializedName("email") val email: String="",
    @SerializedName("avatarUrl") val avatarUrl: String="" ,
    var page: Int= 1
) : Parcelable


@Entity
@Parcelize
data class Post(
    @field:PrimaryKey
    @SerializedName("id") val id: Int=0,
    @SerializedName("authorId") val authorId: Int=0,
    @SerializedName("date") val date: String="",
    @SerializedName("title") val title: String="",
    @SerializedName("body") val body: String="",
    @SerializedName("imageUrl") val imageUrl: String="",
    var page: Int= 1
    ) : Parcelable

