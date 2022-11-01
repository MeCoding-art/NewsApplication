package com.kotlin_beginner.newsapplication.ui.savedNews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kotlin_beginner.newsapplication.R
import com.kotlin_beginner.newsapplication.adapters.ArticleAdapter
import com.kotlin_beginner.newsapplication.data.model.Article
import com.kotlin_beginner.newsapplication.databinding.FragmentSavedNewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedNewsFragment : Fragment(R.layout.fragment_saved_news),
    ArticleAdapter.OnItemClickListener {

    private val viewModel: SavedNewsViewModel by viewModels()
    private var _binding : FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSavedNewsBinding.bind(view)
        val articleAdapter = ArticleAdapter(this)

        binding.apply {
            rvSavedNews.apply {
                adapter = articleAdapter
                setHasFixedSize(true)
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT
            ){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val article = articleAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onArticleSwiped(article)
                }

            }).attachToRecyclerView(rvSavedNews)

        }


        viewModel.getAllArticles().observe(viewLifecycleOwner) {
            articleAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.savedArticleEvent.collect { event ->
                when (event) {
                    is SavedNewsViewModel.SavedArticleEvent.ShowUndoDeleteArticleMessage -> {
                        Snackbar.make(requireView(), "Article Deleted!", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                viewModel.onUndoDeleteClick(event.article)
                            }.show()
                    }
                }
            }
        }


    }

    override fun onItemClick(article: Article, root: View) {
        val action = SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(article)
        Navigation.findNavController(root).navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}