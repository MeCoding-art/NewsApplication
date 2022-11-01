package com.kotlin_beginner.newsapplication.ui.search

import android.content.Context
import android.graphics.pdf.PdfDocument
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin_beginner.newsapplication.data.model.NewsResponse
import com.kotlin_beginner.newsapplication.repository.NewsRepository
import com.kotlin_beginner.newsapplication.util.NetworkUtil.Companion.hasInternetConnection
import com.kotlin_beginner.newsapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    @ApplicationContext private val context: Context
    ) : ViewModel() {

        val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        var searchNewsResponse: NewsResponse? = null
        var searchNewsPage = 1

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNews(searchQuery, searchNewsPage)
    }

    private suspend fun safeSearchNews(searchQuery: String, searchNewsPage: Int) {
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(context)) {
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (e: Exception) {
            when(e) {
            is IOException -> searchNews.postValue(Resource.Error("Network Failure!"))
                else -> searchNews.postValue(Resource.Error("Conversion Failure!"))
            }

        }
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResonse ->
                //searchNewsPage++
                if (searchNewsResponse == null)
                    searchNewsResponse = resultResonse
                else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResonse.articles
                    //oldArticles?.addAll(newArticles)
                    oldArticles?.clear()
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResonse)
            }
        }
        return Resource.Error(response.message())
    }

}