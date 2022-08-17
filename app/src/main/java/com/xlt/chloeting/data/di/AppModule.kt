package com.xlt.chloeting.data.di

import com.xlt.chloeting.data.api.RecipesApi
import com.xlt.chloeting.data.api.WorkoutProgramsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient()
            .newBuilder()
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .callTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(httpLoggingInterceptor)
            .build()

        return Retrofit
            .Builder()
            .baseUrl("https://api.chloeting.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWorkoutPrograms(retrofit: Retrofit): WorkoutProgramsApi
            = retrofit.create(WorkoutProgramsApi::class.java)

    @Provides
    @Singleton
    fun provideRecipesApi(retrofit: Retrofit): RecipesApi
            = retrofit.create(RecipesApi::class.java)


}