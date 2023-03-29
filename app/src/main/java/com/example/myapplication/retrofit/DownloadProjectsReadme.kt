package com.example.myapplication.retrofit

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Streaming

private const val BASE_URL = "https://github.com/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .build()

interface GitHubInterface {
    @Streaming
    @GET("bumptech/glide/blob/master/README.md")
    suspend fun getGlideReadme() : Response<ResponseBody>

    @Streaming
    @GET("udacity/nd940-c3-advanced-android-programming-project-starter/blob/master/README.md")
    suspend fun getUdacityReadme() : Response<ResponseBody>

    @Streaming
    @GET("square/retrofit/blob/master/README.md")
    suspend fun getRetrofitReadme() : Response<ResponseBody>
}

object DownloadProjectsReadme {
    val service:GitHubInterface by lazy {
        retrofit.create(GitHubInterface::class.java)
    }
}