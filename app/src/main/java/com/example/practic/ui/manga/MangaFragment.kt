package com.example.practic.ui.manga

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practic.DetailMangaActivity
import com.example.practic.R
import com.example.practic.adapter.AllAdapter
import com.example.practic.data.Manga
import com.example.practic.data_base.Repository
import com.example.practic.databinding.FragmentMangaBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MangaFragment : Fragment() {

    private lateinit var binding: FragmentMangaBinding
    private lateinit var filterButton: View
    private lateinit var modalDialog: Dialog
    private lateinit var repository: Repository
    private lateinit var allAdapter: AllAdapter
    private lateinit var activeFilterTextView: TextView
    private var allMangaList: List<Manga> = emptyList()

    private var currentSortBy: String? = null
    private var currentSortOrder: String? = null
    private var isViewsSortActive: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMangaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = Repository(requireContext())
        activeFilterTextView = binding.root.findViewById(R.id.active_filter)

        modalDialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            val view = LayoutInflater.from(context).inflate(R.layout.modal_window, null)
            setContentView(view)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setCancelable(true)

            window?.attributes?.gravity = Gravity.TOP or Gravity.END
            window?.attributes?.y = 150
        }

        setupAllRecyclerView()
        loadMangaData()

        filterButton = binding.root.findViewById(R.id.filter_btn)
        filterButton.setOnClickListener {
            showModalDialog()
        }
    }

    private fun setupAllRecyclerView() {
        allAdapter = AllAdapter(emptyList()) { manga ->
            openDetailMangaActivity(manga)
        }
        binding.allRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = allAdapter
        }
    }

    private fun openDetailMangaActivity(manga: Manga){
        val intent = Intent(requireContext(), DetailMangaActivity::class.java)
        intent.putExtra("manga", manga)
        startActivity(intent)
    }

    private fun loadMangaData(sortBy: String? = null, sortOrder: String? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            val mangaList = repository.getAllMangas(sortBy, sortOrder)
            withContext(Dispatchers.Main) {
                allMangaList = mangaList
                allAdapter.setData(mangaList)

                currentSortBy = sortBy
                currentSortOrder = sortOrder
                isViewsSortActive = false
                updateActiveFilterText()
            }
        }
    }

    private fun updateActiveFilterText() {
        val filterText = when {
            isViewsSortActive -> "По просмотрам ↓"
            currentSortBy == "ID" && currentSortOrder == "DESC" -> "По Дате добавления ↓"
            currentSortBy == "Release" && currentSortOrder == "DESC" -> "По году выхода ↓"
            currentSortBy == "Chapters" && currentSortOrder == "DESC" -> "По главам ↓"
            currentSortBy == "Score" && currentSortOrder == "DESC" -> "По рейтингу ↓"
            currentSortBy == "Popularity" && currentSortOrder == "ASC" -> "По популярности ↓"
            else -> "Вся манга"
        }
        activeFilterTextView.text = filterText
    }

    private fun showModalDialog() {
        if (!modalDialog.isShowing) {
            modalDialog.show()

            val sortById = modalDialog.findViewById<Button>(R.id.sortById)
            val sortByRelease = modalDialog.findViewById<Button>(R.id.sortByRelease)
            val sortByViews = modalDialog.findViewById<Button>(R.id.sortByViews)
            val sortByChapters = modalDialog.findViewById<Button>(R.id.sortByChapters)
            val sortByScore = modalDialog.findViewById<Button>(R.id.sortByScore)
            val sortByPopularity = modalDialog.findViewById<Button>(R.id.sortByPopularity)

            resetAllSortButtons(sortById, sortByRelease, sortByViews, sortByChapters, sortByScore, sortByPopularity)

            highlightActiveSortButton(sortById, sortByRelease, sortByViews, sortByChapters, sortByScore, sortByPopularity)

            sortById.setOnClickListener {
                if (currentSortBy == "ID" && currentSortOrder == "DESC") {
                    // Если фильтр уже активен - сбрасываем
                    resetFilters()
                } else {
                    // Если фильтр не активен - применяем
                    loadMangaData("ID", "DESC")
                }
                modalDialog.dismiss()
            }

            sortByRelease.setOnClickListener {
                if (currentSortBy == "Release" && currentSortOrder == "DESC") {
                    resetFilters()
                } else {
                    loadMangaData("Release", "DESC")
                }
                modalDialog.dismiss()
            }

            sortByViews.setOnClickListener {
                if (isViewsSortActive) {
                    resetFilters()
                } else {
                    sortByViewsDescending()
                }
                modalDialog.dismiss()
            }

            sortByChapters.setOnClickListener {
                if (currentSortBy == "Chapters" && currentSortOrder == "DESC") {
                    resetFilters()
                } else {
                    loadMangaData("Chapters", "DESC")
                }
                modalDialog.dismiss()
            }

            sortByScore.setOnClickListener {
                if (currentSortBy == "Score" && currentSortOrder == "DESC") {
                    resetFilters()
                } else {
                    loadMangaData("Score", "DESC")
                }
                modalDialog.dismiss()
            }

            sortByPopularity.setOnClickListener {
                if (currentSortBy == "Popularity" && currentSortOrder == "ASC") {
                    resetFilters()
                } else {
                    loadMangaData("Popularity", "ASC")
                }
                modalDialog.dismiss()
            }
        }
    }

    private fun resetFilters() {
        currentSortBy = null
        currentSortOrder = null
        isViewsSortActive = false

        loadMangaData()
    }

    private fun resetAllSortButtons(vararg buttons: Button) {
        buttons.forEach { button ->
            button.background = ContextCompat.getDrawable(requireContext(), R.drawable.sort_button_background)
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
        }
    }

    private fun highlightActiveSortButton(
        sortById: Button,
        sortByRelease: Button,
        sortByViews: Button,
        sortByChapters: Button,
        sortByScore: Button,
        sortByPopularity: Button
    ) {

        when {
            isViewsSortActive -> {
                sortByViews.background = ContextCompat.getDrawable(requireContext(), R.drawable.active_sort_button_background)
                sortByViews.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_color))
            }
            currentSortBy == "ID" && currentSortOrder == "DESC" -> {
                sortById.background = ContextCompat.getDrawable(requireContext(), R.drawable.active_sort_button_background)
                sortById.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_color))
            }
            currentSortBy == "Release" && currentSortOrder == "DESC" -> {
                sortByRelease.background = ContextCompat.getDrawable(requireContext(), R.drawable.active_sort_button_background)
                sortByRelease.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_color))
            }
            currentSortBy == "Chapters" && currentSortOrder == "DESC" -> {
                sortByChapters.background = ContextCompat.getDrawable(requireContext(), R.drawable.active_sort_button_background)
                sortByChapters.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_color))
            }
            currentSortBy == "Score" && currentSortOrder == "DESC" -> {
                sortByScore.background = ContextCompat.getDrawable(requireContext(), R.drawable.active_sort_button_background)
                sortByScore.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_color))
            }
            currentSortBy == "Popularity" && currentSortOrder == "ASC" -> {
                sortByPopularity.background = ContextCompat.getDrawable(requireContext(), R.drawable.active_sort_button_background)
                sortByPopularity.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_color))
            }
        }
    }

    private fun sortByViewsDescending() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val allManga = repository.getAllMangas()

                val sortedList = allManga.sortedByDescending { manga ->
                    manga.views?.replace("[^0-9]".toRegex(), "")?.toIntOrNull() ?: 0
                }

                withContext(Dispatchers.Main) {
                    allMangaList = sortedList
                    allAdapter.setData(sortedList)

                    isViewsSortActive = true
                    currentSortBy = null
                    currentSortOrder = null
                    updateActiveFilterText()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    loadMangaData()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadMangaData()
    }
}