package com.kotlin_beginner.newsapplication.data.model

data class NewsResponse (
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
 )