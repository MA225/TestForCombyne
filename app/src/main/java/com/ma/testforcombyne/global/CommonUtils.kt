package com.ma.testforcombyne.global

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.ma.testforcombyne.BuildConfig
import com.ma.testforcombyne.R
import com.ma.testforcombyne.activities.BaseActivity
import com.pd.chocobar.ChocoBar
import kotlinx.coroutines.ExperimentalCoroutinesApi

fun Context.showChocoBar(message: String, type: String = MESSAGE_ERROR, duration: Int = ChocoBar.LENGTH_LONG) {
	val chocoBar = ChocoBar.builder()
		.setActivity(this as Activity)
		.setText(message)
		.setDuration(duration)

	when (type) {
		MESSAGE_ERROR -> {
			chocoBar.red().show()
		}
		MESSAGE_SUCCESS -> {
			chocoBar.green().show()
		}
		MESSAGE_INFO -> {
			chocoBar.orange().show()
		}
		else -> {
			chocoBar.build().show()
		}
	}
}

fun Context.showToast(msg: String, vararg args: Any) {
	val message = String.format(msg, *args)
	Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun myLog(message: String, vararg args: Any) {
	try {
		if (BuildConfig.DEBUG) {
			Log.d("MyLog", String.format(message, *args))
		}
	} catch (e: java.lang.Exception) {
		e.printStackTrace()
	}
}

// Show ProgressDialog
@ExperimentalCoroutinesApi
fun Context.showProgressDialog(label: String = "") {
	if (this is BaseActivity<*> && !mKProgressHUD.isShowing) {
		runOnUiThread { mKProgressHUD.setLabel(if (label.trim().isEmpty()) null else label)?.show() }
	}
}

// Hide ProgressDialog
@ExperimentalCoroutinesApi
fun Context.hideProgressDialog() {
	if (this is BaseActivity<*> && mKProgressHUD.isShowing) {
		runOnUiThread { mKProgressHUD.dismiss() }
	}
}

// Show Confirm Dialog
fun Context.showConfirmDialog(message: String, listener: DialogInterface.OnClickListener) {
	AlertDialog.Builder(this)
		.setCancelable(false)
		.setMessage(message)
		.setPositiveButton(getString(R.string.yes), listener)
		.setNegativeButton(getString(R.string.no), null)
		.show()
}

// show shake animation
fun shakeAnimation(view: View, vararg args: String) {
	view.parent.requestChildFocus(view, view)
	YoYo.with(Techniques.Shake).duration(200).repeat(2).playOn(view)
	if (view is EditText && args.isNotEmpty()) {
		view.error = args[0]
		view.showKeyboard()
	}
}

// Hide Keyboard
fun View.hideKeyboard() {
	(context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(windowToken, 0)
}

// Show Keyboard
fun EditText.showKeyboard(vararg args: String) {
	requestFocus()
	(context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
	setSelection(text.toString().length)
	if (args.isNotEmpty()) {
		error = args[0]
	}
}

// Go to other Activity WITHOUT finish current activity
fun <T : AppCompatActivity> Context.gotoActivity(activity: Class<T>, vararg args: Pair<String, Any>) {
	val intent = Intent(this, activity)
	args.forEach {
		when (val value = it.second) {
			is Int -> intent.putExtra(it.first, value)
			is String -> intent.putExtra(it.first, value)
			is Boolean -> intent.putExtra(it.first, value)
			is Long -> intent.putExtra(it.first, value)
			is Float -> intent.putExtra(it.first, value)
			is Double -> intent.putExtra(it.first, value)
			is Array<*> -> intent.putExtra(it.first, value)
		}
	}
	startActivity(intent)
}

// Go to other Activity WITH finish current activity
fun <T : AppCompatActivity> Context.gotoActivityClear(activity: Class<T>, vararg args: Pair<String, Any>) {
	val intent = Intent(this, activity)
	args.forEach {
		when (val value = it.second) {
			is Int -> intent.putExtra(it.first, value)
			is String -> intent.putExtra(it.first, value)
			is Boolean -> intent.putExtra(it.first, value)
			is Long -> intent.putExtra(it.first, value)
			is Float -> intent.putExtra(it.first, value)
			is Double -> intent.putExtra(it.first, value)
			is Array<*> -> intent.putExtra(it.first, value)
		}
	}
	intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
	startActivity(intent)
}