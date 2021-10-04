package com.example.domain.repositories


import com.example.data.di.AppDatabase
import com.example.data.remote.Authors
import com.example.data.remote.AuthorsApis
import com.example.data.remote.Posts
import javax.inject.Inject


class AuthorsRepository
@Inject constructor(
    private val service: AuthorsApis,
    private val database: AppDatabase
) {


    suspend fun isDBEmpty() = database.authorsDao.retrieveAuthorsCount() == 0

    suspend fun getAuthorById(authorId: Int) = database.authorsDao.retrieveAuthorById(authorId)

    suspend fun getAuthorsFromServer(page: Int) = service.getAuthors(page)

    suspend fun getAuthorPostsFromServer(authorId: Int, page: Int) =
        service.getAuthorPosts(authorId, page)

    suspend fun getAuthorPostsFromDatabase(authorId: Int, page: Int) =
        database.authorsDao.retrieveAuthorPostsByPage(authorId, page)

    suspend fun getAuthorsFromDatabase(page: Int) = database.authorsDao.retrieveAuthorsByPage(page)

    suspend fun deleteAllAuthors() = database.authorsDao.deleteAllAuthors()

    suspend fun deleteAuthorPosts(authorId: Int) = database.authorsDao.deleteAuthorPosts(authorId)

    suspend fun insertAuthors(authors: Authors) = database.authorsDao.insertAuthors(authors)

    suspend fun insertAuthorPosts(posts: Posts) = database.authorsDao.insertAuthorPosts(posts)


}
