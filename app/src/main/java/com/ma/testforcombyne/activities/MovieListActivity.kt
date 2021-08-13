package com.ma.testforcombyne.activities

import com.ma.testforcombyne.DeleteMovieMutation
import com.ma.testforcombyne.GetMoviesQuery
import com.ma.testforcombyne.R
import com.ma.testforcombyne.UpdateMovieMutation
import com.ma.testforcombyne.adapters.MovieAdapter
import com.ma.testforcombyne.databinding.ActivityMovieListBinding
import com.ma.testforcombyne.global.*
import com.ma.testforcombyne.states.ViewState
import com.ma.testforcombyne.type.DeleteMovieInput
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.collections.ArrayList

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MovieListActivity : BaseActivity<ActivityMovieListBinding>({ ActivityMovieListBinding.inflate(it) }) {

	private val mMovieAdapter by lazy { MovieAdapter() }

	override fun initView() {
		supportActionBar?.title = "Added TV Shows"
		mBinding.recyclerView.adapter = mMovieAdapter
		mMovieAdapter.onItemClicked = { id ->
			gotoActivity(EditMovieActivity::class.java, EXTRA_ID to id)
		}
		mMovieAdapter.onDeleteClicked = { id ->
			showConfirmDialog(getString(R.string.confirm_delete)) { _, _ ->
				deleteMovieTask(id)
			}
		}
		mBinding.cardAdd.setOnClickListener {
			gotoActivity(EditMovieActivity::class.java)
		}
	}

	override fun onResume() {
		super.onResume()
		getMovieListTask()
	}

	override fun activityFinish() {
		finish()
	}

	private fun getMovieListTask() {
		mViewModel.getMovieList()
		mViewModel.mMovieList.observe(this) { response ->
			when (response) {
				is ViewState.Loading -> showProgressDialog()
				is ViewState.Error -> {
					hideProgressDialog()
					if (!response.message.isNullOrEmpty()) {
						showChocoBar(response.message)
					}
				}
				is ViewState.Success -> {
					val list: MutableList<GetMoviesQuery.Node> = ArrayList()
					response.value?.data?.movies?.edges?.let {
						it.forEach { edge -> edge?.node?.let { node -> list.add(node) } }
					}
					if (list.isEmpty()) {
						showChocoBar(getString(R.string.no_movies_found), MESSAGE_INFO)
					} else {
						mMovieAdapter.submitList(list.reversed())
						mMovieAdapter.notifyDataSetChanged()
					}
					hideProgressDialog()
				}
			}
		}
	}

	private fun deleteMovieTask(id: String) {
		mViewModel.deleteMovie(DeleteMovieInput(id))
		mViewModel.mDeleteMovie.observe(this) { response ->
			when (response) {
				is ViewState.Loading -> showProgressDialog()
				is ViewState.Error -> {
					hideProgressDialog()
					if (!response.message.isNullOrEmpty()) {
						showChocoBar(response.message)
					}
				}
				is ViewState.Success -> {
					hideProgressDialog()
					val movieInfo: DeleteMovieMutation.Movie? = response.value?.data?.deleteMovie?.movie
					if (movieInfo != null) {
						showChocoBar(getString(R.string.deleted_successfully), MESSAGE_SUCCESS)
						getMovieListTask()
					} else {
						showChocoBar(getString(R.string.unexpected_error))
					}
				}
			}
		}
	}
}