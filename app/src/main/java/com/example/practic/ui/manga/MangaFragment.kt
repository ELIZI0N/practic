package com.example.practic.ui.manga

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
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
    private var allMangaList: List<Manga> = emptyList()

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

        repository = Repository(requireContext()) // Initialize Repository

        modalDialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            val view = LayoutInflater.from(context).inflate(R.layout.modal_window, null)
            setContentView(view)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setCancelable(true)
        }

        setupAllRecyclerView()
        loadMangaData() // Load data when the view is created

        filterButton = binding.root.findViewById(R.id.filter_btn)
        filterButton.setOnClickListener {
            showModalDialog()
        }
    }

    private fun setupAllRecyclerView() {
        allAdapter = AllAdapter(emptyList()) { manga ->  // Initialize with click listener
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
            }
        }
    }

    private fun showModalDialog() {
        if (!modalDialog.isShowing) {
            modalDialog.show()

            val modalOverlay = modalDialog.window?.decorView
            modalOverlay?.setOnClickListener {
                if (modalDialog.isShowing) {
                    modalDialog.dismiss()
                }
            }
        }
    }
}