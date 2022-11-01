package com.kotlin_beginner.newsapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kotlin_beginner.newsapplication.R
import com.kotlin_beginner.newsapplication.databinding.ActivityNewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    private var _binding : ActivityNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)

        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment)

        if (fragment != null) {
            binding.bottomNavigationView.setupWithNavController(fragment.findNavController())
        }

    }
}