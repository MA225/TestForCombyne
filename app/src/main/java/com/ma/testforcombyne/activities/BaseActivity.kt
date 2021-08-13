package com.ma.testforcombyne.activities

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.kaopiz.kprogresshud.KProgressHUD
import com.ma.testforcombyne.MyApplication
import com.ma.testforcombyne.R
import com.ma.testforcombyne.viewmodels.CommonViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
abstract class BaseActivity<BINDING : ViewBinding>(val bindingFactory : (LayoutInflater) -> BINDING) : AppCompatActivity() {

	val mViewModel by viewModels<CommonViewModel>()

	lateinit var mApp: MyApplication
	lateinit var mBinding: BINDING
	lateinit var mKProgressHUD: KProgressHUD

	abstract fun initView()
	abstract fun activityFinish()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		mApp = application as MyApplication
		mBinding = bindingFactory(layoutInflater)
		setContentView(mBinding.root)

		mKProgressHUD = KProgressHUD.create(this, KProgressHUD.Style.SPIN_INDETERMINATE)
			.setDimAmount(0.6f)
			.setBackgroundColor(ContextCompat.getColor(this, R.color.progress_color))

		initView()
	}

	override fun onSupportNavigateUp(): Boolean {
		activityFinish()
		return true
	}

	override fun onBackPressed() {
		activityFinish()
	}
}