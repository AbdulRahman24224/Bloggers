package com.example.data.entities

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
    @SerializedName("avatarUrl") val avatarUrl: String="",
    @SerializedName("address") val address: Address= Address(),

    var page: Int= 1
) : Parcelable


@Parcelize
data class Address(
    @SerializedName("latitude") val latitude: Double=0.0,
    @SerializedName("longitude") val longitude: Double=0.0,
) :Parcelable

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

