package com.example.practic.ui.manga

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
import com.example.practic.data.Manga
import com.example.practic.data.TopMangaResponse
import com.example.practic.databinding.FragmentMangaBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MangaFragment : Fragment() {

    private lateinit var binding: FragmentMangaBinding
    private lateinit var allAdapter: AllAdapter
    private lateinit var allMangas: List<Manga>
    private lateinit var filterButton: View
    private lateinit var modalDialog: Dialog

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

        modalDialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            val view = LayoutInflater.from(context).inflate(R.layout.modal_window, null)
            setContentView(view)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setCancelable(true)
        }

        setupRecyclerView()
        fetchMangaData()

        filterButton = binding.root.findViewById(R.id.filter_btn)
        filterButton.setOnClickListener {
            showModalDialog()
        }
    }

    private fun setupRecyclerView() {
        allAdapter = AllAdapter { manga ->
            openDetailMangaActivity(manga)
        }
        binding.allRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = allAdapter
        }
    }
    private fun openDetailMangaActivity(manga: Manga) {
        val intent = Intent(context, DetailMangaActivity::class.java)
        intent.putExtra("manga", manga)
        startActivity(intent)
    }

    private fun fetchMangaData() {

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
