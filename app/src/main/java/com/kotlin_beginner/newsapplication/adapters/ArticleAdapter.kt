package com.kotlin_beginner.newsapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlin_beginner.newsapplication.data.model.Article
import com.kotlin_beginner.newsapplication.databinding.ItemArticlePreviewBinding

class ArticleAdapter (private val listener: OnItemClickListener) : ListAdapter<Article, ArticleAdapter.ArticleViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    inner class ArticleViewHolder(private val binding: ItemArticlePreviewBinding): RecyclerView.ViewHolder(binding.root){
        init{
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val article = getItem(position)
                        listener.onItemClick(article, root)
                    }
                }
            }
        }


        fun bind(article: Article) {
            binding.apply {
                Glide.with(itemView)
                    .load(article.urlToImage)
                    .into(ivArticleImage)

                tvDescription.text = article.description
                tvTitle.text = article.title
                tvPublishedAt.text = article.formattedPublishedAt
                tvSource.text = article.source?.name
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(article: Article, root: View)
    }

    class DiffCallBack: DiffUtil.ItemCallback<Article> (){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

}