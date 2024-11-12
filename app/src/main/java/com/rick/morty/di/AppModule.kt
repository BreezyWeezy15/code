package com.rick.morty.di

import android.content.Context
import androidx.room.Room
import com.rick.morty.business.MainRepo
import com.rick.morty.business.MainRepoImpl
import com.rick.morty.db.AppDatabase
import com.rick.morty.db.CharacterDao
import com.rick.morty.services.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {


    @Singleton
    @Provides
    fun getRetrofitInstance() : AuthService {
        return Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(AuthService::class.java)
    }

    @Singleton
    @Provides
    fun getAppDatabaseInstance(@ApplicationContext context: Context) =
        Room.databaseBuilder(context,AppDatabase::class.java,"characters.db")
            .fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun getDaoInstance(appDatabase: AppDatabase) = appDatabase.characterDao()

    @Singleton
    @Provides
    fun getMainRepoImplInstance(authService: AuthService,characterDao: CharacterDao) : MainRepo {
        return  MainRepoImpl(authService,characterDao)
    }
}