package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entities.Author
import com.example.data.entities.Post

@Dao
interface AuthorsDao {

    @Query("  select * from Author Where page = :pageNumber   ")
   suspend fun retrieveAuthorsByPage(pageNumber: Int): MutableList<Author>

    @Query("  select * from Author Where id = :authorId   ")
   suspend fun retrieveAuthorById(authorId: Int): MutableList<Author>

    @Query("  select count(*) from Author ")
    suspend fun retrieveAuthorsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAuthors(authors: List<Author?>)

    // todo should this be in a separate dao
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
   suspend fun insertAuthorPosts(authors: List<Post?>)

    @Query("  select * from Post Where page = :pageNumber  and authorId = :authorId  ")
    suspend fun retrieveAuthorPostsByPage(authorId: Int ,pageNumber: Int): MutableList<Post>


    @Query("DELETE FROM Author ")
    suspend fun deleteAllAuthors()

    @Query("DELETE FROM Post Where   authorId = :authorId")
    suspend fun deleteAuthorPosts(authorId: Int)


}
