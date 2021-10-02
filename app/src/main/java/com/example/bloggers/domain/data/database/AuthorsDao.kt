package com.example.bloggers.domain.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bloggers.entities.Author
import com.example.bloggers.entities.Post

@Dao
interface AuthorsDao {

    @Query("  select * from Author Where page = :pageNumber   ")
    fun retrieveAuthorsByPage(pageNumber: Int): MutableList<Author>

    @Query("  select * from Author Where id = :authorId   ")
    fun retrieveAuthorById(authorId: Int): MutableList<Author>

    @Query("  select count(*) from Author ")
    fun retrieveAuthorsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAuthors(authors: List<Author?>)

    // todo should this be in a separate dao
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAuthorPosts(authors: List<Post?>)

    @Query("  select * from Post Where page = :pageNumber  and authorId = :authorId  ")
    fun retrieveAuthorPostsByPage(authorId: Int ,pageNumber: Int): MutableList<Post>


    @Query("DELETE FROM Author ")
    fun clearAll()


}
