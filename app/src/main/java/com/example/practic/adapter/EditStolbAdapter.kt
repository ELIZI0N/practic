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

class EditStolbAdapter(
    private var mangaList: List<Manga>,
    private val onItemClick: (Manga) -> Unit
) : RecyclerView.Adapter<EditStolbAdapter.MangaStolbViewHolder>() {

    inner class MangaStolbViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.manga_card_image)
        val title: TextView = itemView.findViewById(R.id.manga_title)
        val author: TextView = itemView.findViewById(R.id.manga_author)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaStolbViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.stroka_mang, parent, false)
        return MangaStolbViewHolder(view)
    }

    override fun onBindViewHolder(holder: MangaStolbViewHolder, position: Int) {
        val manga = mangaList[position]

        holder.title.text = manga.name
        holder.author.text = manga.authors

        try {
            Glide.with(holder.itemView.context)
                .load(manga.images)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.image)
        } catch (e: Exception) {
            holder.image.setImageResource(R.drawable.ic_launcher_foreground)
        }

        holder.itemView.setOnClickListener {
            onItemClick(manga)
        }
    }

    override fun getItemCount(): Int = mangaList.size

    fun setData(newMangaList: List<Manga>) {
        mangaList = newMangaList
        notifyDataSetChanged()
    }
}