package com.ma.testforcombyne.di

import com.ma.testforcombyne.graphql.MyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

	@Singleton
	@Provides
	fun provideWebService() = MyApi()

}