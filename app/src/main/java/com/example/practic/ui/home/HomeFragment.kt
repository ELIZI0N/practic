package com.example.practic.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practic.DetailMangaActivity
import com.example.practic.adapter.AllAdapter
import com.example.practic.adapter.TrendingAdapter
import com.example.practic.data.Manga
import com.example.practic.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var trendingAdapter: TrendingAdapter
    private lateinit var allAdapter: AllAdapter

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
        trendingAdapter = TrendingAdapter { manga ->
            openDetailMangaActivity(manga)
        }
        binding.trendingRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = trendingAdapter
        }
    }
    private fun setupAllRecyclerView() {
        allAdapter = AllAdapter { manga ->
            openDetailMangaActivity(manga)
        }
        binding.allRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = allAdapter
        }
    }

    private fun openDetailMangaActivity(manga: Manga){
        val intent = Intent(context, DetailMangaActivity::class.java)
        intent.putExtra("manga", manga)
        startActivity(intent)
    }

    private fun fetchTopManga() {

    }
}
