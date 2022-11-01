package com.kotlin_beginner.newsapplication.ui.breakingNews

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kotlin_beginner.newsapplication.R
import com.kotlin_beginner.newsapplication.adapters.ArticleAdapter
import com.kotlin_beginner.newsapplication.data.model.Article
import com.kotlin_beginner.newsapplication.databinding.FragmentBreakingNewsBinding
import com.kotlin_beginner.newsapplication.util.QUERY_PAGE_SIZE
import com.kotlin_beginner.newsapplication.util.Resource
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "BreakingNewsFragment"
@AndroidEntryPoint
class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news), ArticleAdapter.OnItemClickListener {

    private var binding: FragmentBreakingNewsBinding? = null
    private val viewModel: BreakingNewsViewModel by viewModels()
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val binding = FragmentBreakingNewsBinding.bind(view)
        binding = FragmentBreakingNewsBinding.bind(view)
        val articleAdapter = ArticleAdapter(this)

       binding?.apply {
           rvBreakingNews.apply {
               adapter = articleAdapter
               setHasFixedSize(true)
               addOnScrollListener(this@BreakingNewsFragment.scrollListner )
           }
       }

        viewModel.breakingNews.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding?.paginationProgressBar?.visibility = View.INVISIBLE
                    isLoading = false
                    it.data?.let {  newsResponse ->
                        articleAdapter.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if (isLastPage)
                            binding?.rvBreakingNews?.setPadding(0,0,0,0)
                    }
                }
                is Resource.Error -> {
                    binding?.paginationProgressBar?.visibility = View.INVISIBLE
                    isLoading = true
                    it.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                        Log.e(TAG, "Error: $message")
                    }
                }
                is Resource.Loading -> {
                    binding?.paginationProgressBar?.visibility = View.VISIBLE
                }
            }
        }

    }

    private val scrollListner = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val totalVisibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + totalVisibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getBreakingNews("in")
                isScrolling = false
            }

        }
    }

    override fun onItemClick(article: Article, view: View) {

        val action = BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(article)
        //val action = BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment()

        Navigation.findNavController(view).navigate(action)

    }

}