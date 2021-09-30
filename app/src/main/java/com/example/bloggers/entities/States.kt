package com.example.bloggers.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

abstract class BaseState {
    abstract var isLoading: Boolean
    abstract var error : String
}

@Parcelize
data class AuthorsListState (val isIdle :Boolean = true,
                             val authors: MutableList<Author> = mutableListOf(),
                             var status: String ="true",
                             override var error: String ="",
                             override var isLoading: Boolean= false

) : BaseState(), Parcelable