package com.ma.testforcombyne.activities

import android.app.DatePickerDialog
import android.view.ContextThemeWrapper
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import com.apollographql.apollo.api.Input
import com.ma.testforcombyne.CreateMovieMutation
import com.ma.testforcombyne.GetMovieQuery
import com.ma.testforcombyne.R
import com.ma.testforcombyne.UpdateMovieMutation
import com.ma.testforcombyne.databinding.ActivityEditMovieBinding
import com.ma.testforcombyne.global.*
import com.ma.testforcombyne.states.ViewState
import com.ma.testforcombyne.type.CreateMovieFieldsInput
import com.ma.testforcombyne.type.CreateMovieInput
import com.ma.testforcombyne.type.UpdateMovieFieldsInput
import com.ma.testforcombyne.type.UpdateMovieInput
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class EditMovieActivity : BaseActivity<ActivityEditMovieBinding>({ ActivityEditMovieBinding.inflate(it) }) {

	private var mMovieId: String? = null
	private val mCalendar = Calendar.getInstance()

	override fun initView() {
		mMovieId = intent.getStringExtra(EXTRA_ID)
		if (mMovieId.isNullOrEmpty()) {
			supportActionBar?.title = "Add TV Show"
			mBinding.txtReleaseDate.text = getDateString()
		} else {
			supportActionBar?.title = "Edit TV Show"
			getMovieInfoTask(mMovieId!!)
		}

		mBinding.editTitle.addTextChangedListener {
			mBinding.btnSave.isEnabled = it.toString().isNotEmpty() && mBinding.editSeasons.text.isNotEmpty()
		}
		mBinding.editSeasons.addTextChangedListener {
			mBinding.btnSave.isEnabled = it.toString().isNotEmpty() && mBinding.editTitle.text.isNotEmpty()
		}
		mBinding.editSeasons.setOnEditorActionListener { _, actionId, _ ->
			if (actionId == EditorInfo.IME_ACTION_DONE){
				saveMovieInfoTask()
				true
			} else {
				false
			}
		}
		mBinding.layoutReleaseDate.setOnClickListener {
			showDatePicker()
		}
		mBinding.btnSave.setOnClickListener {
			saveMovieInfoTask()
		}
	}

	private fun showDatePicker() {
		DatePickerDialog(ContextThemeWrapper(this, R.style.DateTimePickerTheme), { _, year, monthOfYear, dayOfMonth ->
			mCalendar.set(Calendar.YEAR, year)
			mCalendar.set(Calendar.MONTH, monthOfYear)
			mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
			mBinding.txtReleaseDate.text = getDateString(mCalendar)
		}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show()
	}

	private fun onSaveSuccess() {
		if (mMovieId.isNullOrEmpty()) {
			showToast(getString(R.string.added_successfully))
		} else {
			showToast(getString(R.string.saved_successfully))
		}
		finish()
		gotoActivity(MovieListActivity::class.java)
	}

	override fun activityFinish() {
		finish()
	}

	private fun getMovieInfoTask(id: String) {
		mViewModel.getMovieInfo(id)
		mViewModel.mMovieInfo.observe(this) { response ->
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
					val movieInfo: GetMovieQuery.Movie? = response.value?.data?.movie
					if (movieInfo != null) {
						mBinding.editTitle.setText(movieInfo.title)
						mBinding.editSeasons.setText(movieInfo.seasons?.toInt().toString())
						mBinding.txtReleaseDate.text = convertDateFormat(movieInfo.releaseDate)
					}
				}
			}
		}
	}

	private fun saveMovieInfoTask() {
		val title = mBinding.editTitle.text.toString()
		val seasons = toDouble(mBinding.editSeasons.text.toString())
		if (mMovieId.isNullOrEmpty()) {
			val fields = CreateMovieFieldsInput(Input.absent(), title, Input.fromNullable(mCalendar.time), Input.fromNullable(seasons))
			val input = CreateMovieInput(Input.fromNullable(fields))
			mViewModel.createMovie(input)
			mViewModel.mCreateMovie.observe(this) { response ->
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
						val movieInfo: CreateMovieMutation.Movie? = response.value?.data?.createMovie?.movie
						if (movieInfo != null) {
							onSaveSuccess()
						} else {
							showChocoBar(getString(R.string.unexpected_error))
						}
					}
				}
			}
		} else {
			val fields = UpdateMovieFieldsInput(Input.absent(), Input.fromNullable(title), Input.fromNullable(mCalendar.time), Input.fromNullable(seasons))
			val input = UpdateMovieInput(mMovieId!!, Input.fromNullable(fields))
			mViewModel.updateMovie(input)
			mViewModel.mUpdateMovie.observe(this) { response ->
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
						val movieInfo: UpdateMovieMutation.Movie? = response.value?.data?.updateMovie?.movie
						if (movieInfo != null) {
							onSaveSuccess()
						} else {
							showChocoBar(getString(R.string.unexpected_error))
						}
					}
				}
			}
		}
	}
}