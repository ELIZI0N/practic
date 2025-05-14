package com.example.practic.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practic.adapter.AllAdapter
import com.example.practic.adapter.PopularityAdapter
import com.example.practic.data.Manga
import com.example.practic.data_base.Repository
import com.example.practic.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var toggleButton1: ToggleButton
    private lateinit var toggleButton2: ToggleButton
    private lateinit var toggleButton3: ToggleButton

    private lateinit var trendingAdapter: PopularityAdapter
    private lateinit var allAdapter: AllAdapter
    private lateinit var repository: Repository

    private var allMangaList: List<Manga> = emptyList()

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

        repository = Repository(requireContext())

        toggleButton1 = binding.toggleButton
        toggleButton2 = binding.toggleButton2
        toggleButton3 = binding.toggleButton3

        toggleButton1.isChecked = true

        setupToggleButtons()

        setupTrendingRecyclerView()
        setupAllRecyclerView()

        fetchTopManga()
        fetchAllManga()
    }

    private fun setupToggleButtons() {
        var activeButton: ToggleButton? = toggleButton1

        val toggleListener: (CompoundButton, Boolean) -> Unit = { buttonView, isChecked ->
            if (isChecked) {
                if (buttonView != activeButton) {
                    toggleButton1.isChecked = (buttonView == toggleButton1)
                    toggleButton2.isChecked = (buttonView == toggleButton2)
                    toggleButton3.isChecked = (buttonView == toggleButton3)
                }
                activeButton = buttonView as? ToggleButton
                updateAllRecyclerViewData()
            }
        }

        toggleButton1.setOnCheckedChangeListener(toggleListener)
        toggleButton2.setOnCheckedChangeListener(toggleListener)
        toggleButton3.setOnCheckedChangeListener(toggleListener)
    }

    private fun setupTrendingRecyclerView() {
        trendingAdapter = PopularityAdapter(emptyList())
        binding.trendingRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = trendingAdapter
        }
    }

    private fun setupAllRecyclerView() {
        allAdapter = AllAdapter(emptyList())
        binding.allRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = allAdapter
        }
    }

    private fun updateAllRecyclerViewData() {
        when {
            binding.toggleButton.isChecked -> {
                displayAllManga(allMangaList.take(8))
            }
            binding.toggleButton2.isChecked -> {
                val sortedList = allMangaList.sortedByDescending { it.chapters ?: 0 }
                displayAllManga(sortedList.take(8))
            }
            binding.toggleButton3.isChecked -> {
                val sortedList = allMangaList.sortedByDescending { it.release }
                displayAllManga(sortedList.take(8))
            }
        }
    }

    private fun displayAllManga(mangaList: List<Manga>) {
        allAdapter.setData(mangaList)
    }


    private fun fetchTopManga() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val allManga = repository.getAllMangas(sortBy = null, sortOrder = null)

                val topTrending = allManga.sortedBy { it.popularity ?: 0 }.take(8)

                withContext(Dispatchers.Main) {
                    trendingAdapter.setData(topTrending)
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Error fetching top manga: ${e.message}")
            }
        }
    }

    private fun fetchAllManga() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                allMangaList = repository.getAllMangas(sortBy = null, sortOrder = null)
                withContext(Dispatchers.Main) {
                    updateAllRecyclerViewData()
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Error fetching all manga: ${e.message}")
            }
        }
    }

    private fun openDetailMangaActivity(manga: Manga){

    }
}