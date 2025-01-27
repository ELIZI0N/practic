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

class TrendingAdapter(private val onItemClick: (Manga) -> Unit) : RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder>() {

    private var mangaList: List<Manga> = emptyList()

    fun setMangaList(list: List<Manga>) {
        mangaList = list
        notifyDataSetChanged()
    }

    class TrendingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mangaImage: ImageView = itemView.findViewById(R.id.manga_card_image)
        val mangaTitle: TextView = itemView.findViewById(R.id.manga_title)
        val mangaAuthor: TextView = itemView.findViewById(R.id.manga_author)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stroka_mang, parent, false)
        return TrendingViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        val manga = mangaList[position]

        Glide.with(holder.itemView.context)
            .load(manga.images.image_url.image_url)
            .into(holder.mangaImage)

        holder.mangaTitle.text = manga.title
        holder.mangaAuthor.text = manga.authors?.joinToString { it.name } ?: "Неизвестен"

        holder.itemView.setOnClickListener{
            onItemClick(manga)
        }
    }

    override fun getItemCount(): Int {
        return mangaList.size
    }
}
