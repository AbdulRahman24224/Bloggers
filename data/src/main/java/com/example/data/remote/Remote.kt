package com.example.data.remote


import com.example.data.entities.Author
import com.example.data.entities.Post
import retrofit2.http.GET
import retrofit2.http.Query

typealias Authors = List<Author>
typealias Posts = List<Post>

interface AuthorsApis {

    @GET("authors")
    suspend fun getAuthors(
        @Query("_page") page: Int,
        @Query("limit") limit: Int = 20,
    ): Result<Authors>


    @GET("authors")
    suspend fun getAuthorById(
        @Query("id") authorId: Int
    ): Result<Author>


    @GET("posts")
    suspend fun getAuthorPosts(
        @Query("authorId") authorId: Int,
        @Query("_page") page: Int,
        @Query("limit") limit: Int = 20,
    ): Result<Posts>


}
