package com.example.bloggers.domain.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bloggers.entities.Author

@Dao
interface AuthorsDao {

    @Query("  select * from Author Where page = :pageNumber   ")
    fun retrieveAuthorsByPage(pageNumber: Int): MutableList<Author>

    @Query("  select count(*) from Author ")
    fun retrieveAuthorsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAllAuthors(authors: List<Author?>)


    @Query("DELETE FROM Author ")
    fun clearAll()


}
