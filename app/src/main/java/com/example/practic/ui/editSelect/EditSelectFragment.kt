package com.example.practic.ui.edit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practic.ChangeMangaActivity
import com.example.practic.adapter.EditStolbAdapter
import com.example.practic.adapter.EditStrokaAdapter
import com.example.practic.data.Manga
import com.example.practic.data_base.Repository
import com.example.practic.databinding.FragmentEditSelectBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditSelectFragment : Fragment() {

    private var _binding: FragmentEditSelectBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: Repository
    private lateinit var stolbAdapter: EditStolbAdapter
    private lateinit var strokaAdapter: EditStrokaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = Repository(requireContext())
        setupAdapters()
        loadMangaData()
    }

    private fun setupAdapters() {
        stolbAdapter = EditStolbAdapter(emptyList()) { manga ->
            openEditMangaActivity(manga)
        }
        binding.editMangaStolbRecycler.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = stolbAdapter
        }

        strokaAdapter = EditStrokaAdapter(emptyList()) { manga ->
            openEditMangaActivity(manga)
        }
        binding.editMangaStrokaRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = strokaAdapter
        }
    }

    private fun loadMangaData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val allManga = repository.getAllMangasAsList()

                withContext(Dispatchers.Main) {
                    displayManga(allManga)
                }
            } catch (e: Exception) {
                Log.e("EditMangaSelection", "Error loading manga: ${e.message}")
            }
        }
    }

    private fun displayManga(mangaList: List<Manga>) {
        if (mangaList.size <= 4) {
            binding.editMangaStolbRecycler.visibility = View.VISIBLE
            binding.editMangaStrokaRecycler.visibility = View.GONE
            stolbAdapter.setData(mangaList)
        } else {
            binding.editMangaStolbRecycler.visibility = View.GONE
            binding.editMangaStrokaRecycler.visibility = View.VISIBLE
            strokaAdapter.setData(mangaList)
        }
    }

    private fun openEditMangaActivity(manga: Manga) {
        val intent = Intent(requireContext(), ChangeMangaActivity::class.java)
        intent.putExtra("MANGA_ID", manga.id)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}