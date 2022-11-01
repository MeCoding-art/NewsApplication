package com.kotlin_beginner.newsapplication.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin_beginner.newsapplication.data.model.Article
import com.kotlin_beginner.newsapplication.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val newsRepository: NewsRepository
    ) : ViewModel(){

        private val articleEventChannel = Channel<ArticleEvent>()
        val articleEvent = articleEventChannel.receiveAsFlow()

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.insertArticle(article)
            articleEventChannel.send(ArticleEvent.ShowArticleSavedMessage("Article Saved."))
        }
    }


    sealed class ArticleEvent{
        data class ShowArticleSavedMessage(val message: String) : ArticleEvent()
    }

}