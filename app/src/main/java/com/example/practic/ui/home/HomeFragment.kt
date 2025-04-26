package com.example.practic.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practic.data.Manga
import com.example.practic.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTrendingRecyclerView()
        setupAllRecyclerView()
        fetchTopManga()

    }

    private fun setupTrendingRecyclerView() {

    }
    private fun setupAllRecyclerView() {

    }

    private fun openDetailMangaActivity(manga: Manga){

    }

    private fun fetchTopManga() {

    }
}
