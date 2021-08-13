package com.ma.testforcombyne.repositories

import com.apollographql.apollo.api.Response
import com.ma.testforcombyne.*
import com.ma.testforcombyne.type.CreateMovieInput
import com.ma.testforcombyne.type.DeleteMovieInput
import com.ma.testforcombyne.type.UpdateMovieInput

interface MovieRepository {

	suspend fun getMovieList(): Response<GetMoviesQuery.Data>
	suspend fun getMovie(id: String): Response<GetMovieQuery.Data>
	suspend fun createMovie(input: CreateMovieInput): Response<CreateMovieMutation.Data>
	suspend fun updateMovie(input: UpdateMovieInput): Response<UpdateMovieMutation.Data>
	suspend fun deleteMovie(input: DeleteMovieInput): Response<DeleteMovieMutation.Data>

}