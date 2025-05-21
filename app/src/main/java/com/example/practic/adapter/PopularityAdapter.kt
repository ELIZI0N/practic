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

class PopularityAdapter(
    private var mangaList: List<Manga>,
    private val onItemClick: (Manga) -> Unit
) : RecyclerView.Adapter<PopularityAdapter.MangaViewHolder>() {

    class MangaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mangaImage: ImageView = itemView.findViewById(R.id.manga_card_image)
        val mangaTitle: TextView = itemView.findViewById(R.id.manga_title)
        val mangaAuthor: TextView = itemView.findViewById(R.id.manga_author)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.stroka_mang, parent, false)
        return MangaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MangaViewHolder, position: Int) {
        val currentItem = mangaList[position]

        holder.mangaTitle.text = currentItem.name
        holder.mangaAuthor.text = currentItem.authors

        Glide.with(holder.itemView.context)
            .load(currentItem.images)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.mangaImage)

        holder.itemView.setOnClickListener {
            onItemClick(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return mangaList.size
    }

    fun setData(newMangaList: List<Manga>) {
        mangaList = newMangaList
        notifyDataSetChanged()
    }
}