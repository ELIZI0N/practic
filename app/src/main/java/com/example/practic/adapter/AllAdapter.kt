package com.example.practic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practic.R
import com.example.practic.data.Manga

class AllAdapter(private val onItemClick: (Manga) -> Unit) : RecyclerView.Adapter<AllAdapter.AllViewHolder>() {

    private var mangaList: List<Manga> = emptyList()
    fun setMangaList(list: List<Manga>) {
        mangaList = list
        notifyDataSetChanged()
    }

    class AllViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mangaImage: ImageView = itemView.findViewById(R.id.manga_img_stolb)
        val mangaTitle: TextView = itemView.findViewById(R.id.manga_title_stolb)
        val mangaAuthor: TextView = itemView.findViewById(R.id.author_manga_stolb)
        val mangaChapters: TextView = itemView.findViewById(R.id.all_recycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stolbec_mang, parent, false)
        return AllViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllViewHolder, position: Int) {
        val manga = mangaList[position]
        Glide.with(holder.itemView.context)
            .load(manga.images.image_url.image_url)
            .into(holder.mangaImage)

        holder.mangaTitle.text = manga.title
        holder.mangaAuthor.text = manga.authors?.joinToString { it.name } ?: "Неизвестен"
        holder.mangaChapters.text = "Chapters: ${manga.chapters ?: "Неизвестно"}"

        holder.itemView.setOnClickListener{
            onItemClick(manga)
        }
    }

    override fun getItemCount(): Int {
        return mangaList.size
    }
}
