package com.ma.testforcombyne.graphql

import android.os.Looper
import com.apollographql.apollo.ApolloClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class MyApi {

	companion object {
		const val GRAPHQL_URL			= "https://tv-show-manager.combyne.com/graphql"
		const val CLIENT_KEY			= "yiCk1DW6WHWG58wjj3C4pB/WyhpokCeDeSQEXA5HaicgGh4pTUd+3/rMOR5xu1Yi"
		const val APPLICATION_ID		= "AaQjHwTIQtkCOhtjJaN/nDtMdiftbzMWW5N8uRZ+DNX9LI8AOziS10eHuryBEcCI"
	}

	fun getApolloClient(): ApolloClient {
		check(Looper.myLooper() == Looper.getMainLooper()) {
			"Only the main thread can get the apolloClient instance"
		}
		val logging = HttpLoggingInterceptor()
		logging.level = HttpLoggingInterceptor.Level.BODY
		val httpClient = OkHttpClient.Builder()
		httpClient.connectTimeout(300, TimeUnit.SECONDS)
		httpClient.readTimeout(600, TimeUnit.SECONDS)
		httpClient.addInterceptor(logging)
		httpClient.interceptors().add(Interceptor { chain ->
			var request = chain.request()
			request = request.newBuilder()
				.addHeader("Content-Type", "application/json")
				.addHeader("Accept", "application/json")
				.addHeader("X-Parse-Client-Key", CLIENT_KEY)
				.addHeader("X-Parse-Application-Id", APPLICATION_ID)
				.build()
			chain.proceed(request)
		})
		return ApolloClient.builder()
			.serverUrl(GRAPHQL_URL)
			.okHttpClient(httpClient.build())
			.build()
	}

}