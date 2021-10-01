package com.example.bloggers.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

abstract class BaseState {
    abstract var isLoading: Boolean
    abstract var error: String
}

@Parcelize
data class AuthorsListState(
    val hasMoreData: Boolean = true,
    val authors: MutableList<Author> = mutableListOf(),
    var status: String = "true",
    var page: Int = 1,
    override var error: String = "",
    override var isLoading: Boolean = false

) : BaseState(), Parcelable