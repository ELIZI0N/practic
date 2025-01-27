package com.example.practic.ui.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practic.DetailMangaActivity
import com.example.practic.R
import com.example.practic.adapter.AllAdapter
import com.example.practic.adapter.TrendingAdapter
import com.example.practic.api.RetrofitHelper
import com.example.practic.data.Manga
import com.example.practic.data.TopMangaResponse
import com.example.practic.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var trendingAdapter: TrendingAdapter
    private lateinit var allAdapter: AllAdapter
    private lateinit var allMangas: List<Manga>
    private lateinit var search_button: View
    private lateinit var width_search_button: View
    private lateinit var filtr_button: View
    private lateinit var more_button: View
    private lateinit var button_all: View
    private lateinit var button4: View
    private lateinit var button5: View
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

        button_all = binding.root.findViewById(R.id.button_all)
        button_all.setOnClickListener {
            showModalDialog()
        }

        button4 = binding.root.findViewById(R.id.button4)
        button4.setOnClickListener {
            showModalDialog()
        }

        button5 = binding.root.findViewById(R.id.button5)
        button5.setOnClickListener {
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
        val api = RetrofitHelper.api
        api.getTopManga().enqueue(object : Callback<TopMangaResponse> {
            override fun onResponse(
                call: Call<TopMangaResponse>,
                response: Response<TopMangaResponse>
            ) {
                if (response.isSuccessful) {
                    allMangas = response.body()?.data ?: emptyList()

                    val popularityMangas = allMangas.sortedByDescending { it.popularity }.take(8)
                    trendingAdapter.setMangaList(popularityMangas)

                    val allMangasSorted = allMangas.sortedBy { it.rank }.take(8)
                    allAdapter.setMangaList(allMangasSorted)

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to fetch data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<TopMangaResponse>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
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
