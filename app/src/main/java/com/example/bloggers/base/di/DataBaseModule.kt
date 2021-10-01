package com.example.bloggers.base.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bloggers.domain.data.database.AuthorsDao
import com.example.bloggers.entities.Author
import com.example.bloggers.entities.Post
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {


    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext app: Context): AppDatabase {
       return Room.databaseBuilder(
            app,
            AppDatabase::class.java, "database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthorsDao(db: AppDatabase) :AuthorsDao= db.authorsDao

}

@Database(
    entities = [Author::class , Post::class], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val authorsDao: AuthorsDao
}