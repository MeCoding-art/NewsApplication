package com.kotlin_beginner.newsapplication.data.remote

import com.kotlin_beginner.newsapplication.data.model.NewsResponse
import com.kotlin_beginner.newsapplication.util.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") country: String = "tr",
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY
    ) : Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") searchQuery: String,
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String =  API_KEY
    ) : Response<NewsResponse>

}