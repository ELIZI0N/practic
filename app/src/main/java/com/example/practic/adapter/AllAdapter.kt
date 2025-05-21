package com.example.practic.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practic.DetailMangaActivity
import com.example.practic.R
import com.example.practic.data.Manga

class AllAdapter(private var mangaList: List<Manga>, private val onItemClick: (Manga) -> Unit) : RecyclerView.Adapter<AllAdapter.MangaViewHolder>() {

    class MangaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mangaImage: ImageView = itemView.findViewById(R.id.manga_img_stolb)
        val mangaTitle: TextView = itemView.findViewById(R.id.manga_title_stolb)
        val authorManga: TextView = itemView.findViewById(R.id.author_manga_stolb)
        val chapters: TextView = itemView.findViewById(R.id.all_recycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.stolbec_mang, parent, false)
        return MangaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MangaViewHolder, position: Int) {
        val currentItem = mangaList[position]

        holder.mangaTitle.text = currentItem.name
        holder.authorManga.text = currentItem.authors
        holder.chapters.text = currentItem.chapters?.toString() ?: "N/A"

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