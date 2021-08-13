package com.ma.testforcombyne.di

import com.ma.testforcombyne.repositories.MovieRepository
import com.ma.testforcombyne.repositories.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

	@Binds
	@ViewModelScoped
	abstract fun bindRepository(repo: MovieRepositoryImpl): MovieRepository

}