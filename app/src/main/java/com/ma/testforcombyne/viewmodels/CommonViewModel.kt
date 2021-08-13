package com.ma.testforcombyne.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Response
import com.ma.testforcombyne.*
import com.ma.testforcombyne.repositories.MovieRepository
import com.ma.testforcombyne.states.ViewState
import com.ma.testforcombyne.type.CreateMovieInput
import com.ma.testforcombyne.type.DeleteMovieInput
import com.ma.testforcombyne.type.UpdateMovieInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class CommonViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

	val mMovieList: LiveData<ViewState<Response<GetMoviesQuery.Data>>>
		get() = movieList
	val mMovieInfo: LiveData<ViewState<Response<GetMovieQuery.Data>>>
		get() = movieInfo
	val mCreateMovie: LiveData<ViewState<Response<CreateMovieMutation.Data>>>
		get() = createMovie
	val mUpdateMovie: LiveData<ViewState<Response<UpdateMovieMutation.Data>>>
		get() = updateMovie
	val mDeleteMovie: LiveData<ViewState<Response<DeleteMovieMutation.Data>>>
		get() = deleteMovie

	private val movieList by lazy { MutableLiveData<ViewState<Response<GetMoviesQuery.Data>>>() }
	private val movieInfo by lazy { MutableLiveData<ViewState<Response<GetMovieQuery.Data>>>() }
	private val createMovie by lazy { MutableLiveData<ViewState<Response<CreateMovieMutation.Data>>>() }
	private val updateMovie by lazy { MutableLiveData<ViewState<Response<UpdateMovieMutation.Data>>>() }
	private val deleteMovie by lazy { MutableLiveData<ViewState<Response<DeleteMovieMutation.Data>>>() }

	fun getMovieList() = viewModelScope.launch {
		movieList.postValue(ViewState.Loading())
		try {
			val response = repository.getMovieList()
			movieList.postValue(ViewState.Success(response))
		} catch (e: Exception) {
			e.printStackTrace()
			movieList.postValue(ViewState.Error("Error occurs while fetching movies."))
		}
	}

	fun getMovieInfo(id: String) = viewModelScope.launch {
		movieInfo.postValue(ViewState.Loading())
		try {
			val response = repository.getMovie(id)
			movieInfo.postValue(ViewState.Success(response))
		} catch (e: Exception) {
			e.printStackTrace()
			movieInfo.postValue(ViewState.Error("Error occurs while fetching movie info."))
		}
	}

	fun createMovie(input: CreateMovieInput) = viewModelScope.launch {
		createMovie.postValue(ViewState.Loading())
		try {
			val response = repository.createMovie(input)
			createMovie.postValue(ViewState.Success(response))
		} catch (e: Exception) {
			e.printStackTrace()
			createMovie.postValue(ViewState.Error("Error occurs while creating new movie."))
		}
	}

	fun updateMovie(input: UpdateMovieInput) = viewModelScope.launch {
		updateMovie.postValue(ViewState.Loading())
		try {
			val response = repository.updateMovie(input)
			updateMovie.postValue(ViewState.Success(response))
		} catch (e: Exception) {
			e.printStackTrace()
			updateMovie.postValue(ViewState.Error("Error occurs while updating movie."))
		}
	}

	fun deleteMovie(input: DeleteMovieInput) = viewModelScope.launch {
		deleteMovie.postValue(ViewState.Loading())
		try {
			val response = repository.deleteMovie(input)
			deleteMovie.postValue(ViewState.Success(response))
		} catch (e: Exception) {
			e.printStackTrace()
			deleteMovie.postValue(ViewState.Error("Error occurs while deleting movie."))
		}
	}
}