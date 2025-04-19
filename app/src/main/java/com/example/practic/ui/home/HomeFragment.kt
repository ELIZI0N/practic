package com.example.practic.ui.home

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
import com.example.practic.adapter.TrendingAdapter
import com.example.practic.data.Manga
import com.example.practic.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var trendingAdapter: TrendingAdapter
    private lateinit var allAdapter: AllAdapter
    private lateinit var search_button: View
    private lateinit var width_search_button: View
    private lateinit var filtr_button: View
    private lateinit var more_button: View
    private lateinit var modalDialog: Dialog

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

        modalDialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            val view = LayoutInflater.from(context).inflate(R.layout.modal_window, null)
            setContentView(view)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setCancelable(true)
        }

        setupTrendingRecyclerView()
        setupAllRecyclerView()
        fetchTopManga()

        search_button = binding.root.findViewById(R.id.search_button)
        search_button.setOnClickListener {
            showModalDialog()
        }

        width_search_button = binding.root.findViewById(R.id.width_search_button)
        width_search_button.setOnClickListener {
            showModalDialog()
        }

        filtr_button = binding.root.findViewById(R.id.filtr_button)
        filtr_button.setOnClickListener {
            showModalDialog()
        }

        more_button = binding.root.findViewById(R.id.more_button)
        more_button.setOnClickListener {
            showModalDialog()
        }
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
