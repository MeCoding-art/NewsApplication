package com.kotlin_beginner.newsapplication.repository

import com.kotlin_beginner.newsapplication.data.local.ArticleDao
import com.kotlin_beginner.newsapplication.data.model.Article
import com.kotlin_beginner.newsapplication.data.model.NewsResponse
import com.kotlin_beginner.newsapplication.data.remote.NewsApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val articleDao: ArticleDao
  ) {

  suspend fun getBreakingNews(countryCode: String, pageNumber: Int) : Response<NewsResponse> {
      return newsApi.getBreakingNews(countryCode, pageNumber)
  }


  suspend fun searchNews(searchQuery: String, pageNumber: Int) : Response<NewsResponse> {
      return newsApi.searchNews(searchQuery, pageNumber)
  }

  fun getAllArticles() = articleDao.getArticles()

  suspend fun insertArticle(article: Article) = articleDao.insert(article)

  suspend fun deleteArticle(article: Article) = articleDao.delete(article)

  suspend fun deleteAllArticles() = articleDao.deleteAllArticles()

}