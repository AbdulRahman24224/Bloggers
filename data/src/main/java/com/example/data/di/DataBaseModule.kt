package com.example.data.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.entities.Author
import com.example.data.entities.Post
import com.example.data.local.Converters
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
    fun provideAuthorsDao(db: AppDatabase) : com.example.data.local.dao.AuthorsDao = db.authorsDao

}

@Database(
    entities = [Author::class , Post::class], version = 3, exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val authorsDao: com.example.data.local.dao.AuthorsDao
}