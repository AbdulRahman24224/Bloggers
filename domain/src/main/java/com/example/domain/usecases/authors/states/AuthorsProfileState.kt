package com.example.domain.usecases.authors.states

import android.os.Parcelable
import com.example.data.entities.Author
import com.example.data.entities.BaseState
import com.example.data.entities.Post
import kotlinx.parcelize.Parcelize


@Parcelize
data class AuthorsProfileState(
    val hasMoreData: Boolean = true,
    val author: Author = Author(),
    val posts: MutableList<Post> = mutableListOf(),
    var status: String = "true",
    var page: Int = 1,
    override var error: String = "",
    override var isLoading: Boolean = false

) : BaseState(), Parcelable