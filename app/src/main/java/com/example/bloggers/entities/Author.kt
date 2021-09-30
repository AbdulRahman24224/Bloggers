package com.example.bloggers.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Author (
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("userName") val userName : String,
    @SerializedName("email") val email : String,
    @SerializedName("avatarUrl") val avatarUrl : String
) : Serializable

data class Post (
    @SerializedName("id") val id : Int,
    @SerializedName("authorId") val authorId : Int,
    @SerializedName("date") val date : String,
    @SerializedName("title") val title : String,
    @SerializedName("body") val body : String,
    @SerializedName("imageUrl") val imageUrl : String,

): Serializable

