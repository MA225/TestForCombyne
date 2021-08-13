package com.ma.testforcombyne.activities

import com.ma.testforcombyne.R
import com.ma.testforcombyne.databinding.ActivityMainBinding
import com.ma.testforcombyne.global.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : BaseActivity<ActivityMainBinding>({ ActivityMainBinding.inflate(it) }) {

	companion object {
		const val BACK_PRESS_TIME_INTERVAL = 3000
	}

	private var mPrevBackPressed: Long = 0

	override fun initView() {
		mBinding.btnAddTVShow.setOnClickListener {
			gotoActivity(EditMovieActivity::class.java)
		}
		mBinding.btnShowTVShowList.setOnClickListener {
			gotoActivity(MovieListActivity::class.java)
		}
	}

	override fun activityFinish() {
		if (System.currentTimeMillis() - mPrevBackPressed < BACK_PRESS_TIME_INTERVAL) {
			finish()
		} else {
			showToast(getString(R.string.press_again_to_exit))
			mPrevBackPressed = System.currentTimeMillis()
		}
	}
}