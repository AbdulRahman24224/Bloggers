package com.example.data.di


import com.example.data.remote.MyCallAdapterFactory
import com.example.data.remote.AuthorsApis
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


private const val SERVER_BASE_URL = "https://sym-json-server.herokuapp.com/"

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {


    @Singleton
    @Provides
    fun provideLoggingInterceptor(): LoggingInterceptor {
        return LoggingInterceptor.Builder().setLevel(Level.BODY).tag("Logging")
            .request("Retrofit_Request").response("Retrofit_Response").build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(logging: LoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
              .addInterceptor(logging)
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .create()
    }

    @Singleton
    @Named("Json_Converter")
    @Provides
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(SERVER_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(MyCallAdapterFactory())
    }


    @Singleton
    @Provides
    fun provideBlogService(@Named("Json_Converter") retrofit: Retrofit.Builder): AuthorsApis {
        return retrofit
            .build()
            .create(AuthorsApis::class.java)
    }


}

