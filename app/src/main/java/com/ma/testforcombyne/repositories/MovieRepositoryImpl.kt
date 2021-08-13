package com.ma.testforcombyne.repositories

import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.ma.testforcombyne.*
import com.ma.testforcombyne.graphql.MyApi
import com.ma.testforcombyne.type.CreateMovieInput
import com.ma.testforcombyne.type.DeleteMovieInput
import com.ma.testforcombyne.type.UpdateMovieInput
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val webService: MyApi) : MovieRepository {

	override suspend fun getMovieList(): Response<GetMoviesQuery.Data> {
		return webService.getApolloClient().query(GetMoviesQuery()).await()
	}

	override suspend fun getMovie(id: String): Response<GetMovieQuery.Data> {
		return webService.getApolloClient().query(GetMovieQuery(id)).await()
	}

	override suspend fun createMovie(input: CreateMovieInput): Response<CreateMovieMutation.Data> {
		return webService.getApolloClient().mutate(CreateMovieMutation(input)).await()
	}

	override suspend fun updateMovie(input: UpdateMovieInput): Response<UpdateMovieMutation.Data> {
		return webService.getApolloClient().mutate(UpdateMovieMutation(input)).await()
	}

	override suspend fun deleteMovie(input: DeleteMovieInput): Response<DeleteMovieMutation.Data> {
		return webService.getApolloClient().mutate(DeleteMovieMutation(input)).await()
	}

}