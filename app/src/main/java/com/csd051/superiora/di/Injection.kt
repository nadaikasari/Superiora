package com.csd051.superiora.di

import android.content.Context
import com.csd051.superiora.data.LocalDataSource
import com.csd051.superiora.data.RemoteDataSource
import com.csd051.superiora.data.SuperioraRepository
import com.csd051.superiora.data.room.SuperioraDatabase
import com.csd051.superiora.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): SuperioraRepository {
        val database = SuperioraDatabase.getInstance(context)
        val appExecutors = AppExecutors()
        val localDataSource = LocalDataSource.getInstance(database.superioraDao())
        val remoteDataSource = RemoteDataSource.getInstance()
        return SuperioraRepository.getInstance(localDataSource,appExecutors,remoteDataSource)
    }
}